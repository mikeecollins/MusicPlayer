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
import ie.setu.musicplayer.databinding.ActivityArtistListBinding
import ie.setu.musicplayer.databinding.CardArtistBinding
import ie.setu.musicplayer.databinding.CardMusicBinding
import main.MainApp
import models.ArtistModel
import java.text.NumberFormat
import java.util.Locale

class ArtistListActivity: AppCompatActivity() {


    lateinit var app: MainApp
    private lateinit var binding: ActivityArtistListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.title = title
        setSupportActionBar(binding.topAppBar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = ArtistAdapter(app.artists)
        setContentView(R.layout.activity_artist_list)
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
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.artists.size)
            }
        }

}


class ArtistAdapter constructor(private var artists: List<ArtistModel>) :
    RecyclerView.Adapter<ArtistAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardArtistBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)

    }


    override fun onBindViewHolder(holder:ArtistAdapter.MainHolder, position: Int) {
        val artist = artists[holder.adapterPosition]
        holder.bind(artist)
    }

    override fun getItemCount(): Int = artists.size

    class MainHolder(private val binding: CardArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: ArtistModel) {
            binding.artistTitle.text =artist.artistTitle
            binding.aboutartist.text = artist.aboutartist
            binding.artistname.text= artist.artistname
            binding.age.text = NumberFormat.getInstance(Locale.getDefault()).format(artist.age)
            binding.dateofbirth.text = artist.dateofbirth

        }
    }

    }