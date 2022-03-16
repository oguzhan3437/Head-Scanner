package com.oguzhancetin.goodpostureapp.util

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import com.oguzhancetin.goodpostureapp.repository.PreferenceRepository

class ItemDecoration(
    @Px
    private val space: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(space,space,space,space)
    }
}