package com.service.khdmaa.ui.main.profile.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.service.khdmaa.R
import com.service.khdmaa.databinding.FragmentLanguageDialogBinding
import com.service.khdmaa.databinding.FragmentReportDialogBinding
import com.service.khdmaa.ui.details.DetailsViewModel
import com.service.khdmaa.utiles.Utiles
import javax.inject.Inject


class LanguageDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentLanguageDialogBinding

    var lang = Utiles.LANGUAGE
    lateinit var action: LanguageAction
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLanguageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (lang.equals("en"))
            binding.enBtn.setChecked(true)
        else binding.arBtn.setChecked(true)

        binding.submitBtn.setOnClickListener {
            var language = lang
            if (binding.enBtn.isChecked) {
                language = "en"
            } else language = "ar"
            if (::action.isInitialized)
                action.setLanguage(language)

        }
    }

    interface LanguageAction {
        fun setLanguage(language: String)
    }
}