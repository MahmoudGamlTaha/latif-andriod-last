package com.service.khdmaa.ui.filter.filter_list.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.service.khdmaa.data.models.AdsModel
import com.service.khdmaa.databinding.FragmentListFilterBinding
import com.service.khdmaa.ui.details.DetailsActivity
import com.service.khdmaa.ui.filter.filter_list.FilterListViewModel
import com.service.khdmaa.ui.main.items.PetsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilterListFragment : Fragment() {

    @Inject
    lateinit var viewModel: FilterListViewModel
    lateinit var binding: FragmentListFilterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentListFilterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.flow_.observe(viewLifecycleOwner, Observer {
            if (!it.response.data.isNullOrEmpty()) {
                setList(it.response.data)
            }
        })
    }

    private fun setList(data: List<AdsModel>?) {
        val adapter_ = PetsListAdapter()
        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter_
        }
        adapter_.action= object : PetsListAdapter.Action{
            override fun onAdClick(id: Int?) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("ID",id)
                startActivity(intent)
            }
        }
        adapter_.list=data as MutableList<AdsModel>
    }
}