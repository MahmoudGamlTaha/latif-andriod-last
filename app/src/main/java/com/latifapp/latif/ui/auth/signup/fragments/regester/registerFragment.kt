package com.latifapp.latif.ui.auth.signup.fragments.regester

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.latifapp.latif.R
import com.latifapp.latif.databinding.FragmentRegisterBinding
import com.latifapp.latif.ui.auth.editProfile.EditProfileViewModel
import com.latifapp.latif.ui.auth.login.LoginViewModel
import com.latifapp.latif.ui.auth.signup.SignUpViewModel
import com.latifapp.latif.ui.auth.signup.fragments.regester.dialog.city.CityDialog
import com.latifapp.latif.ui.auth.signup.fragments.regester.dialog.country.CountryDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class registerFragment : Fragment() {

     val viewModel by activityViewModels<SignUpViewModel>()
    private lateinit var binding: FragmentRegisterBinding
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        // viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        viewModel.getCountries().observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.countryEx.setOnClickListener {
                    getCountryDialog()
                }
            }

        })

        viewModel.countryModelLiveData.observe(viewLifecycleOwner, Observer {
            binding.countryEx.text = it
            if (it != null) {
                binding.cityEx.setOnClickListener {
                    getCityDialog()
                }
            }else binding.cityEx.setOnClickListener(null)
        })
        viewModel.cityModelLiveData.observe(viewLifecycleOwner, Observer {
            binding.cityEx.text = it
        })

        binding.policyBtn.setOnClickListener({
            val name = binding.nameEx.text.toString()
            val email = binding.emailEx.text.toString()
            val password = binding.passwordEx.text.toString()
            val phone = binding.phoneEx.text.toString()
            val country = viewModel.countryId
            val city = viewModel.cityId
            val address = binding.addressEx.text.toString()
            val govs = binding.governorateEx.text.toString()
            if (viewModel.validate(name, email, password, phone, country, address, city, govs))
                navController.navigate(R.id.navTo_policyFragment)
        })

        viewModel.validateLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null)
                when (it) {
                    LoginViewModel.SignUpFiled.email_ -> binding.emailEx.setError(getString(R.string.check_email))
                    LoginViewModel.SignUpFiled.name_ -> binding.nameEx.setError(getString(R.string.valid_name))
                    LoginViewModel.SignUpFiled.password_ -> binding.passwordEx.setError(getString(R.string.passwordError))
                    LoginViewModel.SignUpFiled.phone_ -> binding.phoneEx.setError(getString(R.string.valid_phone))
                    LoginViewModel.SignUpFiled.address_ -> binding.addressEx.setError(getString(R.string.valid_address))
                    LoginViewModel.SignUpFiled.city_ -> binding.cityEx.setError(getString(R.string.valid_city))
                    LoginViewModel.SignUpFiled.country_ -> binding.countryEx.setError(getString(R.string.valid_country))
                    LoginViewModel.SignUpFiled.governorate_ -> binding.governorateEx.setError(
                        getString(R.string.valid_govs)
                    )
                }
        })
    }

    private fun getCityDialog() {
        childFragmentManager.let {
            CityDialog(viewModel).apply {
                show(it, tag)
            }
        }

    }


    private fun getCountryDialog() {
        childFragmentManager.let {
            CountryDialog(viewModel).apply {
                show(it, tag)
             }
        }
    }

}