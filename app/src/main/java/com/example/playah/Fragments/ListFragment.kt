package com.example.playah.Fragments


import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playah.*
import kotlinx.android.synthetic.main.fragment_main.*

class ListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: EpisodeAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val model: EpisodesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = activity!!.applicationContext

        ApiClient.getEpisodes(context, { response ->
            AsyncTask.execute {
                val episodesToInclude = AppDatabase.getInstance(context).listItemDao().getAll()
                model.filterEpisodes(episodesToInclude)
            }
        }, { e -> Log.d("error", e.message) })

        viewManager = LinearLayoutManager(context)
        viewAdapter = EpisodeAdapter(model.episodes.value!!, "Ta bort från lista", { episode ->
            AsyncTask.execute {
                val dao = AppDatabase.getInstance(context).listItemDao()
                dao.delete(episode.id)

                model.deleteEpisode(episode.id)
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
