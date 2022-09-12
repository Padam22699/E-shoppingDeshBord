package com.example.user_admin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.user_admin.R
import com.example.user_admin.databinding.FragmentCotegaryBinding
import com.example.user_admin.databinding.FragmentProductBinding


class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle? ,
    ): View? {
       binding=FragmentProductBinding.inflate(layoutInflater)

        binding.floatingActionButton.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_productFragment_to_addProductFragment2)
        }

        return binding.root
    }


}