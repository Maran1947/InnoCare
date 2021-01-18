package com.example.innocare.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.innocare.R
import com.example.innocare.model.Food
import com.example.innocare.utils.utils
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.recycler_card_layout.view.*

class FoodAdapter(options: FirestoreRecyclerOptions<Food>) :
        FirestoreRecyclerAdapter<Food, FoodAdapter.FoodViewHolder>(options) {

    class FoodViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val foodName: TextView = itemView.findViewById(R.id.tv_foodName)
        val fats: TextView = itemView.findViewById(R.id.tv_fats)
        val protein: TextView = itemView.findViewById(R.id.tv_protein)
        val carbohydrates: TextView = itemView.findViewById(R.id.tv_carbohydrates)
        val calories: TextView = itemView.findViewById(R.id.tv_calorie)
        val qty: TextView = itemView.findViewById(R.id.tv_qty)
        val createdAt: TextView = itemView.findViewById(R.id.tv_createdat)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_card_layout, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int, model: Food) {


        holder.foodName.text = model.foodName
        holder.qty.text = model.qty.toString()
        holder.fats.text = model.fats.toString() + "g"
        holder.protein.text = model.protein.toString() + "g"
        holder.carbohydrates.text = model.carbohydrates.toString() + "g"
        holder.calories.text = model.calories.toString()

        holder.createdAt.text = utils.getTimeAgo(model.createdAt)
    }

}