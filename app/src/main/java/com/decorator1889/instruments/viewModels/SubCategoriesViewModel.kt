package com.decorator1889.instruments.viewModels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.models.Categories
import com.decorator1889.instruments.models.toCategories
import com.decorator1889.instruments.models.toSubCategories
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.enums.State
import kotlinx.coroutines.launch

class SubCategoriesViewModel: ViewModel() {

    private val _subCategoriesList = MutableLiveData<List<Categories>>()
    val subCategoriesList: LiveData<List<Categories>> = _subCategoriesList
    private val _subCategoriesResultEvent = MutableLiveData<NetworkEvent<State>>()
    val subCategoriesResultEvent: LiveData<NetworkEvent<State>> = _subCategoriesResultEvent

    fun loadSubCategories() {
        viewModelScope.launch {
            _subCategoriesResultEvent.value = NetworkEvent(State.LOADING)
            try {
                val response = ApiNetwork.API.getSubCategoriesAsync().await()
                if (response.result == "success") {
                    _subCategoriesList.value = response.subcategory?.toSubCategories()
                    _subCategoriesResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _subCategoriesList.value = listOf()
                    _subCategoriesResultEvent.value = NetworkEvent(State.ERROR, response.error)
                }
            } catch (e: Exception) {
                _subCategoriesList.value = listOf()
                _subCategoriesResultEvent.value = NetworkEvent(State.FAILURE, e.message)
            }
        }
    }
}