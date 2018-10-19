package com.gnz.locamat.feature.atmlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList

import com.gnz.locamat.R
import com.gnz.locamat.data.*
import com.gnz.locamat.extensions.observe
import com.gnz.locamat.feature.atmlist.adapter.ATMPagedAdapter
import com.gnz.locamat.feature.atmlist.adapter.OnClickListener
import kotlinx.android.synthetic.main.fragment_atm_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AtmListFragment : Fragment(), OnClickListener {

    companion object {
        const val TAG = "AtmListFragment"
        fun newInstance() = AtmListFragment()
    }

    private lateinit var atmAdapter: ATMPagedAdapter

    val atmViewModel by viewModel<ATMListViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atm_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    private fun initViews() {
        atmAdapter = ATMPagedAdapter()
        atmRecyclerView.adapter = atmAdapter
    }

    private fun initData() {
        with(atmViewModel) {
            observe(observeAtms(), ::setPagedList)
            observe(observeResultState(), ::showState)
        }
    }

    private fun setPagedList(atmList: PagedList<DisATM>) {
        atmAdapter.submitList(atmList)
    }

    private fun showState(resourceState: ResourceState) = when (resourceState) {
        is Loading -> showLoadingState(true)
        is PopulateState -> showLoadingState(false)
        is EmptyState -> showLoadingState(false)
        is ErrorState -> showErrorState()
    }

    private fun showLoadingState(showLoading: Boolean) {
        progressBar.visibility = if (showLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showErrorState() {

    }

    override fun click(disATM: DisATM) {
        atmViewModel.onClick(disATM)
    }
}
