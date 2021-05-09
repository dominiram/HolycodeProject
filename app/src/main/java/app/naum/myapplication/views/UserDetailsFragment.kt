package app.naum.myapplication.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.naum.myapplication.MainActivity
import app.naum.myapplication.databinding.FragmentUserBinding
import app.naum.myapplication.viewmodels.UserReposViewModel
import com.bumptech.glide.Glide

class UserDetailsFragment : BaseFragment() {

    private val TAG = "UserDetailsFragment"
    private val viewModel: UserReposViewModel by viewModels()
    lateinit var navController: NavController
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).setCurrentFrag()
        navController = findNavController()

        val args: UserDetailsFragmentArgs by navArgs()
        Glide
            .with(requireContext())
            .load(args.avatarUrl)
            .into(binding.avatar)

        binding.tvName.text = args.name
        binding.tvCompany.text = args.company

        binding.btnUserRepos.setOnClickListener {
            val args: UserDetailsFragmentArgs by navArgs()
            val direction: NavDirections = UserDetailsFragmentDirections
                .actionUserDetailsFragmentToUserReposFragment(args.user)
            navController.navigate(direction)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun handleOnBackPressed() {
        val direction: NavDirections = UserDetailsFragmentDirections
            .actionUserDetailsFragmentToEnterUserFragment()
        navController.navigate(direction)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
        _binding = null
    }
}
