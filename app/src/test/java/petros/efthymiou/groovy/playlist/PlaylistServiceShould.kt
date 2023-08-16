package petros.efthymiou.groovy.playlist

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlaylistServiceShould : BaseUnitTest() {

    private val playlists: List<PlaylistRaw> = mock()
    private val api: PlaylistAPI = mock()

    private lateinit var service: PlaylistService

    @Test
    fun fetchPlaylistsFromAPI() = runTest {
        service = PlaylistService(api)
        service.fetchPlaylists().first()
        verify(api, times(1)).fetchAllPlaylists()
    }

    @Test
    fun convertValuesForFlowResultAndEmitsThem() = runTest {
        mockSuccessfulCase()
        assertEquals(Result.success(playlists), service.fetchPlaylists().first())
    }

    private suspend fun mockSuccessfulCase() {
        whenever(api.fetchAllPlaylists()).thenReturn(playlists)
        service = PlaylistService(api)
    }

    @Test
    fun emitsErrorResultWhenNetworkFails() = runTest {
        mockFailureCase()
        assertEquals(
            "Something went wrong",
            service.fetchPlaylists().first().exceptionOrNull()?.message
        )
    }

    private suspend fun mockFailureCase() {
        whenever(api.fetchAllPlaylists()).thenThrow(RuntimeException("Damn backend developer"))
        service = PlaylistService(api)
    }

}