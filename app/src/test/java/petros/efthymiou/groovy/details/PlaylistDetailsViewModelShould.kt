package petros.efthymiou.groovy.details

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import petros.efthymiou.groovy.utils.BaseUnitTest
import petros.efthymiou.groovy.utils.captureValues
import petros.efthymiou.groovy.utils.getValueForTest

class PlaylistDetailsViewModelShould : BaseUnitTest() {

    private val playlistDetails: PlaylistDetails = mock()
    private val service: PlaylistDetailsService = mock()

    private val expected = Result.success(playlistDetails)
    private val exception = RuntimeException("Something went wrong")
    private val error = Result.failure<PlaylistDetails>(exception)

    lateinit var viewModel: PlaylistDetailsViewModel
    private val id = "1"

    @Test
    fun getPlaylistDetailsFromService() = runTest {
        mockSuccessfulCase()
        viewModel.getPlaylistDetails(id)
        viewModel.playlistDetails.getValueForTest()
        verify(service, times(1)).fetchPlaylistDetails(id)
    }

    @Test
    fun emitsPlaylistDetailsFromService() = runTest {
        mockSuccessfulCase()
        viewModel.getPlaylistDetails(id)
        assertEquals(expected, viewModel.playlistDetails.getValueForTest())
    }

    @Test
    fun emitsErrorWhenReceiveError() = runTest {
        mockErrorCase()
        assertEquals(error, viewModel.playlistDetails.getValueForTest())
    }

    @Test
    fun showSpinnerWhileLoading() = runTest {
        mockSuccessfulCase()
        viewModel.loader.captureValues {
            viewModel.getPlaylistDetails(id)
            viewModel.playlistDetails.getValueForTest()
            assertEquals(true, values.first())
        }
    }

    @Test
    fun closeLoaderAfterPlaylistDetailsLoad() = runTest {
        mockSuccessfulCase()
        viewModel.loader.captureValues {
            viewModel.getPlaylistDetails(id)
            viewModel.playlistDetails.getValueForTest()
            assertEquals(false, values.last())
        }
    }

    private suspend fun mockErrorCase() {
        whenever(service.fetchPlaylistDetails(id)).thenReturn(
            flow {
                emit(error)
            }
        )
        viewModel = PlaylistDetailsViewModel(service)
        viewModel.getPlaylistDetails(id)
    }

    private suspend fun mockSuccessfulCase() {
        whenever(service.fetchPlaylistDetails(id)).thenReturn(
            flow {
                emit(expected)
            }
        )
        viewModel = PlaylistDetailsViewModel(service)
    }

}