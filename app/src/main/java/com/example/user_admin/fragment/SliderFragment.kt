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
import com.example.user_admin.R
import com.example.user_admin.databinding.FragmentSliderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class SliderFragment : Fragment() {
   private lateinit var binding: FragmentSliderBinding
   private lateinit var diloag:Dialog
    var imageUrl: Uri? = null

    //for select image from phone storage
   private  var launchGalleryActivity = registerForActivityResult(
       ActivityResultContracts.StartActivityForResult()
   ){
      if(it.resultCode==Activity.RESULT_OK){
         imageUrl=it.data!!.data
          binding.imageView.setImageURI(imageUrl)
      }
   }
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle? ,
    ): View? {
        binding = FragmentSliderBinding.inflate(layoutInflater)

      diloag= Dialog(requireContext())
        diloag.setContentView(R.layout.progresslyout)
        diloag.setCancelable(false)

        binding.apply {
            imageView.setOnClickListener{
               val intent=Intent("android.intent.action.GET_CONTENT")
                intent.type="image/*"
                launchGalleryActivity.launch(intent)
            }

         uploadImages.setOnClickListener {
                if(imageUrl!=null ){
                   uploadImages1(imageUrl!!)
                }else{
                  Toast.makeText(requireContext(),"Please Slect Image",Toast.LENGTH_SHORT).show()
                }
         }
        }

        return  binding.root



    }

    private fun uploadImages1(uri:Uri) {
      diloag.show()
        val fileName=UUID.randomUUID().toString() + ".jpg"
        val refStorage=FirebaseStorage.getInstance().reference.child("Slider/$fileName")
        refStorage.putFile(uri)
            .addOnSuccessListener {
             it.storage.downloadUrl.addOnSuccessListener { image ->
                 storedata(image.toString())
             }
            }
            .addOnFailureListener{
              diloag.dismiss()
                Toast.makeText(requireContext(),"somthing went wrong with storage",Toast.LENGTH_SHORT).show()
            }

    }

    private fun storedata(image: String) {
         val db=Firebase.firestore
        val data = hashMapOf<String,Any>(
          "img" to image)

        db.collection("slider").document("item").set(data)
            .addOnSuccessListener {
                diloag.dismiss()
                Toast.makeText(requireContext(),"slider Updated",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                diloag.dismiss()
                Toast.makeText(requireContext(),"Somthing went Wrong" ,Toast.LENGTH_SHORT).show()
            }
    }

}