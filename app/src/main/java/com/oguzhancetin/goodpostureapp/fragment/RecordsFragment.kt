package com.oguzhancetin.goodpostureapp.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.oguzhancetin.goodpostureapp.R.id
import com.oguzhancetin.goodpostureapp.adapter.RecordsAdapter
import com.oguzhancetin.goodpostureapp.data.model.Record
import com.oguzhancetin.goodpostureapp.databinding.FragmentRecordsBinding
import com.oguzhancetin.goodpostureapp.viewmodel.MainActivityViewModel
import com.oguzhancetin.goodpostureapp.viewmodel.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordsFragment : BaseFragment<FragmentRecordsBinding>() {
    private val viewModel: RecordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecordsAdapter(listOf<Record>(), requireContext()) { record ->
            goToCamera(record.imageUri?.toUri())
        }
        binding.listviewRecords.adapter = adapter
        viewModel.records.observe(this.viewLifecycleOwner) {
            adapter.loadData(it)
        }
        viewModel.printViewModel()
    }

    private fun goToCamera(uri: Uri?) {
        findNavController()
            .navigate(
                RecordsFragmentDirections.actionRecordsFragmentToMainFragment(
                    uri = uri.toString(),
                    isRecordedPhoto = true
                )
            )
    }

    override fun getViewBinding() = FragmentRecordsBinding.inflate(layoutInflater)
}