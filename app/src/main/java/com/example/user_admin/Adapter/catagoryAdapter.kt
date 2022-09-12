package com.example.user_admin.Adapter

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.user_admin.Model.categoryModel
import com.example.user_admin.R
import com.google.android.material.textfield.TextInputEditText

class catagoryAdapter(var context: Context,var list:ArrayList<categoryModel>): RecyclerView.Adapter<catagoryAdapter.CategoryViewHolde>(){

    inner class CategoryViewHolde(view: View):RecyclerView.ViewHolder(view){
         var image:ImageView=view.findViewById(R.id.imageView2)
         var catName:TextView=view.findViewById(R.id.catText)
    }
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): CategoryViewHolde {
       return  CategoryViewHolde(LayoutInflater.from(context).inflate(R.layout.catogery_item,parent,false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolde , position: Int) {
      holder.catName.text = list[position].cat
        Glide.with(context).load(list[position].img).into(holder.image)
    }

    override fun getItemCount(): Int {
       return  list.size
    }
}

