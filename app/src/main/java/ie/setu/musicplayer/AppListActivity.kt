package ie.setu.musicplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ie.setu.app.adapters.AppAdapter
import ie.setu.app.adapters.AppListener
import ie.setu.musicplayer.databinding.ActivityAppListBinding
import main.MainApp
import models.ArtistModel
import models.SongModel

class AppListActivity : AppCompatActivity(), AppListener {

    private var position: Int = 0
    lateinit var app: MainApp
    private lateinit var binding: ActivityAppListBinding
    private lateinit var appAdapter: AppAdapter
    private val items = mutableListOf<AppItem>()  // Central mutable list

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

        // Populate the items list
        items.clear()
        items.addAll(app.artists.findAll().map { AppItem.ArtistItem(it) })
        items.addAll(app.songs.findAll().map { AppItem.SongItem(it) })

        // Set up adapter and recycler view
        appAdapter = AppAdapter(items, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
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
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    // Refresh the entire list
                    items.clear()
                    items.addAll(app.artists.findAll().map { AppItem.ArtistItem(it) })
                    items.addAll(app.songs.findAll().map { AppItem.SongItem(it) })
                    appAdapter.notifyDataSetChanged()
                }

                Activity.RESULT_CANCELED -> {
                    Snackbar.make(binding.root, getString(R.string.add_cancelled), Snackbar.LENGTH_LONG).show()
                }

                99 -> {
                    // Remove item at 'position'
                    if (position in items.indices) {
                        items.removeAt(position)
                        appAdapter.notifyItemRemoved(position)
                    }
                }
            }
        }

    override fun onSongClick(song: SongModel, position: Int) {
        val launcherIntent = Intent(this, MusicActivity::class.java)
        launcherIntent.putExtra("update_song", song)
        this.position = position
        getResult.launch(launcherIntent)
    }

    override fun onArtistClick(artist: ArtistModel, position: Int) {
        val launcherIntent = Intent(this, ArtistActivity::class.java)
        launcherIntent.putExtra("update_artist", artist)
        this.position = position
        getResult.launch(launcherIntent)
    }
}