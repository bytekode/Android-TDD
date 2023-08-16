package petros.efthymiou.groovy.playlist

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import petros.efthymiou.groovy.utils.BaseUnitTest
import petros.efthymiou.groovy.utils.captureValues
import petros.efthymiou.groovy.utils.getValueForTest

class PlaylistViewModelShould : BaseUnitTest() {

    private val repository: PlaylistRepository = mock()
    private val playlists = mock<List<Playlist>>()
    private val expected = Result.success(playlists)
    private val exception = RuntimeException("Something went")

    @Test
    fun getPlaylistsFromRepository() = runTest {
        val viewModel = mockSuccessfulCase()
        viewModel.playlists.getValueForTest()
        verify(repository, times(1)).getPlaylists()
    }

    @Test
    fun emitsPlaylistsFromRepository() = runTest {
        val viewModel = mockSuccessfulCase()
        assertEquals(expected, viewModel.playlists.getValueForTest())
    }

    @Test
    fun emitErrorWhenReceiveError() = runTest {
        val viewModel = mockFailureCase()
        assertEquals(exception, viewModel.playlists.getValueForTest()!!.exceptionOrNull())
    }

    @Test
    fun showSpinnerWhileLoading() = runTest {
        val viewModel = mockSuccessfulCase()
        viewModel.loader.captureValues {
            viewModel.playlists.getValueForTest()
            assertEquals(true, values.first())
        }
    }

    @Test
    fun closeLoaderAfterPlaylistsLoad() = runTest {
        val viewModel = mockSuccessfulCase()
        viewModel.loader.captureValues {
            viewModel.playlists.getValueForTest()
            assertEquals(false, values.last())
        }
    }

    @Test
    fun closeLoaderAfterError() = runTest {
        val viewModel = mockFailureCase()
        viewModel.loader.captureValues {
            viewModel.playlists.getValueForTest()
            assertEquals(false, values.last())
        }
    }

    private fun mockSuccessfulCase(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(flow {
                emit(expected)
            })
        }

        return PlaylistViewModel(repository)
    }

    private fun mockFailureCase(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(flow {
                emit(Result.failure(exception))
            })
        }

        return PlaylistViewModel(repository)
    }
}