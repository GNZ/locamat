package com.gnz.locamat.feature.atmlist


import android.Manifest
import android.arch.paging.PagedList
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gnz.locamat.R
import com.gnz.locamat.data.*
import com.gnz.locamat.extensions.observe
import com.gnz.locamat.feature.atmlist.adapter.ATMPagedAdapter
import com.gnz.locamat.feature.atmlist.adapter.OnClickListener
import com.google.android.gms.location.LocationRequest
import kotlinx.android.synthetic.main.fragment_atm_list.*
import org.koin.android.viewmodel.ext.android.viewModel


class AtmListFragment : Fragment(), OnClickListener {

    companion object {
        const val TAG = "AtmListFragment"
        private val REQUEST_LOCATION = 1011
        fun newInstance() = AtmListFragment()

        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
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
        requestPermission()
        lifecycle.addObserver(atmViewModel)
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
        is NoLocationGranted -> noLocationGranted()
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

    private fun noLocationGranted() {

    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        } else {
            atmViewModel.startListeningLocation(locationRequest)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    atmViewModel.startListeningLocation(locationRequest)
                }
                return
            }
        }
    }

    override fun click(disATM: DisATM) {
        atmViewModel.onClick(disATM)
    }
}