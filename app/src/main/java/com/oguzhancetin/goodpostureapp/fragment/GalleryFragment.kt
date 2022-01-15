package com.oguzhancetin.goodpostureapp.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzhancetin.goodpostureapp.databinding.FragmentGalleryBinding
import com.oguzhancetin.goodpostureapp.databinding.FragmentGalleryBinding.*
import com.oguzhancetin.goodpostureapp.gallery.GalleryRcAdapter
import com.oguzhancetin.goodpostureapp.getOutputDirectory

class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    private fun initRc() {
        val galleryAdapter = GalleryRcAdapter(getImagesUri(), ::onImageClick)
        binding.rc.apply {
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