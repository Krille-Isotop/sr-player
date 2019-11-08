package com.example.playah

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_main.*

class ListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: EpisodeAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var model: EpisodesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        model = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)
        model.episodes.value = arrayOf<Episode>()

        ApiClient.getEpisodes(this, { response ->
            AsyncTask.execute {
                val moshi = Moshi.Builder().build()
                val jsonAdapter = moshi.adapter(Episodes::class.java)
                val episodesFromApi = jsonAdapter.fromJson(response)?.episodes
                val episodesToInclude = AppDatabase.getInstance(this).userDao().getAll()

                model.episodes.postValue(episodesFromApi?.filter { episode ->
                    episodesToInclude.any { (id) -> id == episode.id }
                }?.toTypedArray())
            }
        }, { e -> Log.d("error", e.message) })

        viewManager = LinearLayoutManager(this)
        viewAdapter = EpisodeAdapter(model.episodes.value!!, "Ta bort från lista") { episode ->
            AsyncTask.execute {
                val dao = AppDatabase.getInstance(this).userDao()
                dao.delete(episode.id)

                model.episodes.postValue(model.episodes.value?.filter { currEp ->
                    currEp.id != episode.id
                }?.toTypedArray())
            }
        }

        episodeRecyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val nameObserver = Observer<Array<Episode>> { episodes ->
            viewAdapter.updateDataSet(episodes)
            viewAdapter.notifyDataSetChanged()
        }

        model.episodes.observe(this, nameObserver)

        val binding: UserBinding = DataBindingUtil.setContentView(this, R.layout.user)

        binding.setLifecycleOwner(this)
    }
}