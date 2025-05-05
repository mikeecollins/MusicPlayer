package ie.setu.musicplayer

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import ie.setu.musicplayer.databinding.ActivityMusicBinding
import main.MainApp
import models.SongModel
import timber.log.Timber
import timber.log.Timber.i

class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    var song = SongModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // enableEdgeToEdge()
       // setContentView(R.layout.activity_music)
        Timber.plant(Timber.DebugTree())
        i(getString(R.string.mp3_booting))

        val ratingOptions = (1..10).map { it.toString() }
        val ratingAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ratingOptions)
        binding.maxRating.setAdapter(ratingAdapter)// https://www.youtube.com/watch?v=jXSNobmB7u4 how I added drop down menu
        binding.maxRating.inputType = 0
        binding.maxRating.setOnClickListener {
            binding.maxRating.showDropDown()
        }

       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/


        binding.topAppBar.title = title
        setSupportActionBar(binding.topAppBar)



        app = application as MainApp

        var edit = false

        if (intent.hasExtra("update_song")) {
            edit = true
            song = intent.extras?.getParcelable("update_song")!!
            binding.songname.setText(song.songname)
            binding.maxRating.setText(song.maxrating.toString(), false)
            binding.duration.setText(song.duration.toString())
            binding.genre.setText(song.genre)
            binding.releasedate.setText(song.releasedate)
            binding.isFavourite.isChecked = song.isFavourite
            binding.btnAddMusic.setText(R.string.saveSong)

        }

            binding.btnAddMusic.setOnClickListener() {
            song.songname = binding.songname.text.toString()
            song.duration = binding.duration.text.toString().toDoubleOrNull() ?: 800.0
            song.genre = binding.genre.text.toString()
            song.isFavourite = binding.isFavourite.isChecked
            song.releasedate = binding.releasedate.text.toString()
            song.maxrating = binding.maxRating.text.toString().toIntOrNull() ?: 10
            if (song.songname.isNotEmpty()) {
                if (edit) {
                    app.songs.update(song.copy())
                }
                else {


                    app.songs.create(song.copy())
                }
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(it, getString(R.string.artist_title),
                    Snackbar.LENGTH_LONG).show()
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
        }

