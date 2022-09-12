package com.example.user_admin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.user_admin.Adapter.catagoryAdapter
import com.example.user_admin.Model.categoryModel
import com.example.user_admin.R
import com.example.user_admin.databinding.FragmentCotegaryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class CotegaryFragment : Fragment() {
    private lateinit var binding: FragmentCotegaryBinding
    private lateinit var diloag: Dialog
    var imageUrl: Uri? = null

    //for select image from phone storage
    private  var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== Activity.RESULT_OK){
            imageUrl=it.data!!.data
            binding.imageView.setImageURI(imageUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle? ,
    ): View? {
     binding=FragmentCotegaryBinding.inflate(layoutInflater)
        diloag= Dialog(requireContext())
        diloag.setContentView(R.layout.progresslyout)
        diloag.setCancelable(false)

        getData()

         binding.apply {
             imageView.setOnClickListener{
                 val intent= Intent("android.intent.action.GET_CONTENT")
                 intent.type="image/*"
                 launchGalleryActivity.launch(intent)
             }

             uploadCatogry.setOnClickListener {
           validateData(binding.categoryName.text.toString())
             }
         }


        return binding.root
    }

    private fun getData() {
       val list=ArrayList<categoryModel>()
        Firebase.firestore.collection("category")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in  it.documents){
                    val data=doc.toObject(categoryModel::class.java)
                    list.add(data!!)
                }
                binding.CategorrRecycler.adapter=catagoryAdapter(requireContext(),list)
            }

    }

    private fun validateData(categoryName: String) {
       if(categoryName.isEmpty()){
           Toast.makeText(requireContext(),"Please Provide catogry name",Toast.LENGTH_SHORT).show()
       }else if(imageUrl == null){
           Toast.makeText(requireContext(),"Please select image",Toast.LENGTH_SHORT).show()

       }else{
           upoadimage(categoryName)
       }
    }

    private fun upoadimage(categoryName: String) {
        diloag.show()
        val fileName= UUID.randomUUID().toString() + ".jpg"
        val refStorage= FirebaseStorage.getInstance().reference.child("category/$fileName")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storedata(categoryName,image.toString())
                }
            }
            .addOnFailureListener{
                diloag.dismiss()
                Toast.makeText(requireContext(),"somthing went wrong with storage",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storedata(categoryName: String , url: String) {
        val db= Firebase.firestore
        val data = hashMapOf<String,Any>(
            "cat" to categoryName,
        "ima" to url)

        db.collection("category").add(data)
            .addOnSuccessListener {
                diloag.dismiss()
                binding.imageView.setImageDrawable(null)
                binding.categoryName.text=null
                getData()
                Toast.makeText(requireContext(),"category Updated",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                diloag.dismiss()
                Toast.makeText(requireContext(),"Somthing went Wrong" ,Toast.LENGTH_SHORT).show()
            }
    }


}