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

    init {
        ApiClient.getEpisodes(application.applicationContext, { response ->
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Episodes::class.java)
            episodes.value = jsonAdapter.fromJson(response)?.episodes

        }, { e -> Log.d(EpisodesViewModel::class.java.simpleName, e.message) })
    }

    fun filterEpisodes(episodesToInclude: List<ListItem>) {
        episodes.postValue(episodes.value?.filter { episode ->
            episodesToInclude.any { (id) -> id == episode.id }
        }?.toTypedArray())
    }

    fun deleteEpisode(id: Int) {
        episodes.postValue(episodes.value?.filter { episode ->
            episode.id != id
        }?.toTypedArray())
    }
}