package com.example.playah.Fragments


import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playah.*

import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.fragment_main.*

class ListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: EpisodeAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var model: EpisodesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)
        model.episodes.value = arrayOf<Episode>()
        val context = activity!!.applicationContext

        ApiClient.getEpisodes(context, { response ->
            AsyncTask.execute {
                val moshi = Moshi.Builder().build()
                val jsonAdapter = moshi.adapter(Episodes::class.java)
                val episodesFromApi = jsonAdapter.fromJson(response)?.episodes
                val episodesToInclude = AppDatabase.getInstance(context).userDao().getAll()

                model.episodes.postValue(episodesFromApi?.filter { episode ->
                    episodesToInclude.any { (id) -> id == episode.id }
                }?.toTypedArray())
            }
        }, { e -> Log.d("error", e.message) })

        viewManager = LinearLayoutManager(context)
        viewAdapter = EpisodeAdapter(model.episodes.value!!, "Ta bort från lista", { episode ->
            AsyncTask.execute {
                val dao = AppDatabase.getInstance(context).userDao()
                dao.delete(episode.id)

                model.episodes.postValue(model.episodes.value?.filter { currEp ->
                    currEp.id != episode.id
                }?.toTypedArray())
            }
        }, { uri ->
            findNavController().navigate(ListFragmentDirections.actionListFragmentToEpisodeFragment(uri))
        })

        episodeRecyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val nameObserver = Observer<Array<Episode>> { episodes ->
            viewAdapter.updateDataSet(episodes)
            viewAdapter.notifyDataSetChanged()
        }

        model.episodes.observe(this, nameObserver)
    }
}
