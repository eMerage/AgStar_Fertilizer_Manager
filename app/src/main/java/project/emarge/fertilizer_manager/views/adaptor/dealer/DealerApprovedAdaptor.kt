package project.emarge.fertilizer_manager.views.adaptor.dealer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.listview_approved_dealers.view.*
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.model.datamodel.Dealer

class DealerApprovedAdaptor(val items: ArrayList<Dealer>, val context: Context) :
    RecyclerView.Adapter<DealerApprovedAdaptor.ViewHolderDealerApproved>() {
    lateinit var mClickListener: ClickListener

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDealerApproved {
        return ViewHolderDealerApproved(
            LayoutInflater.from(context).inflate(R.layout.listview_approved_dealers, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolderDealerApproved, position: Int) {
        holder.bindView(position)
    }

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }



    interface ClickListener {
        fun onClick(dealer: Dealer, aView: View)
    }


    inner class ViewHolderDealerApproved(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback, View.OnClickListener {


        val textviewCode = view.textview_dealercode
        val textviewNumber = view.textview_dealernumber
        val textviewName = view.textview_dealername
        var mapView = view.mapView
        private lateinit var latLng: LatLng
        lateinit var mapCurrent: GoogleMap


        override fun onClick(p0: View?) {
            mClickListener.onClick( items[adapterPosition], p0!!)
        }


        init {
            with(mapView) {
                onCreate(null)
                onResume()
                getMapAsync(this@ViewHolderDealerApproved)
            }

            view.setOnClickListener(this)
        }

        fun bindView(position: Int) {
            items[position].let {
                latLng = it.dealerLocationLan?.let { it1 -> it.dealerLocationLon?.let { it2 -> LatLng(it1, it2) } }!!
                textviewCode?.text = it.dealerCode
                textviewNumber?.text = it.dealerContactNumber.toString()
                textviewName?.text = it.dealerName
                setMapLocation()
            }
        }
        private fun setMapLocation() {
            if (!::mapCurrent.isInitialized) return
            with(mapCurrent) {
                uiSettings.isMyLocationButtonEnabled = true
                setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map))
                addMarker(MarkerOptions().position(latLng).title(""))
                moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                setOnMapClickListener {

                }
            }
        }


        override fun onMapReady(p0: GoogleMap?) {
            MapsInitializer.initialize(context)
            mapCurrent = p0!!
            setMapLocation()
        }
    }
}

