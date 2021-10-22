package com.latifapp.latif.ui.auth.signup.fragments.policy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.latifapp.latif.R
import com.latifapp.latif.databinding.FragmentPolicyBinding
import com.latifapp.latif.ui.auth.signup.SignUpViewModel
import com.latifapp.latif.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class PolicyFragment : BaseFragment<SignUpViewModel, FragmentPolicyBinding>() {
    override val viewModel by activityViewModels<SignUpViewModel>()
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