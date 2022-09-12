package com.example.user_admin.Model

data class addProductModel (
    val productName:String?="",
    val productDescription:String?="",
    val productCoverImage:String?="",
    val productcategry:String?="",
    val productId:String?="",
    val productMrp:String?="",
    val productSp:String?="",
    val productImage:ArrayList<String>?
    )