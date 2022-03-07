package com.decorator1889.instruments.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.LocalDb.RealmDb.realm
import com.decorator1889.instruments.Models.InstrumentModel
import com.decorator1889.instruments.R
import com.jsibbold.zoomage.ZoomageView
import com.squareup.picasso.Picasso
import io.realm.*
import java.util.*


internal class InstrumentAdapter(data: OrderedRealmCollection<InstrumentModel?>?, type: String) :
    RealmRecyclerViewAdapter<InstrumentModel?, InstrumentAdapter.MyViewHolder?>(data, true), Filterable {
    private var typeAdapter: String = ""
init {
    typeAdapter = type
    setHasStableIds(true)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_company, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val obj = getItem(position)
        holder.data = obj
        holder.title.text = obj!!.instrument_name.capitalize(Locale.ROOT)
        holder.title2.text = obj.description
        val mPicasso: Picasso? = Picasso.get()
        mPicasso?.setIndicatorsEnabled(false)
        mPicasso
            ?.load("http://ovz2.a-decorator1889.noklm.vps.myjino.ru/instruments/admin-company/upload/" + obj.image)
            ?.into(holder.imageLogo)

    }

    override fun getItemId(index: Int): Long {
        return getItem(index)!!.id.toLong()
    }

    internal class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.tvName)
        var title2: TextView = view.findViewById(R.id.tvSize)
        var imageLogo: ZoomageView = view.findViewById(R.id.ivIcon)
        var data: InstrumentModel? = null

    }


    override fun getFilter(): Filter {
return MyFilter(this)
    }

    private class MyFilter(var adapter: InstrumentAdapter) : Filter() {

        override fun performFiltering(p0: CharSequence?): FilterResults {
            return FilterResults()
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            adapter.filterResults(p0.toString())

        }
    }
     fun filterResults(text: String) {
         println(typeAdapter)
         if (text.isEmpty() || "" == text) {
             updateData(realm.where(InstrumentModel::class.java).equalTo("type", typeAdapter)
                 .sort("instrument_name").findAll())
         }  else {
             updateData(realm.where(InstrumentModel::class.java).equalTo("type", typeAdapter)
                 .contains("instrument_name", text.toLowerCase()).sort("instrument_name").findAll())
         }

     }
}


