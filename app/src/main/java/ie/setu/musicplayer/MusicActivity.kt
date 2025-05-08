package ie.setu.musicplayer

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.app.helpers.showImagePicker
import ie.setu.musicplayer.databinding.ActivityMusicBinding
import main.MainApp
import models.SongModel
import timber.log.Timber
import timber.log.Timber.i

class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    private lateinit var app: MainApp
    var song = SongModel()
    private var edit = false

    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var audioIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        Timber.plant(Timber.DebugTree())

        binding.topAppBar.title = title
        setSupportActionBar(binding.topAppBar)

        val ratingOptions = (1..10).map { it.toString() }
        val ratingAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ratingOptions)
        binding.maxRating.setAdapter(ratingAdapter)// https://www.youtube.com/watch?v=jXSNobmB7u4 how I added drop down menu
        binding.maxRating.inputType = 0
        binding.maxRating.setOnClickListener {
            binding.maxRating.showDropDown()
        }


        // Edit mode
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
            Picasso.get().load(song.image).resize(800, 600).into(binding.songImage)
        }
        binding.chooseImageSong.setOnClickListener {
            showImagePicker()
        }


        binding.chooseAudioFile.setOnClickListener {
            showAudioPicker()
        }


        binding.btnAddMusic.setOnClickListener {
            saveSong()
        }
        registerImagePickerCallback()
        registerAudioPickerCallback()
    }

    private fun showImagePicker() {
        val pickImageIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        imageIntentLauncher.launch(pickImageIntent)
    }

    private fun showAudioPicker() {
        val pickAudioIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "audio/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        audioIntentLauncher.launch(pickAudioIntent)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        val imageUri = result.data?.data
                        imageUri?.let {
                            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            song.image = it
                            Picasso.get().load(song.image).resize(800, 600).into(binding.songImage)
                        }
                    }
                    RESULT_CANCELED -> { }
                    else -> { }
                }
            }
    }

    private fun registerAudioPickerCallback() {
        audioIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        val audioUri = result.data?.data
                        audioUri?.let {
                            song.audioUri = it

                            contentResolver.takePersistableUriPermission(
                                audioUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        }
                    }
                    RESULT_CANCELED -> { }
                    else -> { }
                }
            }
    }

    private fun saveSong() {
        song.songname = binding.songname.text.toString()
        song.duration = binding.duration.text.toString().toDoubleOrNull() ?: 800.0
        song.genre = binding.genre.text.toString()
        song.isFavourite = binding.isFavourite.isChecked
        song.releasedate = binding.releasedate.text.toString()
        song.maxrating = binding.maxRating.text.toString().toIntOrNull() ?: 10

        if (song.songname.isNotEmpty()) {
            if (edit) {
                app.songs.update(song)
            } else {
                app.songs.create(song)
            }
            setResult(RESULT_OK)
            finish()
        } else {
            Snackbar.make(binding.root, getString(R.string.artist_title), Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                setResult(RESULT_CANCELED)
                finish()
            }
            R.id.item_delete -> {
                app.songs.delete(song)
                setResult(99)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}






