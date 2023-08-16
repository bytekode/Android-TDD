package petros.efthymiou.groovy.playlist

import junit.framework.TestCase
import org.junit.Test
import petros.efthymiou.groovy.R
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlaylistMapperShould : BaseUnitTest() {

    private val playlistRaw = PlaylistRaw("1", "da name", "jazz")
    private val playlistRawRock = PlaylistRaw("1", "da name", "rock")

    private val mapper = PlaylistMapper()

    private val playlist = mapper(listOf(playlistRaw))[0]
    private val playlistRock = mapper(listOf(playlistRawRock))[0]

    @Test
    fun keepSameId() {
        TestCase.assertEquals(playlistRaw.id, playlist.id)
    }

    @Test
    fun keepSameName() {
        TestCase.assertEquals(playlistRaw.name, playlist.name)
    }

    @Test
    fun keepSameCategory() {
        TestCase.assertEquals(playlistRaw.category, playlist.category)
    }

    @Test
    fun mapDefaultImageWhenNotRock() {
        TestCase.assertEquals(R.mipmap.playlist, playlist.image)
    }

    @Test
    fun mapRockImageWhenRockCategory() {
        TestCase.assertEquals(R.mipmap.rock, playlistRock.image)
    }
}