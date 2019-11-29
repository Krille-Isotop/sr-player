package com.example.playah

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EpisodesViewModel : ViewModel() {
    val episodes: MutableLiveData<Array<Episode>> by lazy {
        MutableLiveData<Array<Episode>>()
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