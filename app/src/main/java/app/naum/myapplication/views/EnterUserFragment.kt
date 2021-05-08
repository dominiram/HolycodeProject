package app.naum.myapplication.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import app.naum.myapplication.MainActivity
import app.naum.myapplication.R
import app.naum.myapplication.databinding.FragmentEnterUserBinding
import app.naum.myapplication.utils.DataState
import app.naum.myapplication.viewmodels.EnterUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterUserFragment : Fragment() {

    private val TAG = "EnterUserFragment"
    private val viewModel: EnterUserViewModel by viewModels()
    private var _binding: FragmentEnterUserBinding? = null
    private val binding get() = _binding!!
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        subscribeToObservables()
        binding.btnSearchUser.setOnClickListener{
            viewModel.getSearchUserInfo(binding.etUsername.text.toString())
//            viewModel.getOctocatInfo()
        }
    }

    private fun subscribeToObservables() {
        viewModel.userState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Error -> {
                    (activity as MainActivity).hideProgressBar()
                    Log.d(TAG, "subscribeToObservables: Error!")
                    it.exception.printStackTrace()
                    Toast.makeText(
                        context,
                        resources.getString(R.string.error_get_user_info),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is DataState.Loading -> {
                    (activity as MainActivity).showProgressBar()
                    Log.d(TAG, "subscribeToObservables: Loading...")
                }
                is DataState.Success -> {
                    (activity as MainActivity).hideProgressBar()
                    Log.d(TAG, "subscribeToObservables: user = ${it.data}")
                    val direction: NavDirections =
                        EnterUserFragmentDirections
                            .actionEnterUserFragmentToUserFragment()
                    navController.navigate(direction)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
