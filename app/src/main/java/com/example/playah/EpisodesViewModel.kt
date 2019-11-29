package com.example.playah

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi

class EpisodesViewModel(application: Application) : AndroidViewModel(application) {
    val episodes: MutableLiveData<Array<Episode>> by lazy {
        MutableLiveData<Array<Episode>>()
    }

    val filteredEpisodes: MutableLiveData<Array<Episode>> by lazy {
        MutableLiveData<Array<Episode>>()
    }

    init {
        ApiClient.getEpisodes(application.applicationContext, { response ->
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Episodes::class.java)
            episodes.value = jsonAdapter.fromJson(response)?.episodes
        }, { e -> Log.d(EpisodesViewModel::class.java.simpleName, e.message) })

        filteredEpisodes.value = arrayOf<Episode>()
    }

    fun filterEpisodes(episodesToInclude: List<ListItem>) {
        val newFilteredEpisodes = episodes.value?.filter { episode ->
            episodesToInclude.any { (id) -> id == episode.id }
        }?.toTypedArray()

        filteredEpisodes.postValue(newFilteredEpisodes)
    }
}