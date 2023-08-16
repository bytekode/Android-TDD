package petros.efthymiou.groovy.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val service: PlaylistDetailsService
) : ViewModel() {

    val playlistDetails: MutableLiveData<Result<PlaylistDetails>> = MutableLiveData()
    val loader = MutableLiveData<Boolean>()

    fun getPlaylistDetails(playlistId: String) {
        viewModelScope.launch {
            loader.postValue(true)
            service.fetchPlaylistDetails(playlistId)
                .onEach {
                    loader.postValue(false)
                }
                .collect {
                    playlistDetails.postValue(it)
                }
        }
    }

}
