package com.example.playah.Fragments


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.example.playah.R
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_episode.*

class EpisodeFragment : Fragment() {
    val player by lazy { ExoPlayerFactory.newSimpleInstance(activity?.applicationContext) }
    val args: EpisodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sr_player_view.player = player
        val uri = args.uri

        val dataSourceFactory = DefaultDataSourceFactory(activity?.applicationContext, Util.getUserAgent(activity?.applicationContext, "com.example.playah"))
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(uri))
        player.prepare(videoSource)
    }

    override fun onDestroyView() {
        player.release()
        super.onDestroyView()
    }
}
