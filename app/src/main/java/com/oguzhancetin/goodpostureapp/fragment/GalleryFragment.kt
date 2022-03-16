package com.oguzhancetin.goodpostureapp.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.databinding.FragmentGalleryBinding
import com.oguzhancetin.goodpostureapp.databinding.FragmentGalleryBinding.*
import com.oguzhancetin.goodpostureapp.adapter.GalleryRcAdapter
import com.oguzhancetin.goodpostureapp.getOutputDirectory
import com.oguzhancetin.goodpostureapp.util.ItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    private fun initRc() {
        val galleryAdapter = GalleryRcAdapter(getImagesUri(), ::onImageClick)
        binding.rc.apply {
            addItemDecoration(ItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_tiny)))
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(requireContext(), 4)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRc()
    }

    private fun getImagesUri(): List<Uri> {
        val uriList = arrayListOf<Uri>()
        val imageFiles = getOutputDirectory(requireActivity().application).listFiles()
        imageFiles?.let {
            it.forEach {
                uriList.add(it.toUri())
            }
        }
        return uriList
    }

    private fun onImageClick(uri: Uri) {
        val direction =
            GalleryFragmentDirections.actionGalleryFragmentToMainFragment(uri.toString())
        findNavController().navigate(direction)
    }

    override fun getViewBinding() = inflate(layoutInflater)
}