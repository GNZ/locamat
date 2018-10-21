package com.gnz.locamat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gnz.locamat.feature.atmlist.AtmListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, AtmListFragment.newInstance(), AtmListFragment.TAG)
                .commit()
    }
}
