package com.gnz.locamat.feature.atmlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gnz.locamat.R
import com.gnz.locamat.common.DistanceUtil
import com.gnz.locamat.data.DisATM
import kotlinx.android.synthetic.main.atm_viewholder.view.*


class ATMPagedAdapter : PagedListAdapter<DisATM, ATMViewHolder>(ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ATMViewHolder = ATMViewHolder(parent)

    override fun onBindViewHolder(holder: ATMViewHolder, position: Int) {
        getItem(position)?.let { atm ->
            holder.atmAddress.text = atm.address
            holder.atmName.text = atm.name
            holder.atmDistance.text = getFormattedDistance(atm.distance, holder)
        }
    }

    private fun getFormattedDistance(distance: Float, holder: ATMViewHolder): String = with(DistanceUtil.formatDistance(distance)) {
        when (distanceUnit) {
            DistanceUtil.DistanceUnit.M -> holder.itemView.context.getString(R.string.distance_meters, distance)
            else -> holder.itemView.context.getString(R.string.distance_kilometers, distance)
        }
    }

    private object ItemCallback : DiffUtil.ItemCallback<DisATM>() {

        override fun areContentsTheSame(oldItem: DisATM, newItem: DisATM): Boolean =
                oldItem == newItem

        override fun areItemsTheSame(oldItem: DisATM, newItem: DisATM): Boolean =
                oldItem.id != newItem.id
    }
}

class ATMViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.atm_viewholder, parent, false)) {
    val atmName = itemView.atmNameTextView
    val atmDistance = itemView.atmDistanceTextView
    val atmAddress = itemView.atmAddressTextView
}