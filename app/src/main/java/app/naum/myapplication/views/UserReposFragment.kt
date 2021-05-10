package app.naum.myapplication.views

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        binding.repoList.layoutManager = LinearLayoutManager(context)
        binding.repoList.adapter = RepoListAdapter(userRepos)
        binding.repoList.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }

    override fun handleOnBackPressed() {
        navController.navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    inner class RepoListAdapter(
        private val repoList: List<UserRepoNetworkEntity>
    ): RecyclerView.Adapter<RepoListAdapter.RepoListViewHolder>() {

        inner class RepoListViewHolder(view: View): RecyclerView.ViewHolder(view){
            val repoName: TextView = view.findViewById(R.id.userRepoName)
            val repoUrl: TextView = view.findViewById(R.id.userRepoUrl)
            val repoDescription: TextView = view.findViewById(R.id.userRepoDescription)
            val openIssues: TextView = view.findViewById(R.id.openIssues)
            val btnCommitDetails: Button = view.findViewById(R.id.btnCommitDetails)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_repo_item, parent, false)
            return RepoListViewHolder(view)
        }

        override fun onBindViewHolder(holder: RepoListViewHolder, position: Int) {
            holder.repoName.text = repoList[position].name
            holder.repoUrl.text = repoList[position].htmlUrl
            holder.repoDescription.text = repoList[position].description
            holder.openIssues.text = repoList[position].issueCount.toString()
            holder.btnCommitDetails.setOnClickListener {
                val direction: NavDirections = UserReposFragmentDirections
                    .actionUserReposFragmentToCommitDetailsFragment()
                navController.navigate(direction)
            }
        }

        override fun getItemCount(): Int = repoList.size
    }
}
