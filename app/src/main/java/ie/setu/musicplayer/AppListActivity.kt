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
import ie.setu.app.adapters.AppAdapter
import ie.setu.app.adapters.AppListener
import ie.setu.musicplayer.databinding.ActivityAppListBinding
import ie.setu.musicplayer.databinding.ActivityArtistBinding
import ie.setu.musicplayer.databinding.CardArtistBinding
import ie.setu.musicplayer.databinding.CardMusicBinding
import main.MainApp
import models.ArtistModel
import models.ArtistStore
import models.SongModel
import models.SongStore
import java.text.NumberFormat
import java.util.Locale

class AppListActivity: AppCompatActivity(), AppListener {


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



        val items = mutableListOf<AppItem>()//allows the user to add data into AppItem which has both models Song and Artist
        items.addAll(app.artists.findAll().map { AppItem.ArtistItem(it) })
        items.addAll(app.songs.findAll().map { AppItem.SongItem(it) })
        binding.recyclerView.adapter = AppAdapter(items, this)





        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        appAdapter = AppAdapter(items, this)
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
            ActivityResultContracts.StartActivityForResult()// Allows us to open another screen eg a form to add a song or artist) and get the result back.
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                // Refresh RecyclerView when new data is added
                val items = mutableListOf<AppItem>()
                items.addAll(app.artists.findAll().map { AppItem.ArtistItem(it) })
                items.addAll(app.songs.findAll().map { AppItem.SongItem(it) })

                appAdapter = AppAdapter(items,this)
                binding.recyclerView.adapter = appAdapter
            }
        }

    override fun onSongClick(song: SongModel) {
        val launcherIntent = Intent(this, MusicActivity::class.java)
        launcherIntent.putExtra("update_song", song)
        getResult.launch(launcherIntent)

    }

    override fun onArtistClick(artist: ArtistModel) {
        val launcherIntent = Intent(this, ArtistActivity::class.java)
        launcherIntent.putExtra("update_artist", artist)
        getResult.launch(launcherIntent)

        }

    }





