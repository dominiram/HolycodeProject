package app.naum.myapplication.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import app.naum.myapplication.databinding.FragmentCommitDetailsBinding
import app.naum.myapplication.databinding.FragmentUserReposBinding
import app.naum.myapplication.viewmodels.UserReposViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommitDetailsFragment: BaseFragment() {

    private val TAG = "CommitDetailsFragment"
    lateinit var navController: NavController
    private var _binding: FragmentCommitDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommitDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    override fun handleOnBackPressed() {
        navController.navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
