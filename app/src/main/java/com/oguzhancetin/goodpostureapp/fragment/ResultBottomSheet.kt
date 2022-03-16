package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oguzhancetin.goodpostureapp.R
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
        val resultString = "Your neck degree: $degree"
        binding.txtResult.text = resultString
        binding.txtDetailResult.text = getResultTextInDetail(degree)
        binding.textOk.setOnClickListener {
            onCLickOk.invoke()
            this.dismiss()
        }
        binding.textCancel.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getResultTextInDetail(degree: Int): String {
        return when (degree) {
            in 0..12 -> getString(R.string.exercise_name_1)
            in 12..30 -> getString(R.string.exercise_name_2)
            in 10..45 -> getString(R.string.exercise_name_3)
            in 45..60 -> getString(R.string.exercise_name_4)
            else -> {
                ""
            }
        }
    }

}