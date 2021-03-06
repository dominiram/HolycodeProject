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
import app.naum.myapplication.databinding.CommentsItemBinding
import app.naum.myapplication.databinding.CommitItemBinding
import app.naum.myapplication.databinding.FragmentCommitDetailsBinding
import app.naum.myapplication.networking.entities.CommentNetworkEntity
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
    private var comments: MutableMap<Int, List<CommentNetworkEntity>?> = mutableMapOf()

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
                    it.data
                    comments = viewModel.commentsList
                    Log.d(TAG, "subscribeToObservables: comments = $comments")
                    populateList(it.data, comments)
                    (activity as MainActivity).hideProgressBar()
                }
            }
        })
    }

    private fun populateList(commits: List<CommitDetailsNetworkEntity>,
                             comments: MutableMap<Int, List<CommentNetworkEntity>?>) {
        Log.d(TAG, "populateList: comments = $comments")

        binding.commitList.layoutManager = LinearLayoutManager(context)
        binding.commitList.adapter = CommitListAdapter(commits, comments)
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
        private val commitList: List<CommitDetailsNetworkEntity>,
        private val comments: MutableMap<Int, List<CommentNetworkEntity>?>
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
            CommitItemBinding.bind(holder.itemView).apply {
                commitAuthor.text = commitList[position].commit.author.name
                committer.text = commitList[position].commit.committer.name
                commitMessage.text = commitList[position].commit.commitMessage
                commitDate.text = commitList[position].commit.committer.date

                Log.d(TAG, "onBindViewHolder: commentsList = $comments")
                commentsList.layoutManager = LinearLayoutManager(context)
                commentsList.adapter = comments[position]?.let {
                    CommentsAdapter(
                        it
                    )
                }
                commentsList.addItemDecoration(
                    DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                )
            }

        }

        override fun getItemCount(): Int = commitList.size

    }

    inner class CommentsAdapter(
        private val commentsList: List<CommentNetworkEntity>
    ) : RecyclerView.Adapter<CommentsAdapter.CommentListViewHolder>() {

        inner class CommentListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListViewHolder {
            return CommentListViewHolder(
                CommentsItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                    .root
            )
        }

        override fun onBindViewHolder(holder: CommentListViewHolder, position: Int) {
            CommentsItemBinding.bind(holder.itemView).apply {
                Log.d(TAG, "onBindViewHolder: author = ${commentsList[position].user.name}")
                Log.d(TAG, "onBindViewHolder: message = ${commentsList[position].message}")
                commentAuthor.text = commentsList[position].user.name
                commentMessage.text = commentsList[position].message
            }
        }

        override fun getItemCount(): Int = commentsList.size
    }
}
