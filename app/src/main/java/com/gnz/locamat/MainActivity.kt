package com.gnz.locamat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.gnz.locamat.feature.atmlist.AtmListFragment
import com.gnz.locamat.feature.map.MapFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val atmFragment by lazy { AtmListFragment.newInstance() }
    val mapFragment by lazy { MapFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeBottomNavigationView()
        replaceFragmentInMainContainer(AtmListFragment.newInstance())
    }

    private fun initializeBottomNavigationView() {
        // Workaround to prevent selecting wrong menu option when activity was killed in background
        mainNavigationView.isSaveEnabled = false
        mainNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.atm_list -> replaceFragmentInMainContainer(atmFragment)
                R.id.map -> replaceFragmentInMainContainer(mapFragment)
                else -> throw IllegalStateException("No action specified for item: $it")
            }
        }
    }

    private fun replaceFragmentInMainContainer(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, fragment)
                .commit()

        return true
    }
}
