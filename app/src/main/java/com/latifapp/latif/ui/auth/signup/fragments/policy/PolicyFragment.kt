package com.latifapp.latif.ui.auth.signup.fragments.policy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.latifapp.latif.R
import com.latifapp.latif.databinding.FragmentPolicyBinding
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PolicyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PolicyFragment : Fragment() {
    private lateinit var binding: FragmentPolicyBinding
    private lateinit var navController: NavController



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPolicyBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        binding.interestsBtn.setOnClickListener({
            navController.navigate(R.id.navTo_interestsFragment)
        })
    }
}