package com.example.playah

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EpisodesViewModel : ViewModel() {
    val episodes: MutableLiveData<Array<Episode>> by lazy {
        MutableLiveData<Array<Episode>>()
    }
}