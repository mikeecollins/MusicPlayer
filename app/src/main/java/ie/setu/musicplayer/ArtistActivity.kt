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
import ie.setu.musicplayer.databinding.ActivityArtistBinding
import main.MainApp
import models.ArtistModel
import timber.log.Timber
import timber.log.Timber.i

class ArtistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtistBinding
    var artist = ArtistModel()
    lateinit var app: MainApp//Allows users to avoid using nullable types
    //Lateinit meaning the property will not be initialized at the time of object creation but it will later on

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        binding = ActivityArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())
        i(getString(R.string.mp3_booting))
        val ageOptions = (1..100).map { it.toString() }
        val ageAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ageOptions)
        binding.age.setAdapter(ageAdapter)// https://www.youtube.com/watch?v=jXSNobmB7u4 how I added drop down menu
        binding.age.inputType = 0
        binding.age.setOnClickListener {
            binding.age.showDropDown()
        }
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.artist)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/



        binding.topAppBar.title = title
        setSupportActionBar(binding.topAppBar)

        app = application as MainApp

        var edit = false
        if (intent.hasExtra("update_artist")) {
            edit = true
            artist = intent.extras?.getParcelable("update_artist")!!
            binding.artistTitle.setText(artist.artistTitle)
            binding.artistname.setText(artist.artistname)
            binding.age.setText(artist.age.toString(), false)
            binding.aboutartist.setText(artist.aboutartist)
            binding.dateofbirth.setText(artist.dateofbirth)
            binding.btnAddArtist.setText(R.string.saveArtist)

        }
        binding.btnAddArtist.setOnClickListener() {
            artist.artistTitle = binding.artistTitle.text.toString()
            artist.artistname = binding.artistname.text.toString()
            artist.aboutartist = binding.aboutartist.text.toString()
            artist.age = binding.age.text.toString().toIntOrNull() ?: 0
            artist.dateofbirth = binding.dateofbirth.text.toString()

            if (artist.artistTitle.isNotEmpty()) {
                if (edit){
                    app.artists.update(artist.copy())
                }
                else {
                    app.artists.create(artist.copy())
                }
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(
                    it, getString(R.string.songname),
                    Snackbar.LENGTH_LONG
                ).show()
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
