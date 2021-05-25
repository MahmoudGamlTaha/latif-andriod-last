package com.latifapp.latif.ui.auth.signup.fragments.interests

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.latifapp.latif.R
import com.latifapp.latif.databinding.FragmentInterestsBinding
import com.latifapp.latif.databinding.FragmentRegisterBinding
import com.latifapp.latif.ui.auth.signup.SignUpViewModel
import com.latifapp.latif.ui.main.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InterestsFragment : Fragment() {
    @Inject
    lateinit var viewModel: SignUpViewModel
     private val adapter_: InterestsAdapter = InterestsAdapter()
    private lateinit var binding:FragmentInterestsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentInterestsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            layoutManager=GridLayoutManager(activity,3)
            adapter=adapter_
        }


        binding.doneBtn.setOnClickListener {
            viewModel.register().observe(viewLifecycleOwner, Observer {
                if (it) {
                    activity?.onBackPressed()
                    Toast.makeText(activity, R.string.register_success,Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

}