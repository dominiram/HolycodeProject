package app.naum.myapplication.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import app.naum.myapplication.MainActivity
import app.naum.myapplication.databinding.FragmentUserReposBinding

class UserReposFragment: BaseFragment() {

    private val TAG = "UserReposFragment"
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
    }

    override fun handleOnBackPressed() {
        navController.navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
