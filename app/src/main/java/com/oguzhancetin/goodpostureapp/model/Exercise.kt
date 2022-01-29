package com.oguzhancetin.goodpostureapp.model

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes

data class Exercise(
    val id:Int,
    val name:String,
    @DrawableRes
    val imageLocation:Int,

)
