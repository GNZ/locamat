package com.gnz.locamat.feature.atmlist.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gnz.locamat.R
import com.gnz.locamat.common.DistanceUtil
import com.gnz.locamat.data.DisATM
import com.gnz.locamat.data.DistanceResult
import com.gnz.locamat.data.NONE
import com.gnz.locamat.data.Result
import kotlinx.android.synthetic.main.atm_viewholder.view.*


class ATMPagedAdapter : PagedListAdapter<DisATM, ATMViewHolder>(ItemCallback) {

    var clickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ATMViewHolder = ATMViewHolder(parent)

    override fun onBindViewHolder(holder: ATMViewHolder, position: Int) {
        getItem(position)?.let { atm ->
            holder.atmAddress.text = atm.address
            holder.atmName.text = atm.name
            holder.atmDistance.text = getFormattedDistance(atm.distanceResult, holder)
            holder.itemView.setOnClickListener {
                clickListener?.click(atm)
            }
        }
    }

    private fun getFormattedDistance(distanceResult: DistanceResult, holder: ATMViewHolder): String =
            when (distanceResult) {
                is Result -> with(DistanceUtil.formatDistance(distanceResult.distance)) {
                    when (distanceUnit) {
                        DistanceUtil.DistanceUnit.M -> holder.itemView.context.getString(R.string.distance_meters, distance)
                        else -> holder.itemView.context.getString(R.string.distance_kilometers, this.distance)
                    }
                }
                // TODO change this string
                NONE -> ""
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

interface OnClickListener {
    fun click(disATM: DisATM)
}