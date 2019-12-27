package com.example.playah

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.episode_row.view.*


class EpisodeAdapter(
    private val buttonText: String,
    private val addToListOnClickListener: (episode: Episode) -> Unit,
    private val navigateOnClickListener: (uri: String) -> Unit
) :
    ListAdapter<Episode, EpisodeAdapter.EpisodeViewHolder>(EpisodeDiffCallback()) {

    class EpisodeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.imageView
        val episodeTitle: TextView = view.episodeTitle
        val episodeDescription: TextView = view.episodeDescription
        val listButton: Button = view.listButton

        fun bind(
            episode: Episode,
            buttonText: String,
            addToListOnClickListener: (episode: Episode) -> Unit,
            navigateOnClickListener: (uri: String) -> Unit
        ) {
            episodeTitle.text = episode.title
            episodeDescription.text = episode.description
            listButton.text = buttonText

            Glide.with(view)
                .load(episode.imageurl)
                .into(imageView)

            listButton.setOnClickListener { addToListOnClickListener(episode) }
            view.setOnClickListener { navigateOnClickListener(episode.downloadpodfile.url) }
        }

        companion object {
            fun from(parent: ViewGroup): EpisodeViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.episode_row, parent, false) as View
                return EpisodeViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = getItem(position)
        holder.bind(
            episode,
            buttonText,
            addToListOnClickListener,
            navigateOnClickListener
        )
    }
}

class EpisodeDiffCallback : DiffUtil.ItemCallback<Episode>() {
    override fun areContentsTheSame(oldItem: Episode, newItem: Episode) = oldItem == newItem
    override fun areItemsTheSame(oldItem: Episode, newItem: Episode) = oldItem.id == newItem.id
}