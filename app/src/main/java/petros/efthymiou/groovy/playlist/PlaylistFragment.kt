package petros.efthymiou.groovy.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import petros.efthymiou.groovy.databinding.FragmentPlaylistBinding
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlaylistViewModel

    @Inject
    lateinit var viewModelFactory: PlaylistViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlaylistViewModel::class.java)

        observeLoader()
        observePlaylist()

        return binding.root
    }

    private fun observeLoader() {
        viewModel.loader.observe(this as LifecycleOwner) { loading ->
            when (loading) {
                true -> binding.loader.visibility = View.VISIBLE
                else -> binding.loader.visibility = View.GONE
            }
        }
    }

    private fun observePlaylist() {
        viewModel.playlists.observe(this as LifecycleOwner) { playlists ->
            if (playlists.getOrNull() != null)
                setupList(playlists.getOrNull()!!)
            else {
                //TODO
            }
        }
    }


    private fun setupList(playlists: List<Playlist>) {
        with(binding.playlistsList) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyPlaylistRecyclerViewAdapter(playlists) { playlistId ->
                val action =
                    PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistDetailFragment(
                        playlistId
                    )
                findNavController().navigate(action)
            }
        }
    }
}