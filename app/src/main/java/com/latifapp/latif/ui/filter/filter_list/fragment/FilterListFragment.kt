package com.latifapp.latif.ui.filter.filter_list.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.databinding.FragmentListFilterBinding
import com.latifapp.latif.ui.details.DetailsActivity
import com.latifapp.latif.ui.filter.filter_list.FilterListViewModel
import com.latifapp.latif.ui.main.items.PetsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilterListFragment : Fragment() {

      val viewModel by activityViewModels<FilterListViewModel>()
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