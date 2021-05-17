package main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import base.BaseActivity
import com.example.tvmazeschedule.R

class MainActivity : BaseActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController!!.setGraph(R.navigation.nav_graph)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun hideKeyboard() {
        try {
            val inputmanager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputmanager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}
