package com.oguzhancetin.goodpostureapp.gallery

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.databinding.FragmentGalleryBinding
import com.oguzhancetin.goodpostureapp.databinding.FragmentGalleryBinding.*
import com.oguzhancetin.goodpostureapp.databinding.FragmentMainBinding
import com.oguzhancetin.goodpostureapp.getOutputDirectory


class GalleryFragment : Fragment() {


    private var binding: FragmentGalleryBinding? = null
    private val _binding get() = binding!!


    private fun initRc() {
        val galleryAdapter = GalleryRcAdapter(getImagesUri(), ::onImageClick)
        _binding.rc.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(requireContext(), 4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(layoutInflater)

        initRc()

        return _binding.root
    }

    private fun getImagesUri(): List<Uri> {
        val uriList = arrayListOf<Uri>()
        val imageFiles = getOutputDirectory(requireActivity().application).listFiles()
        imageFiles.forEach {
            uriList.add(it.toUri())
        }
        return uriList
    }

    private fun onImageClick(uri: Uri) {
        val direction = GalleryFragmentDirections.actionGalleryFragmentToMainFragment(uri.toString())
        findNavController().navigate(direction)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}