package com.oguzhancetin.goodpostureapp.data.model

import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


class Exercise (
    val id:Int,
    val name:String,
    val headerExplanation:String,
    val explanation:String,
    @DrawableRes
    val imageLocation:Int,

): Serializable
