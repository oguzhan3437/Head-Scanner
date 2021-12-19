package com.oguzhancetin.goodpostureapp.gallery

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.databinding.FragmentGalleryBinding
import com.oguzhancetin.goodpostureapp.databinding.FragmentMainBinding
import com.oguzhancetin.goodpostureapp.getOutputDirectory


class GalleryFragment : Fragment() {


    private var binding: FragmentGalleryBinding? = null
    private val _binding get() = binding!!



    private fun initRc() {
        val galleryAdapter = GalleryRcAdapter(getImagesUri()) {
        }

        _binding.rc.apply {

            adapter  = galleryAdapter

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(layoutInflater)

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


}