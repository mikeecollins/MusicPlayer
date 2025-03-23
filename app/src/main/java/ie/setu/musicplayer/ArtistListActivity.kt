package ie.setu.musicplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.musicplayer.databinding.ActivityArtistListBinding
import ie.setu.musicplayer.databinding.CardArtistBinding
import ie.setu.musicplayer.databinding.CardMusicBinding
import main.MainApp
import models.ArtistModel

class ArtistListActivity: AppCompatActivity() {


    lateinit var app: MainApp
    private lateinit var binding: ActivityArtistListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = ArtistAdapter(app.artists)
        setContentView(R.layout.activity_artist_list)
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
        }
    }

    }