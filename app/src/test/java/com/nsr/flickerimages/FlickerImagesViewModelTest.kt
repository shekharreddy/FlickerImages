package com.nsr.flickerimages

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.nsr.flickerimages.model.FlickerSearchResponse
import com.nsr.flickerimages.model.Item
import com.nsr.flickerimages.model.Media
import com.nsr.flickerimages.network.ApiHelper
import com.nsr.flickerimages.network.ResponseResource
import com.nsr.flickerimages.network.Status
import com.nsr.flickerimages.viewmodel.FlickerImagesViewModel
import com.nsr.flickerimages.viewmodel.FlickerUIItem
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FlickerImagesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var apiHelper: ApiHelper
    private lateinit var viewModel: FlickerImagesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        apiHelper = mockk()
        viewModel = FlickerImagesViewModel(apiHelper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchFlickerImages success`() = runTest {
        val searchKey = "trees"
        val valuesCaptured: CapturingSlot<ResponseResource<FlickerSearchResponse>> = slot() // TODO capture values and assert

        coEvery { apiHelper.getFlickerImages(searchKey) } returns apiResponse
        val observer = mockk<Observer<ResponseResource<List<FlickerUIItem>>>>(relaxUnitFun = true)
        viewModel.fetchFlickerImages(searchKey)
        viewModel.flickerImagesList.asLiveData().observeForever(observer)

        verifySequence {
            observer.onChanged(loadingState)
            observer.onChanged(successState)
        }
    }

//    @Test
//    fun `fetchFlickerImages error`() = runTest {
//        val searchKey = "trees"
//
//        coEvery { apiHelper.getFlickerImages(searchKey) } throws IOException("Mocked error")
//        val observer = mockk<Observer<ResponseResource<List<FlickerUIItem>>>>(relaxUnitFun = true)
//        viewModel.fetchFlickerImages(searchKey)
//        viewModel.flickerImagesList.asLiveData().observeForever(observer)
//
//        verifySequence {
//            observer.onChanged(loadingState)
//            observer.onChanged(errorState)
//        }
//    }

    private companion object {
        //UI related model data
        val weatherItem = FlickerUIItem(
            title = "Cincinnati Zoo 2-14-24-01623",
            description = "Rain - light rain",
            author = "nobody@flickr.com (joemastrullo)",
            imageURL = "https://live.staticflickr.com/65535/53529914873_f11e77c6a7_m.jpg",
            publishedDate = "02-14-2024",
            widthHeight = Pair(160, 120)
        )

        val apiResponse = FlickerSearchResponse(
            title  ="Recent Uploads tagged porcupine",
            link = "https://www.flickr.com/photos/tags/porcupine/",
            description = "",
            modified = "2024-02-14T23:36:51Z",
            generator = "https://www.flickr.com",
            items = listOf(
                Item(
                    title = "Cincinnati Zoo 2-14-24-01623",
                    link =  "https://www.flickr.com/photos/djjamphoto/53529914873/",
                    media =  Media(m = "https://live.staticflickr.com/65535/53529914873_f11e77c6a7_m.jpg"),
                    date_taken = "2024-02-14T14:09:49-08:00",
                    description = "Rain - light rain",
                    published = "2024-02-14T23:36:51Z",
                    author = "nobody@flickr.com (joemastrullo)",
                    author_id = "124191108@N08",
                    tags = "cincinnati zoo botanical garden porcupine rico"
                )
            )
        )

        val errorState = ResponseResource(status= Status.ERROR, data=null, message = R.string.error_message)
        val loadingState = ResponseResource(status = Status.LOADING, data = null, message = R.string.error_message)
        val successState = ResponseResource(status = Status.SUCCESS, data = listOf(weatherItem), message = R.string.error_message)
    }
}
