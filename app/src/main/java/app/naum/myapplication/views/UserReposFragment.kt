package app.naum.myapplication.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.naum.myapplication.MainActivity
import app.naum.myapplication.R
import app.naum.myapplication.databinding.FragmentUserReposBinding
import app.naum.myapplication.networking.entities.UserRepoNetworkEntity
import app.naum.myapplication.utils.DataState
import app.naum.myapplication.viewmodels.UserReposViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserReposFragment: BaseFragment() {

    private val TAG = "UserReposFragment"
    private val viewModel: UserReposViewModel by viewModels()
    lateinit var navController: NavController
    private var _binding: FragmentUserReposBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserReposBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).setCurrentFrag()
        navController = findNavController()
        subscribeToObservables()
        val args: UserReposFragmentArgs by navArgs()
        viewModel.getUserRepos(args.user)
    }

    private fun subscribeToObservables() {
        viewModel.userRepos.observe(viewLifecycleOwner, {
            when (it) {
                is DataState.Loading -> (activity as MainActivity).showProgressBar()

                is DataState.Error -> {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.error_get_user_repos),
                        Toast.LENGTH_SHORT
                    ).show()
                    (activity as MainActivity).hideProgressBar()
                }

                is DataState.Success -> {
                    populateList(it.data)
                    (activity as MainActivity).hideProgressBar()
                }
            }
        })
    }

    private fun populateList(userRepos: List<UserRepoNetworkEntity>) {
        for(repo in userRepos)
            Log.d(TAG, "populateList: repo = $repo")
    }

    override fun handleOnBackPressed() {
        navController.navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
