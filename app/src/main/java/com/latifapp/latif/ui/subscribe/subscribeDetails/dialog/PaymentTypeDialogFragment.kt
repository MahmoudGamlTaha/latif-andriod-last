package com.latifapp.latif.ui.subscribe.subscribeDetails.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.latifapp.latif.R
import com.latifapp.latif.databinding.FragmentLanguageDialogBinding
import com.latifapp.latif.databinding.FragmentPaymentTypeDialogBinding



class PaymentTypeDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentPaymentTypeDialogBinding

    var type:String? = null
    lateinit var action: PaymentAction
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentTypeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.submitBtn.setOnClickListener {

            if (binding.madaBtn.isChecked) {
                type = "1"
            } else type = null
            if (::action.isInitialized)
                action.setpayment(type)

            dismiss()

        }
    }

    interface PaymentAction {
        fun setpayment(type: String?)
    }
}