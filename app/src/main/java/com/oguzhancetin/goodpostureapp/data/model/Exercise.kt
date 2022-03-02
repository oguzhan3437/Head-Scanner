package com.oguzhancetin.goodpostureapp.data.model

import androidx.annotation.DrawableRes

data class Exercise(
    val id:Int,
    val name:String,
    @DrawableRes
    val imageLocation:Int,

)
