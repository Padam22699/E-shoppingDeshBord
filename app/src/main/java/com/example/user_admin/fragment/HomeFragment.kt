package com.example.user_admin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.user_admin.Activity.AllOrderActivity
import com.example.user_admin.R
import com.example.user_admin.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
      lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle? ,
    ): View? {
         binding=FragmentHomeBinding.inflate(layoutInflater)

        binding.button.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_cotegaryFragment2)
        }
   binding.button1.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_productFragment2)
        }
   binding.button2.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_sliderFragment2)
        }
  binding.button3.setOnClickListener{
          startActivity(Intent(requireActivity(),AllOrderActivity::class.java))
        }



        return binding.root
    }


}