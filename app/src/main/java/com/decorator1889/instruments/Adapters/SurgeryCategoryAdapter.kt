package com.decorator1889.instruments.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.Models.CategoryModel
import com.decorator1889.instruments.R

class SurgeryCategoryAdapter(private val companies: ArrayList<CategoryModel>, private var listener: Clicker, var context: Context): RecyclerView.Adapter<SurgeryCategoryAdapter.ViewHolder>(){


    interface Clicker{
        fun onClick(company: CategoryModel)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.list_category_surgery, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  companies.size
    }
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.insertAddons(companies[p1], listener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun insertAddons(category: CategoryModel, listener: Clicker){
            val cardView = itemView.findViewById(R.id.cardViewCategory) as CardView
            val nameCategory = itemView.findViewById(R.id.tvNameCategory) as TextView
            val imageLogo = itemView.findViewById(R.id.ivIconCategory) as ImageView
            imageLogo.setImageResource(category.image)
            nameCategory.text = category.name
            cardView.setOnClickListener {listener.onClick(category)}
        }
    }

}