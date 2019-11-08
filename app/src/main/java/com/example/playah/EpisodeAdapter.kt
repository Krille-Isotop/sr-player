package com.example.playah

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.episode_row.view.*


class EpisodeAdapter(
    private var dataSet: Array<Episode>,
    private val buttonText: String = "Lägg till i lista",
    private val onClickListener: (episode: Episode) -> Unit
) :
    RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {
    class EpisodeViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.episode_row, parent, false) as View
        return EpisodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = dataSet[position]

        holder.view.episodeTitle.text = episode.title
        holder.view.episodeDescription.text = episode.description
        holder.view.listButton.text = buttonText

        Glide.with(holder.view)
            .load(episode.imageurl)
            .into(holder.view.imageView)

        holder.view.listButton.setOnClickListener {  onClickListener(episode) }
        holder.view.setOnClickListener {
            val intent = Intent(context, EpisodeActivity::class.java).apply {
                putExtra("uri", episode.downloadpodfile.url)
            }
            context.startActivity(intent)
        }
    }

    fun updateDataSet(newEpisodes: Array<Episode>) {
        dataSet = newEpisodes
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}