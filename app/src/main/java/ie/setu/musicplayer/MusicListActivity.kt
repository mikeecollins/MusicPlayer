package ie.setu.musicplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.musicplayer.databinding.ActivityMusicListBinding
import ie.setu.musicplayer.databinding.CardMusicBinding
import main.MainApp
import models.SongModel

class MusicListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityMusicListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = MusicAdapter(app.songs)
        setContentView(R.layout.activity_music_list)
    }
}

 class MusicAdapter(private var songs: ArrayList<SongModel>) :
         RecyclerView.Adapter<MusicAdapter.MainHolder>() {

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
         val binding = CardMusicBinding
             .inflate(LayoutInflater.from(parent.context), parent, false)

         return MainHolder(binding)

     }

     override fun onBindViewHolder(holder: MainHolder, position: Int) {
         val song = songs[holder.adapterPosition]
         holder.bind(song)
     }

     override fun getItemCount(): Int = songs.size

     class MainHolder(private val binding: CardMusicBinding) :
         RecyclerView.ViewHolder(binding.root) {

         fun bind(song: SongModel) {
             binding.songname.text = song.songname
             binding.genre.text = song.genre
             binding.maxRating = binding.maxRating.text.toString().toIntOrNull() ?: 10
         }
     }
 }