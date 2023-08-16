package petros.efthymiou.groovy.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import petros.efthymiou.groovy.R
import petros.efthymiou.groovy.databinding.FragmentPlaylistDetailBinding
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistDetailsFragment : Fragment() {

    private lateinit var viewModel: PlaylistDetailsViewModel

    @Inject
    lateinit var viewModelFactory: PlaylistDetailsViewModelFactory

    val args: PlaylistDetailsFragmentArgs by navArgs()

    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        val playlistId = args.playlistId

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(PlaylistDetailsViewModel::class.java)
        viewModel.getPlaylistDetails(playlistId)

        observeLoader()
        observePlaylistDetails()

        return binding.root
    }

    private fun observePlaylistDetails() {
        viewModel.playlistDetails.observe(this as LifecycleOwner) { playlistDetails ->
            if (playlistDetails.getOrNull() != null) {
                setupUI(playlistDetails)
            } else {
                Snackbar.make(binding.root, R.string.generic_error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun observeLoader() {
        viewModel.loader.observe(this as LifecycleOwner) { loading ->
            when (loading) {
                true -> binding.detailsLoader.visibility = View.VISIBLE
                else -> binding.detailsLoader.visibility = View.GONE
            }
        }
    }

    private fun setupUI(playlistDetails: Result<PlaylistDetails>) {
        binding.playlistName.text = playlistDetails.getOrNull()!!.name
        binding.playlistDetails.text = playlistDetails.getOrNull()!!.details
    }

}