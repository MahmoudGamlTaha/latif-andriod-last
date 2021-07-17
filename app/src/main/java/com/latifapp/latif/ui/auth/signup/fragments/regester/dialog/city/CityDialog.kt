package com.latifapp.latif.ui.auth.signup.fragments.regester.dialog.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.latifapp.latif.R
import com.latifapp.latif.databinding.CountryDialogBinding
import com.latifapp.latif.ui.auth.editProfile.EditProfileViewModel
import com.latifapp.latif.ui.auth.signup.CountryViewModel
import com.latifapp.latif.ui.auth.signup.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

 class CityDialog(val viewModel: CountryViewModel) :BottomSheetDialogFragment(), CityAdapter.Actions {

     private lateinit var binding: CountryDialogBinding

     override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
     override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View? {
         // Inflate the layout for this fragment
         binding= CountryDialogBinding.inflate(inflater,container,false)
         return binding.root
     }


     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         val isEnglish =if (viewModel.lang.equals("en")) true else false
          binding.recyclerView.apply {
             layoutManager=LinearLayoutManager(context)
             val adapter_= CityAdapter(isEnglish, viewModel.cityList,this@CityDialog)
              adapter= adapter_
         }

     }

    override fun select(id: String, name: String) {
        viewModel.setCity(id,name)
        dismiss()
    }
}