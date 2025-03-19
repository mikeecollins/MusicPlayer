package ie.setu.musicplayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ie.setu.musicplayer.databinding.ActivityArtistBinding
import models.ArtistModel
import timber.log.Timber
import timber.log.Timber.i

class ArtistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtistBinding
    var artist = ArtistModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artist)
        Timber.plant(Timber.DebugTree())
        i("Activity started..")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.artist)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnAdd.setOnClickListener() {
            i("add Button Pressed")
        }
    }
}

