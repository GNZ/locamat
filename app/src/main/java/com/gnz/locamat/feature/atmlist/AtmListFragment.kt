package com.gnz.locamat.feature.atmlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gnz.locamat.R
import com.gnz.locamat.feature.atmlist.adapter.ATMPagedAdapter
import kotlinx.android.synthetic.main.fragment_atm_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AtmListFragment : Fragment() {

    companion object {
        const val TAG = "AtmListFragment"
        fun newInstance() = AtmListFragment()
    }

    private lateinit var atmAdapter: ATMPagedAdapter

    val reposViewModel by viewModel<ATMListViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atm_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        atmAdapter = ATMPagedAdapter()
        atmRecyclerView.adapter = atmAdapter
    }
}
