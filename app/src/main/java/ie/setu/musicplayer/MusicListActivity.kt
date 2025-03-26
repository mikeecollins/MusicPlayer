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
import ie.setu.musicplayer.databinding.ActivityMusicListBinding
import ie.setu.musicplayer.databinding.CardMusicBinding
import main.MainApp
import models.SongModel
import java.text.NumberFormat
import java.util.Locale

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.songs.size)
            }
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
             song.duration = binding.duration.text.toString().toDoubleOrNull() ?: 800.0
             binding.releasedate.text = song.releasedate
             binding.isFavourite.text = if (song.isFavourite) "Favourite" else "Not Favourite"
             binding.maxRating.text = NumberFormat.getInstance(Locale.getDefault()).format(song.maxrating)
             binding.songid.text = NumberFormat.getInstance(Locale.getDefault()).format(song.songid)

         }
     }
 }