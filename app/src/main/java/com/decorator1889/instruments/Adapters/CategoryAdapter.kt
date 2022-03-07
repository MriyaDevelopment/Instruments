package com.decorator1889.instruments.Adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.LocalDb.RealmDbTest
import com.decorator1889.instruments.Models.CategoryModel
import com.decorator1889.instruments.Models.TestModel
import com.decorator1889.instruments.R

class CategoryAdapter(private val companies: ArrayList<CategoryModel>, private var listener: Clicker, var context: Context): RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){

    interface Clicker{
        fun onClick(company: CategoryModel)
        fun onClick1(company: CategoryModel)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.list_category, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  companies.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.insertAddons(companies[p1], listener, context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var highestScore: String = "0"
        private lateinit var sharedPreferences: SharedPreferences

        fun insertAddons(category: CategoryModel, listener: Clicker, context: Context){
           val db = RealmDbTest.realm.where(TestModel::class.java).equalTo("type", category.type).findAll()

            sharedPreferences = context.getSharedPreferences("SP_HIGHEST_SCORE", Context.MODE_PRIVATE)
            highestScore = sharedPreferences.getInt(category.type, 0).toString()

            val cardView = itemView.findViewById(R.id.cardViewCategory) as CardView
            val nameCategory = itemView.findViewById(R.id.tvNameCategory) as TextView
            val nameScore = itemView.findViewById(R.id.score) as TextView
            val imageLogo = itemView.findViewById(R.id.ivIconCategory) as ImageView
            val button = itemView.findViewById(R.id.button) as Button
            val questionNumber = db.size

            imageLogo.setImageResource(category.image)
            nameCategory.text = category.name
                nameScore.text = "$highestScore / $questionNumber"

            cardView.setOnClickListener {listener.onClick(category)}
button.setOnClickListener { listener.onClick1(category) }
        }
    }
}