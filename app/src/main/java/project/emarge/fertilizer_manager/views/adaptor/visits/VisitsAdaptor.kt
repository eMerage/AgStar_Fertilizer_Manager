package project.emarge.fertilizer_manager.views.adaptor.visits

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listview_visits.view.*
import project.emarge.agstar.views.adaptor.visits.VisitsPurposeAdaptor
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.model.datamodel.Products
import project.emarge.fertilizer_manager.model.datamodel.Purpose
import project.emarge.fertilizer_manager.model.datamodel.Visits

class VisitsAdaptor(val items: ArrayList<Visits>, val context: Context) :
    RecyclerView.Adapter<VisitsAdaptor.ViewHolderVisits>() {


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderVisits {
        return ViewHolderVisits(LayoutInflater.from(context).inflate(R.layout.listview_visits, parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolderVisits, position: Int) {
        var itemPostion = items[position]
        holder?.textviewDate?.text = itemPostion.visitsDate?.substring(0, 10)
        holder?.textviewVisitcode?.text = itemPostion.visitsCode
        holder?.textviewDealer?.text = (itemPostion.visitsDealerName + " - " + itemPostion.visitsDealerCode)
        holder?.textviewRep?.text = itemPostion.visitsRep.name
        holder?.textviewDealerCode?.text = itemPostion.visitsDealerCode
        holder?.textviewTime?.text = itemPostion.visitsDate?.substring(11)




        if (itemPostion.visitsOrder.orderCode == null) {

            holder?.relativeLayoutOrder.visibility = View.GONE
            holder?.relativeLayoutVisitsPoducts.visibility = View.GONE

            var listEmptyPro = ArrayList<Products>()
            setVisitsProducts(context, holder, listEmptyPro)



        } else {
            holder?.relativeLayoutVisitsPoducts.visibility = View.VISIBLE
            holder?.relativeLayoutOrder.visibility = View.VISIBLE
            holder?.textviewOrdercode?.text = itemPostion.visitsOrder.orderCode
            holder?.textviewDispatchDate?.text = itemPostion.visitsOrder.orderDispatchDate?.substring(0, 10)
            holder?.textviewDispatchtype?.text = itemPostion.visitsOrder.orderDispatchType
            holder?.textviewPaymentType?.text = itemPostion.visitsOrder.orderPaymentType


            if (itemPostion.visitsOrder.isOrderConfirmed) {
                holder?.textviewOrderstatus.text = "Confirmed"
            } else {
                holder?.textviewOrderstatus.text = "Not Confirmed"
            }




            setVisitsProducts(context, holder, itemPostion.visitsOrder.productsList!!)
        }

        setVisitsPorpus(context, holder, itemPostion.visitsPurpose!!)
    }


    fun setVisitsProducts(context: Context, holder: ViewHolderVisits, itemsPro: ArrayList<Products>) {
        holder?.recyclerviewProducts?.adapter = VisitsProductsAdaptor(itemsPro, context)
    }

    fun setVisitsPorpus(context: Context, holder: ViewHolderVisits, itemsPro: ArrayList<Purpose>) {
        holder?.recyclerviewVisitsPorpus?.adapter = VisitsPurposeAdaptor(itemsPro, context)
    }


    inner class ViewHolderVisits(view: View) : RecyclerView.ViewHolder(view) {
        val textviewDate = view.textview_date
        val textviewVisitcode = view.textview_visitcode
        val textviewDealer = view.textview_dealer
        val textviewRep = view.textview_rep
        val recyclerviewProducts = view.recyclerview_products

        val relativeLayoutVisitsPoducts = view.relativeLayout_visits_poducts
        val relativeLayoutOrder = view.relativeLayout_order
        val textviewOrdercode = view.textview_ordercode
        val textviewDispatchDate = view.textview_dispatchdate
        val textviewDispatchtype = view.textview_dispatchtype
        val textviewPaymentType = view.textview_paymenttype
        val textviewDealerCode = view.textview_dealer_code
        val textviewTime = view.textview_time

        val textviewOrderstatus = view.textview_orderstatus







        val recyclerviewVisitsPorpus = view.recyclerview_visits_porpus


    }
}

