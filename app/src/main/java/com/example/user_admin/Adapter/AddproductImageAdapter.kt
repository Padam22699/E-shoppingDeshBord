package com.example.user_admin.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.user_admin.databinding.ImageItemBinding

class AddproductImageAdapter(val list:ArrayList<Uri>):RecyclerView.Adapter<AddproductImageAdapter.AddproductImageViewHolder>() {

inner class AddproductImageViewHolder(val binding:ImageItemBinding)
    :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): AddproductImageViewHolder {
     val binding=ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
      return AddproductImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddproductImageViewHolder , position: Int) {
       holder.binding.itemImage.setImageURI(list[position])
    }

    override fun getItemCount(): Int {
      return list.size
    }

}