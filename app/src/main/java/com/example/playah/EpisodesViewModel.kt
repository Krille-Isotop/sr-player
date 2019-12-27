package com.example.playah

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi

class EpisodesViewModel(application: Application) : AndroidViewModel(application) {
    private val _episodes: MutableLiveData<Array<Episode>> = MutableLiveData<Array<Episode>>()
    val episodes: LiveData<Array<Episode>>
        get() = _episodes

    private val _filteredEpisodes: MutableLiveData<Array<Episode>> = MutableLiveData<Array<Episode>>()
    val filteredEpisodes: LiveData<Array<Episode>>
        get() = _filteredEpisodes

    init {
        ApiClient.getEpisodes(application.applicationContext, { response ->
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Episodes::class.java)
            _episodes.value = jsonAdapter.fromJson(response)?.episodes
        }, { e -> Log.d(EpisodesViewModel::class.java.simpleName, e.message) })

        _filteredEpisodes.value = arrayOf<Episode>()
    }

    fun filterEpisodes(episodesToInclude: List<ListItem>) {
        val newFilteredEpisodes = _episodes.value?.filter { episode ->
            episodesToInclude.any { (id) -> id == episode.id }
        }?.toTypedArray()

        _filteredEpisodes.postValue(newFilteredEpisodes)
    }
}