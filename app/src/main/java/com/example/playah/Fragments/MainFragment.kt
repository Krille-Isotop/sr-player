package com.example.playah.Fragments


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playah.*

import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = activity!!.applicationContext

        ApiClient.getEpisodes(context, { response ->
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Episodes::class.java)
            val episodes = jsonAdapter.fromJson(response)?.episodes

            viewManager = LinearLayoutManager(context)
            viewAdapter = EpisodeAdapter(episodes!!, "Lägg till i lista", { episode ->
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
        }, { e -> Log.d("error", e.message) })

        goToListButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToListFragment()
            findNavController().navigate(action)
        }
    }
}
