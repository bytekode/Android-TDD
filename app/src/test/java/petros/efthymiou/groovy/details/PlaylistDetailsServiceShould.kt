package petros.efthymiou.groovy.details

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlaylistDetailsServiceShould : BaseUnitTest() {

    private val exception = RuntimeException("Damn backend developer")
    private val api: PlaylistDetailsAPI = mock()
    private val playlistDetails: PlaylistDetails = mock()
    private val id = "1"

    private lateinit var service: PlaylistDetailsService

    @Test
    fun fetchPlaylistDetailsFromAPI() = runTest {
        mockSuccessfulCase()
        service.fetchPlaylistDetails(id).single()
        verify(api, times(1)).fetchPlaylistDetails(id)
    }

    @Test
    fun convertValuesToFlowResultAndEmitThem() = runTest {
        mockSuccessfulCase()
        assertEquals(Result.success(playlistDetails), service.fetchPlaylistDetails(id).single())
    }

    @Test
    fun emitErrorResultWhenNetworkFails() = runTest {
        mockErrorCase()

        assertEquals(
            "Something went wrong",
            service.fetchPlaylistDetails(id).single().exceptionOrNull()?.message
        )
    }

    private suspend fun mockErrorCase() {
        whenever(api.fetchPlaylistDetails(id)).thenThrow(exception)
        service = PlaylistDetailsService(api)
    }

    private suspend fun mockSuccessfulCase() {
        whenever(api.fetchPlaylistDetails(id)).thenReturn(playlistDetails)
        service = PlaylistDetailsService(api)
    }

}