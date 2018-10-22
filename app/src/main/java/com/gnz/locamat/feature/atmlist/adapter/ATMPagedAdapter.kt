package com.gnz.locamat.feature.atmlist.adapter

import android.arch.paging.PagedListAdapter
import android.location.Location
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gnz.locamat.R
import com.gnz.locamat.common.DistanceUtil
import com.gnz.locamat.data.LocATM
import com.gnz.locamat.data.getLocation
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.atm_viewholder.view.*
import kotlin.properties.Delegates


class ATMPagedAdapter : PagedListAdapter<LocATM, ATMViewHolder>(ItemCallback) {

    var clickListener: OnClickListener? = null
    var currentLocation: Location by Delegates.observable(DEFAULT_LOCATION) { _, _, new ->
        if (new != DEFAULT_LOCATION) {
            locationSubject.onNext(new)
        }
    }
    private val locationSubject = BehaviorSubject.create<Location>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ATMViewHolder = ATMViewHolder(parent)

    override fun onBindViewHolder(holder: ATMViewHolder, position: Int) {
        getItem(position)?.let { atm ->
            holder.atmAddress.text = atm.formatted
            holder.atmName.text = atm.name
            holder.itemView.setOnClickListener {
                clickListener?.click(atm)
            }
            holder.bind(locationSubject, atm.getLocation())
        }
    }

    override fun onViewRecycled(holder: ATMViewHolder) {
        super.onViewRecycled(holder)
        holder.unBind()
    }

    private object ItemCallback : DiffUtil.ItemCallback<LocATM>() {

        override fun areContentsTheSame(oldItem: LocATM, newItem: LocATM): Boolean =
                oldItem == newItem

        override fun areItemsTheSame(oldItem: LocATM, newItem: LocATM): Boolean =
                oldItem.id != newItem.id
    }

    companion object {
        private val DEFAULT_LOCATION = Location("DEFAULT")
    }
}

class ATMViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.atm_viewholder, parent, false)) {
    val atmName = itemView.atmNameTextView
    val atmDistance = itemView.atmDistanceTextView
    val atmAddress = itemView.atmAddressTextView

    private var disposable: Disposable? = null

    fun bind(locationObservable: Observable<Location>, atmLocation: Location) {
        disposable = locationObservable
                .subscribe { newLocation ->
                    val distance = DistanceUtil.calculateAndFormatDistance(newLocation, atmLocation)
                    atmDistance.text = getFormattedDistance(distance)
                }
    }

    private fun getFormattedDistance(distanceResult: DistanceUtil.FormattedDistance): String =
            with(distanceResult) {
                when (distanceUnit) {
                    DistanceUtil.DistanceUnit.M -> itemView.context.getString(R.string.distance_meters, distance)
                    else -> itemView.context.getString(R.string.distance_kilometers, this.distance)
                }
            }

    fun unBind() {
        disposable?.dispose()
    }
}

interface OnClickListener {
    fun click(locAtm: LocATM)
}