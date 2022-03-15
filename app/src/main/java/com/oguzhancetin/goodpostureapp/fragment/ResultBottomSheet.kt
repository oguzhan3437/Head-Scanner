package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oguzhancetin.goodpostureapp.databinding.FragmentShowResultDialogBinding

class ResultBottomSheet(private val degree: Int, private val onCLickOk: () -> Unit) :
    BottomSheetDialogFragment() {
    private var _binding: FragmentShowResultDialogBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowResultDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textviewResult0.text = "Your neck degree: $degree "
        binding.textviewResult0.text = getResultTextInDetail(degree)
        binding.textOk.setOnClickListener {
            onCLickOk.invoke()
            this.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getResultTextInDetail(degree: Int): String {
        return when (degree) {
            in 0..10 -> "10 kg effect your neck.This is normal weight"
            in 10..30 -> "When the angle increases to 30 degrees weight of your head effectively increases to 18kg"
            in 10..45 -> "When the angle increases to 45 degrees weight of your head effectively increases to 22kg"
            in 45..60 -> "When the angle increases to 60 degrees weight of your head effectively increases to 27kg, which is similar to carrying around an 8-year-old child."
            else -> {""}
        }
    }

}