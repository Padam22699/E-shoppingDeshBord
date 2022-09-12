package com.example.user_admin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.user_admin.Adapter.AddproductImageAdapter
import com.example.user_admin.Model.addProductModel
import com.example.user_admin.Model.categoryModel
import com.example.user_admin.R
import com.example.user_admin.databinding.FragmentAddProductBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class AddProductFragment : Fragment() {
    private lateinit var binding:FragmentAddProductBinding

    private lateinit var list:ArrayList<Uri>
    private lateinit var listImage:ArrayList<String>
    private lateinit var adapte:AddproductImageAdapter
    private var coverImage:Uri?=null
    private lateinit var dialog:Dialog
    private var coverImageUrl:String?=""
    private lateinit var categoryList:ArrayList<String>

    private  var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            coverImage = it.data!!.data
            binding.prodectCoverImage.setImageURI(coverImage)
            binding.prodectCoverImage.visibility = View.VISIBLE
        }
    }

    private  var launchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== Activity.RESULT_OK){
          val imageUrl=it.data!!.data
            list.add(imageUrl!!)
            adapte.notifyDataSetChanged()

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle? ,
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
              list= ArrayList()
            listImage= ArrayList()
              dialog= Dialog(requireContext())
          dialog.setCancelable(false)
        dialog.setContentView(R.layout.progresslyout)

        binding.selectCoverImge.setOnClickListener{
            val intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchGalleryActivity.launch(intent)
        }
        binding.productImge.setOnClickListener{
            val intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchProductActivity.launch(intent)
        }

        setProductCategory()
        adapte= AddproductImageAdapter(list)
        binding.productImagaRecyclerView.adapter=adapte

        binding.submiteProduct.setOnClickListener{
            validateData()
        }

        return binding.root


    }

    private fun validateData() {
      if(binding.productName.text.toString().isEmpty()){
          binding.productName.requestFocus()
          binding.productName.error="Empty"
      }else if(binding.productDescription.text.toString().isEmpty()){
          binding.productName.requestFocus()
          binding.productDescription.error="Empty"
      }else if(binding.producPrice.text.toString().isEmpty()){
          binding.productName.requestFocus()
          binding.producPrice.error="Empty"
      }else if(binding.productSP.text.toString().isEmpty()){
          binding.productName.requestFocus()
          binding.productSP.error="Empty"
      } else if(coverImage == null){
          Toast.makeText(requireContext() , "plese select cover Image" , Toast.LENGTH_SHORT).show()
      }
       else if(list.size < 1){
          Toast.makeText(requireContext() , "plese select product Images" , Toast.LENGTH_SHORT).show()
      }else{
          uploadImage()
      }

    }
private var i =0
    private fun uploadImage() {
        dialog.show()
        val fileName= UUID.randomUUID().toString() + ".jpg"
        val refStorage= FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
               coverImageUrl=image.toString()

                    uploadProductImage()
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"somthing went wrong with storage",Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadProductImage() {
        dialog.show()
        val fileName= UUID.randomUUID().toString() + ".jpg"
        val refStorage= FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    listImage.add((image.toString()))
                    if(list.size == listImage.size){
                        storeData()
                    }else{
                        i=i+1
                          uploadProductImage()
                    }

                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"somthing went wrong with storage",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData() {
       val db=Firebase.firestore.collection("product")
        val key=db.document().id
        val data= addProductModel(
            binding.productName.text.toString(),
            binding.productDescription.text.toString(),
            coverImageUrl.toString(),
            categoryList[binding.productcategryDropDoun.selectedItemPosition],
            key,
            binding.producPrice.text.toString(),
            binding.productSP.text.toString(),
            listImage
        )
        db.document(key).set(data).addOnSuccessListener {
           dialog.dismiss()
            Toast.makeText(requireContext(),"Product Added",Toast.LENGTH_SHORT).show()
            binding.productName.text=null
            binding.productDescription.text=null
            binding.producPrice.text=null
            binding.productSP.text=null

        }.addOnFailureListener{
            dialog.dismiss()
            Toast.makeText(requireContext(),"Somthing Went Worng",Toast.LENGTH_SHORT).show()

        }
    }

    private fun setProductCategory(){
             categoryList= ArrayList()
           Firebase.firestore.collection("category").get().addOnSuccessListener {
               for(doc in it.documents){
                   val data = doc.toObject(categoryModel::class.java)
                   categoryList.add(data!!.cat!!)
               }
               categoryList.add(0,"Select Category")
               val arrayAdapter=ArrayAdapter(requireContext(),R.layout.dropdown_item,categoryList)
               binding.productcategryDropDoun.adapter=arrayAdapter
           }
    }
}