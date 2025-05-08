package ie.setu.app.adapters

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.musicplayer.AppListActivity
import ie.setu.musicplayer.R
import ie.setu.musicplayer.databinding.CardArtistBinding
import ie.setu.musicplayer.databinding.CardMusicBinding
import models.ArtistModel
import models.SongModel
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale

interface AppListener {
    fun onSongClick(song: SongModel,position : Int)
    fun onArtistClick(artist: ArtistModel, position : Int)
}


class AppAdapter constructor(private var items: List<AppListActivity.AppItem>,
    private val listener: AppListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object { // Defines static like variables and methods
        private const val VIEW_TYPE_ARTIST = 1
        private const val VIEW_TYPE_SONG = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is AppListActivity.AppItem.ArtistItem -> VIEW_TYPE_ARTIST
            is AppListActivity.AppItem.SongItem -> VIEW_TYPE_SONG
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ARTIST -> {
                val binding =
                    CardArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ArtistViewHolder(binding)
            }

            VIEW_TYPE_SONG -> {
                val binding =
                    CardMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MusicViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is AppListActivity.AppItem.ArtistItem -> (holder as ArtistViewHolder).bind(
                item.artist,
                listener
            )

            is AppListActivity.AppItem.SongItem -> (holder as MusicViewHolder).bind(
                item.song,
                listener
            )
        }
    }

    override fun getItemCount(): Int =
        items.size// Returns the size of a collection that contains the item you want to show

    class ArtistViewHolder(private val binding: CardArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: ArtistModel, listener: AppListener) {
            binding.artistTitle.text = artist.artistTitle
            binding.aboutartist.text = artist.aboutartist
            binding.artistname.text = artist.artistname
            binding.age.text = NumberFormat.getInstance(Locale.getDefault()).format(artist.age)
            binding.dateofbirth.text = artist.dateofbirth
            Picasso.get().load(artist.image).resize(200, 200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onArtistClick(artist, adapterPosition) }

        }
    }

    class MusicViewHolder(private val binding: CardMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var mediaPlayer: MediaPlayer? = null
        private var isPlaying = false


        fun bind(song: SongModel, listener: AppListener) {
            binding.songname.text = song.songname
            binding.genre.text = song.genre
            binding.duration.text = song.duration.toString()
            binding.releasedate.text = song.releasedate
            binding.isFavourite.text = if (song.isFavourite) "Favourite" else "Not Favourite"
            binding.maxRating.text =
                NumberFormat.getInstance(Locale.getDefault()).format(song.maxrating)
            Picasso.get().load(song.image).resize(200, 200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onSongClick(song, adapterPosition) }

            var isPlaying = false

            binding.btnPlayAudio.setOnClickListener {// used this tutorial to help me with the audio code https://www.youtube.com/watch?v=nKdRdCjBKxM
                val uri = song.audioUri
                Log.d("AppAdapter", "Trying to play URI: $uri")


                try {
                    if (uri != Uri.EMPTY) {  //https://www.youtube.com/watch?v=s3XRXOQ2Amg used this video to help me play songs in my card
                        val parcelFileDescriptor =
                            binding.root.context.contentResolver.openFileDescriptor(uri, "r")
                        val fileDescriptor = parcelFileDescriptor?.fileDescriptor

                        mediaPlayer = MediaPlayer().apply {
                            setDataSource(fileDescriptor!!)
                            setOnPreparedListener {
                                start()
                                isPlaying = true
                                binding.btnPlayAudio.setImageResource(android.R.drawable.ic_media_pause)
                            }
                            setOnCompletionListener {
                                isPlaying = false
                                binding.btnPlayAudio.setImageResource(android.R.drawable.ic_media_play)
                            }
                            prepareAsync()
                        }
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Audio file is missing",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: IOException) {
                    Log.e("MusicActivity", "Error playing audio", e)
                    Toast.makeText(
                        binding.root.context,
                        "Unable to play audio: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}


            //val uri = Uri.parse(song.audioUri)
           // mediaPlayer = MediaPlayer.create(binding.root.context, uri)
          //  mediaPlayer?.start()
//originally used this code for my audio but it did not work

            //binding.songFileUri.text = "File: ${Uri.parse(song.audioUri).lastPathSegment}"









