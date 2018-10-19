package com.gnz.locamat.feature.atmlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gnz.locamat.R
import com.gnz.locamat.data.LocATM
import kotlinx.android.synthetic.main.atm_viewholder.view.*


class ATMPagedAdapter(private val mLifecycleOwner: LifecycleOwner) : PagedListAdapter<LocATM, ATMViewHolder>(ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ATMViewHolder = ATMViewHolder(parent)

    override fun onBindViewHolder(holder: ATMViewHolder, position: Int) {
        getItem(position)?.let { locATM ->
            holder.atmAddress.text = locATM.formatted
            holder.atmName.text = locATM.name
        }
    }

    private object ItemCallback : DiffUtil.ItemCallback<LocATM>() {

        override fun areContentsTheSame(oldItem: LocATM, newItem: LocATM): Boolean =
                oldItem == newItem

        override fun areItemsTheSame(oldItem: LocATM, newItem: LocATM): Boolean =
                oldItem.id != newItem.id
    }
}

class ATMViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.atm_viewholder, parent, false)) {
    val atmName = itemView.atmNameTextView
    val atmDistance = itemView.atmDistanceTextView
    val atmAddress = itemView.atmAddressTextView
}