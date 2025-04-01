package ie.setu.musicplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.musicplayer.databinding.ActivityAppListBinding
import ie.setu.musicplayer.databinding.ActivityArtistBinding
import ie.setu.musicplayer.databinding.CardArtistBinding
import ie.setu.musicplayer.databinding.CardMusicBinding
import main.MainApp
import models.ArtistModel
import models.SongModel
import java.text.NumberFormat
import java.util.Locale

class AppListActivity: AppCompatActivity() {


    lateinit var app: MainApp
    private lateinit var binding: ActivityAppListBinding
    private lateinit var appAdapter: AppAdapter


    sealed class AppItem {
        data class ArtistItem(val artist: ArtistModel) : AppItem()
        data class SongItem(val song: SongModel) : AppItem()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.title = title

        setSupportActionBar(binding.topAppBar)

        app = application as MainApp



        val items = mutableListOf<AppItem>()
        items.addAll(app.artists.map { AppItem.ArtistItem(it) })
        items.addAll(app.songs.map { AppItem.SongItem(it) })



        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        appAdapter = AppAdapter(items)
        binding.recyclerView.adapter = appAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_addArtist -> {
                val launcherIntent = Intent(this, ArtistActivity::class.java)
                getResult.launch(launcherIntent)
            }

            R.id.item_addSong -> {
                val launcherIntent = Intent(this, MusicActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                // Refresh RecyclerView when new data is added
                val items = mutableListOf<AppItem>()
                items.addAll(app.artists.map { AppItem.ArtistItem(it) })
                items.addAll(app.songs.map { AppItem.SongItem(it) })

                appAdapter = AppAdapter(items)
                binding.recyclerView.adapter = appAdapter
            }
        }
}


class AppAdapter(private var items: List<AppListActivity.AppItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
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
                val binding = CardArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ArtistViewHolder(binding)
            }
            VIEW_TYPE_SONG -> {
                val binding = CardMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MusicViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is AppListActivity.AppItem.ArtistItem -> (holder as ArtistViewHolder).bind(item.artist)
            is AppListActivity.AppItem.SongItem -> (holder as MusicViewHolder).bind(item.song)
        }
    }

    override fun getItemCount(): Int = items.size

    class ArtistViewHolder(private val binding: CardArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: ArtistModel) {
            binding.artistTitle.text = artist.artistTitle
            binding.aboutartist.text = artist.aboutartist
            binding.artistname.text = artist.artistname
            binding.age.text = NumberFormat.getInstance(Locale.getDefault()).format(artist.age)
            binding.dateofbirth.text = artist.dateofbirth
        }
    }

    class MusicViewHolder(private val binding: CardMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SongModel) {
            binding.songname.text = song.songname
            binding.genre.text = song.genre
            binding.duration.text = song.duration.toString()
            binding.releasedate.text = song.releasedate
            binding.isFavourite.text = if (song.isFavourite) "Favourite" else "Not Favourite"
            binding.maxRating.text = NumberFormat.getInstance(Locale.getDefault()).format(song.maxrating)
            binding.songid.text = NumberFormat.getInstance(Locale.getDefault()).format(song.songid)
        }
    }
}