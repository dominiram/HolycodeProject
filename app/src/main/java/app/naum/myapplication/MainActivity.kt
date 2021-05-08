package app.naum.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import app.naum.myapplication.databinding.ActivityMainBinding
import app.naum.myapplication.views.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var currentFragment: BaseFragment
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun showProgressBar() {
        binding.progressBar.root.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        binding.progressBar.root.visibility = View.GONE
    }

    fun setCurrentFrag() {
        currentFragment =
            (supportFragmentManager
                .findFragmentById(R.id.hostFragment)
                ?.childFragmentManager
                ?.findFragmentById(R.id.hostFragment) as BaseFragment)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        Log.d(TAG, "onBackPressed: currentFrag = $currentFragment")
        currentFragment.handleOnBackPressed()
    }
}