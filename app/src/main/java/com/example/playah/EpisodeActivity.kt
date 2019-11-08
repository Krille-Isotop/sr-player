package com.example.playah

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_episode.*

class EpisodeActivity : AppCompatActivity() {
    val player by lazy { ExoPlayerFactory.newSimpleInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode)

        sr_player_view.player = player
        val uri = intent.getStringExtra("uri")

        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "com.example.playah"))
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(uri))
        player.prepare(videoSource)
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }
}
