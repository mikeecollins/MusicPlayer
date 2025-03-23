package ie.setu.musicplayer

import android.os.Bundle
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
        enableEdgeToEdge()
        setContentView(R.layout.activity_music)

        Timber.plant(Timber.DebugTree())
        i(getString(R.string.mp3_booting))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)


        app = application as MainApp
        binding.btnAdd.setOnClickListener() {
            song.songname = binding.songname.text.toString()
            song.duration = binding.duration.text.toString().toIntOrNull() ?: 800
            song.genre = binding.genre.text.toString()
            song.isFavourite =
                binding.isFavourite.text.toString() == "false" //I used chatgtp to convert this into boolean
            song.releasedate = binding.releasedate.text.toString()
            song.songid = binding.songid.text.toString().toIntOrNull() ?: 0
            song.maxrating = binding.songid.text.toString().toIntOrNull() ?: 10
            if (song.songname.isNotEmpty()) {
                app.songs.add(song.copy())
                i("add Button Pressed: ${song}")
                for (i in app.songs.indices ) {
                i("Song[$i]:${this.app.songs[i]}")

            }
        }
        else{
            Snackbar
                .make(it, "Enter a song name", Snackbar.LENGTH_LONG)
                .show()
        }
    }
}




        }

