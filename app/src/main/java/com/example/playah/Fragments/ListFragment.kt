package com.example.playah.Fragments


import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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
    private val appContext = activity!!.applicationContext

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    fun refreshEpisodes() {
        AsyncTask.execute {
            val episodesToInclude = AppDatabase.getInstance(appContext).listItemDao().getAll()
            model.filterEpisodes(episodesToInclude)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewManager = LinearLayoutManager(context)
        viewAdapter = EpisodeAdapter(model.filteredEpisodes.value!!, getString(R.string.remove_from_list), { episode ->
            AsyncTask.execute {
                val dao = AppDatabase.getInstance(appContext).listItemDao()
                dao.delete(episode.id)
                refreshEpisodes()
            }
        }, { uri ->
            findNavController().navigate(ListFragmentDirections.actionListFragmentToEpisodeFragment(uri))
        })

        episodeRecyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val episodesObserver = Observer<Array<Episode>> { episodes ->
            viewAdapter.updateDataSet(episodes)
            viewAdapter.notifyDataSetChanged()
        }

        model.filteredEpisodes.observe(this, episodesObserver)
        refreshEpisodes()
    }
}
