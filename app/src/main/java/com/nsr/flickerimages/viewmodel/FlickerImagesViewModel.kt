package com.nsr.flickerimages.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsr.flickerimages.R
import com.nsr.flickerimages.model.FlickerSearchResponse
import com.nsr.flickerimages.network.ApiHelper
import com.nsr.flickerimages.network.ResponseResource
import com.nsr.flickerimages.utils.extractWidthAndHeight
import com.nsr.flickerimages.utils.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

// View model responsible for fetching Flicker images for searched string

@HiltViewModel
class FlickerImagesViewModel @Inject constructor(private val apiHelper: ApiHelper): ViewModel() {
    private var currentJob: Job? = null
    private val _flickerImagesList = MutableStateFlow<ResponseResource<List<FlickerUIItem>>>(
        ResponseResource.init())
    val flickerImagesList: MutableStateFlow<ResponseResource<List<FlickerUIItem>>> = _flickerImagesList

    private var selectedItem: FlickerUIItem? = null

    fun fetchFlickerImages(searchKey: String) {
        currentJob?.cancel()
        _flickerImagesList.value = ResponseResource.loading()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = apiHelper.getFlickerImages(searchKey)
                    processResponse(response)
                } catch (e: Exception) {
                    _flickerImagesList.value = ResponseResource.error(R.string.error_message)
                }
            }
        }
    }

    fun getCurrentSelectedItem(): FlickerUIItem? = selectedItem

    fun setCurrentSelectedItem(currentItem: FlickerUIItem) {
        selectedItem = currentItem
    }

    private fun processResponse(res: FlickerSearchResponse){
        val list = mutableListOf<FlickerUIItem>()
        res.items.forEach {
            list.add(
                FlickerUIItem(
                    imageURL = it.media.m,
                    title = it.title,
                    description = it.description,
                    author = it.author,
                    publishedDate = formatDate(it.date_taken),
                    widthHeight = extractWidthAndHeight(it.description)
                )
            )
        }.also {
            _flickerImagesList.value = ResponseResource.success(list)
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
        selectedItem = null
    }
}

data class FlickerUIItem(
    val imageURL: String? = null,
    val title: String? = null,
    val description: String? = null,
    val author: String? = null,
    val publishedDate: String? = null,
    val widthHeight: Pair<Int, Int>
)