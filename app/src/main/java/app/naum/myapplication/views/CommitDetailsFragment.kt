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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.naum.myapplication.MainActivity
import app.naum.myapplication.R
import app.naum.myapplication.databinding.CommitItemBinding
import app.naum.myapplication.databinding.FragmentCommitDetailsBinding
import app.naum.myapplication.networking.entities.CommitDetailsNetworkEntity
import app.naum.myapplication.utils.DataState
import app.naum.myapplication.viewmodels.CommitDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommitDetailsFragment : BaseFragment() {

    private val TAG = "CommitDetailsFragment"
    lateinit var navController: NavController
    private val viewModel: CommitDetailsViewModel by viewModels()
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
        subscribeToObservables()
        val args: CommitDetailsFragmentArgs by navArgs()
        viewModel.getCommitDetails(args.user, args.repo)
    }

    private fun subscribeToObservables() {
        viewModel.commitsState.observe(viewLifecycleOwner, {
            when (it) {
                is DataState.Error -> {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.error_get_commits),
                        Toast.LENGTH_SHORT
                    ).show()
                    it.exception.printStackTrace()
                    (activity as MainActivity).hideProgressBar()
                }

                is DataState.Loading -> (activity as MainActivity).showProgressBar()

                is DataState.Success -> {
                    populateList(it.data)
                    (activity as MainActivity).hideProgressBar()
                }
            }
        })
    }

    private fun populateList(commits: List<CommitDetailsNetworkEntity>) {
        for (commit in commits)
            Log.d(TAG, "populateList: commit = $commit")
        binding.commitList

        binding.commitList.layoutManager = LinearLayoutManager(context)
        binding.commitList.adapter = CommitListAdapter(commits)
        binding.commitList.addItemDecoration(
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

    inner class CommitListAdapter(
        private val commitList: List<CommitDetailsNetworkEntity>
    ) : RecyclerView.Adapter<CommitListAdapter.CommitListViewHolder>() {

        inner class CommitListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitListViewHolder {
            return CommitListViewHolder(
                CommitItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                    .root
            )
        }

        override fun onBindViewHolder(holder: CommitListViewHolder, position: Int) {
            CommitItemBinding.bind(holder.itemView).apply{
                commitAuthor.text = commitList[position].commit.author.name
                committer.text = commitList[position].commit.committer.name
                commitMessage.text = commitList[position].commit.commitMessage
                commitDate.text = commitList[position].commit.committer.date
                //todo comments list
            }

        }

        override fun getItemCount(): Int = commitList.size

    }
}
