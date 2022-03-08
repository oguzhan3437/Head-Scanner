package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oguzhancetin.goodpostureapp.databinding.FragmentShowResultDialogBinding

class ResultBottomSheet(private val degree:Int,private val onCLickOk:()->Unit) : BottomSheetDialogFragment() {
    private var _binding: FragmentShowResultDialogBinding? = null
    private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentShowResultDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textviewResult0.text = "Your neck degree: $degree "
        binding.textOk.setOnClickListener {
            onCLickOk.invoke()
            this.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}