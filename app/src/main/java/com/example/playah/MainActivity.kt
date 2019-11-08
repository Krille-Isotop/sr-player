package com.example.playah

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ApiClient.getEpisodes(this, { response ->
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Episodes::class.java)
            val episodes = jsonAdapter.fromJson(response)?.episodes

            viewManager = LinearLayoutManager(this)
            viewAdapter = EpisodeAdapter(episodes!!) { episode ->
                AsyncTask.execute {
                    val dao = AppDatabase.getInstance(this).userDao()
                    if (dao.getById(episode.id) == null) {
                        dao.insert(ListItem(episode.id))
                    }
                }
            }

            episodeRecyclerView.apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }, { e -> Log.d("error", e.message) })


        goToListButton.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }
}
