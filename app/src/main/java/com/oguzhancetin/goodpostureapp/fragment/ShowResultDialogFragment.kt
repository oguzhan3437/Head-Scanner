package com.oguzhancetin.goodpostureapp.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.databinding.FragmentShowResultDialogBinding


class ShowResultDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Your Result")
            .setPositiveButton("Exercises") { _, _ -> }
            .setView(FragmentShowResultDialogBinding.inflate(layoutInflater).root)
            .create()

    companion object {
        const val TAG = "ResultDialog"
    }




}