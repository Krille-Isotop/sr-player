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

class MainFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val model: EpisodesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = activity!!.applicationContext

        val episodesObserver = Observer<Array<Episode>> { episodes ->
            if (episodes.size > 0) {
                viewManager = LinearLayoutManager(context)
                viewAdapter = EpisodeAdapter(model.episodes.value!!, getString(R.string.add_to_list), { episode ->
                    AsyncTask.execute {
                        val dao = AppDatabase.getInstance(context).listItemDao()
                        if (dao.getById(episode.id) == null) {
                            dao.insert(ListItem(episode.id))
                        }
                    }
                }, { uri ->
                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToEpisodeFragment(uri))
                })

                episodeRecyclerView.apply {
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
        }

        model.episodes.observe(this, episodesObserver)

        goToListButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToListFragment()
            findNavController().navigate(action)
        }
    }
}
