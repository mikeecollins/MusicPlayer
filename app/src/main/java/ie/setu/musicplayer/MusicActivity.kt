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
    var song = SongModel()
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    var edit = false

    lateinit var app: MainApp
    // private lateinit var mediaPlayer: MediaPlayer
    //  private val PICK_AUDIO_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)


        //  val pickAudioIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        //     type = "audio/*"
        //       addCategory(Intent.CATEGORY_OPENABLE)
        //   }
        //   startActivityForResult(pickAudioIntent, PICK_AUDIO_REQUEST_CODE)


        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // enableEdgeToEdge()
        // setContentView(R.layout.activity_music)
        Timber.plant(Timber.DebugTree())
        i(getString(R.string.mp3_booting))

        val ratingOptions = (1..10).map { it.toString() }
        val ratingAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ratingOptions)
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
            Picasso.get()
                .load(song.image)
                .resize(800, 600)
                .into(binding.songImage)

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
                } else {


                    app.songs.create(song.copy())
                }
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(
                    it, getString(R.string.artist_title),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }






        binding.chooseImageSong.setOnClickListener {
            showImagePicker(this, imageIntentLauncher, R.string.select_song_image)
        }
        registerImagePickerCallback()
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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            song.image = image

                            Picasso.get()
                                .load(song.image)
                                .resize(800, 600)
                                .into(binding.songImage)

                            binding.chooseImageSong.setText(R.string.change_song_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}

//@Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
  //  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    //    super.onActivityResult(requestCode, resultCode, data)

    /*    if (requestCode == PICK_AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // ✅ Persist permission to access the file across restarts
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                val newSong = SongModel(
                    songname = "User Selected Song",
                    audioUri = uri.toString(), // <-- Make sure you added this to SongModel
                    duration = 800.0
                )

                (application as MainApp).songs.create(newSong)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
   */









