package ie.setu.musicplayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import ie.setu.musicplayer.databinding.ActivityArtistBinding
import main.MainApp
import models.ArtistModel
import timber.log.Timber
import timber.log.Timber.i

class ArtistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtistBinding
    var artist = ArtistModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artist)


        Timber.plant(Timber.DebugTree())
        i(getString(R.string.mp3_booting))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.artist)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding = ActivityArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        binding.btnAdd.setOnClickListener() {
            artist.artistTitle = binding.artistTitle.text.toString()
            artist.artistname = binding.artistname.text.toString()
            artist.aboutartist = binding.aboutartist.text.toString()
            artist.age = binding.age.text.toString().toIntOrNull() ?: 0
            artist.dateofbirth = binding.dateofbirth.text.toString()

            if (artist.artistTitle.isNotEmpty()) {
                app.artists.add(artist.copy())
                i("add Button Pressed: ${artist}")
                for (i in app.artists.indices) {
                    i("Artist[$i]:${this.app.artists[i]}")

                }
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(it, "add an artist", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

}
