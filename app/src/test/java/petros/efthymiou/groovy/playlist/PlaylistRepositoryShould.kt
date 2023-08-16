package petros.efthymiou.groovy.playlist

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import petros.efthymiou.groovy.utils.BaseUnitTest


class PlaylistRepositoryShould : BaseUnitTest() {

    private val mapper: PlaylistMapper = mock()
    private val exception = RuntimeException("Something went wrong")
    private val service: PlaylistService = mock()
    private val playlists = mock<List<Playlist>>()
    private val playlistsRaw = mock<List<PlaylistRaw>>()

    @Test
    fun getMappedPlaylistsFromService() = runTest {
        val repository = mockSuccessfulCase()
        repository.getPlaylists()
        verify(service, times(1)).fetchPlaylists()
    }

    @Test
    fun emitPlaylistsFromService() = runTest {
        val repository = mockSuccessfulCase()
        assertEquals(playlists, repository.getPlaylists().first().getOrNull())
    }

    @Test
    fun propagateErrors() = runTest {
        val repository = mockFailureCase()
        assertEquals(exception, repository.getPlaylists().first().exceptionOrNull())
    }

    @Test
    fun delegateBusinessLogicToMapper() = runTest {
        val repository = mockSuccessfulCase()
        repository.getPlaylists().first()
        verify(mapper, times(1)).invoke(playlistsRaw)
    }

    private suspend fun mockFailureCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.failure(exception))
            }
        )

        return PlaylistRepository(service, mapper)
    }

    private suspend fun mockSuccessfulCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.success(playlistsRaw))
            }
        )
        whenever(mapper.invoke(playlistsRaw)).thenReturn(playlists)
        return PlaylistRepository(service, mapper)
    }

}