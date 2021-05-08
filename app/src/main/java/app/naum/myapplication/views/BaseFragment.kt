package app.naum.myapplication.views

import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
    abstract fun handleOnBackPressed()
}
