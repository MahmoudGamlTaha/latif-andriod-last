package com.latifapp.latif.ui.details.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.latifapp.latif.R
import com.latifapp.latif.databinding.FragmentRegisterBinding
import com.latifapp.latif.databinding.FragmentReportDialogBinding
import com.latifapp.latif.ui.details.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReportDialogFragment : DialogFragment(), AdapterView.OnItemSelectedListener {


    private lateinit var binding: FragmentReportDialogBinding
    @Inject
    lateinit var viewModel: DetailsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding=FragmentReportDialogBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpinner()
        binding.reportAdBtn.setOnClickListener {
            if (binding.reason.text.toString().isEmpty())
                binding.reason.error=getString(R.string.required)
            else{
                viewModel.reportAd(binding.reason.text.toString())

            }
        }
    }

    private fun setSpinner() {

        val list = listOf("reason 1","reason 2","reason 3","reason 4","reason 5")
        val arrayAdapter = activity?.let {
            ArrayAdapter<String>(
                it, android.R.layout.simple_list_item_1, list)
        }
        binding.spinner.apply {
            adapter = arrayAdapter
            onItemSelectedListener = this@ReportDialogFragment
            prompt=getString(R.string.reason)
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        Toast.makeText(activity,"Ok $position",Toast.LENGTH_SHORT).show()
//        dismiss()
     }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}