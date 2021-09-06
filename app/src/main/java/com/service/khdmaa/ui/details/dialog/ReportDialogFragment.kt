package com.service.khdmaa.ui.details.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.service.khdmaa.R
import com.service.khdmaa.data.models.ReportedReasonsList
import com.service.khdmaa.databinding.FragmentReportDialogBinding
import com.service.khdmaa.ui.details.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ReportDialogFragment() : DialogFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var reasons_list: List<ReportedReasonsList>
    private lateinit var binding: FragmentReportDialogBinding
    private var reasonID: String? = null
    private var otherReason: String? = null

    @Inject
    lateinit var viewModel: DetailsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReportDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.report_List_liveData.value == null) {
            viewModel.reportReasonsList()
        }
        viewModel.report_List_liveData.observe(requireActivity(), Observer {
            if (it != null) {
                this.reasons_list = it

                setSpinner()
            }
        })

        binding.reportAdBtn.setOnClickListener {
            if (binding.reason.visibility == View.VISIBLE) {
                reasonID = null
                otherReason = binding.reason.text.toString()
            }
            if (reasonID.isNullOrEmpty()&&otherReason.isNullOrEmpty()) {
                if (binding.reason.visibility == View.VISIBLE)
                    binding.reason.error = getString(R.string.required)
                else
                    (binding.spinner.getSelectedView() as TextView)?.error =
                        getString(R.string.required)
            } else {
                viewModel.reportAd(reasonID,otherReason)
                dismiss()
            }
        }
    }

    private fun setSpinner() {

        val list = reasons_list.map {
            if (viewModel.lang.equals("en"))
                it.value
            else it.valueAr
        } as MutableList<String>
        list.add(requireActivity().getString(R.string.other))
        val arrayAdapter = activity?.let {
            ArrayAdapter<String>(
                it, android.R.layout.simple_list_item_1, list
            )
        }
        binding.spinner.apply {
            adapter = arrayAdapter
            onItemSelectedListener = this@ReportDialogFragment
            prompt = getString(R.string.reason)
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == reasons_list.size) {
            binding.reason.visibility = View.VISIBLE
            reasonID = ""
        } else {
            reasonID = reasons_list.get(position).id.toString()
            binding.reason.visibility = View.GONE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}