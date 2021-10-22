package com.latifapp.latif.ui.auth.signup.fragments.interests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.R
import com.latifapp.latif.data.models.CategoryModel
import com.latifapp.latif.databinding.FragmentInterestsBinding
import com.latifapp.latif.ui.auth.signup.SignUpViewModel
import com.latifapp.latif.ui.base.BaseFragment
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestsFragment : BaseFragment<SignUpViewModel, FragmentInterestsBinding>() {
    override val viewModel by activityViewModels<SignUpViewModel>()
    private var isLoadingData: Boolean = true
    private val adapter_: InterestsAdapter = InterestsAdapter()
    private var index = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Utiles.log_D("xmmxmxmmxmxmssx", viewModel)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = adapter_
            adapter_.lang = viewModel.lang
            addOnScrollListener(scrollListener)
        }


        binding.doneBtn.setOnClickListener {
            if (adapter_.selectedList.isEmpty()) {
                toastMsg_Warning(
                    getString(R.string.selectAtLeastOne),
                    binding.root,
                    requireContext()
                )
            } else {

                viewModel.interestList = adapter_.selectedList.toMutableList()
                viewModel.register().observe(viewLifecycleOwner, Observer {
                    if (it) {
                        Toast.makeText(activity, R.string.register_success, Toast.LENGTH_SHORT)
                            .show()
                        activity?.finish()

                    }
                })
            }
            Utiles.log_D("xmmxmxmmxmxmx", adapter_.selectedList)
        }
        getList()

    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager?.itemCount
            if (!isLoadingData && totalItemCount <= layoutManager.findLastVisibleItemPosition() + 2) {
                isLoadingData = true
                index++
                getList()
            }
        }

    }

    private fun getList() {
        viewModel.getAllCategories(index).observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                adapter_.list = it as MutableList<CategoryModel>
                isLoadingData = false
            }
        })
    }

    override fun setBindingView(inflater: LayoutInflater): FragmentInterestsBinding {
        return FragmentInterestsBinding.inflate(inflater)
    }

    override fun showLoader() {

    }

    override fun hideLoader() {

    }

}