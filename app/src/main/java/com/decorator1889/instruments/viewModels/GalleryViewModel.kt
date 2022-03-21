package com.decorator1889.instruments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decorator1889.instruments.models.Instruments

class GalleryViewModel : ViewModel() {

    private val _galleryImageList = MutableLiveData<List<Instruments>>()
    val galleryImageList: LiveData<List<Instruments>> = _galleryImageList

    fun setInstrumentsGalleryList(instrumentsList: List<Instruments>) {
        _galleryImageList.value = instrumentsList
    }
}