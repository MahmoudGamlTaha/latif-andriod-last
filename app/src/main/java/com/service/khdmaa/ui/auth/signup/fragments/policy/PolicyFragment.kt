package com.service.khdmaa.ui.auth.signup.fragments.policy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.service.khdmaa.R
import com.service.khdmaa.databinding.FragmentPolicyBinding
import com.service.khdmaa.ui.auth.signup.SignUpViewModel
import com.service.khdmaa.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class PolicyFragment : BaseFragment<SignUpViewModel, FragmentPolicyBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.interestsBtn.setOnClickListener({
            if (binding.aggreeCheckbox.isChecked)
                navController.navigate(R.id.navTo_interestsFragment)
            else {
                toastMsg_Warning(getString(R.string.agreePolices), binding.root, requireContext())
            }
        })

        viewModel.policesLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.parent.visibility = View.VISIBLE
                binding.policesTxt.text = it
            }
        })
        if (viewModel.policesLiveData.value == null)
            viewModel.getPolices()
    }

    override fun setBindingView(inflater: LayoutInflater): FragmentPolicyBinding {
        return FragmentPolicyBinding.inflate(inflater)
    }

    override fun showLoader() {

    }

    override fun hideLoader() {
    }
}