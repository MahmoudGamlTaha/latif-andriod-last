package com.service.khdmaa.ui.main.blogs.blogsList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.models.BlogsModel
import com.service.khdmaa.databinding.FragmentBlogsBinding
import com.service.khdmaa.ui.auth.login.LoginActivity
import com.service.khdmaa.ui.base.BaseFragment
import com.service.khdmaa.ui.main.blogs.blogsDetails.BlogDetailsActivity
import com.service.khdmaa.ui.main.blogs.createBlog.CreateBlogActivity
import com.service.khdmaa.ui.main.home.MainActivity
import com.service.khdmaa.ui.main.items.PetsListAdapter
import com.service.khdmaa.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.blog_item.view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class BlogsFragment : BaseFragment<BlogsViewModel, FragmentBlogsBinding>(),
    PetsCategoryAdapter.CategoryActions {
    private lateinit var searchview: SearchView
    private var category: Int? = null
    private var isLoadingData: Boolean = true

    @Inject
    lateinit var adapter_: BlogsAdapter
    private val petsAdapter = PetsCategoryAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchview = (activity as MainActivity).searchView
        setSearchView()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter_
            addOnScrollListener(scrollListener)


        }
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            petsAdapter.isEnglish=isEnglish
            adapter = petsAdapter
            petsAdapter.action = this@BlogsFragment
        }
        binding.sellBtn.setOnClickListener {
            if (AppPrefsStorage.token.isNullOrEmpty())
                startActivity(Intent(activity, LoginActivity::class.java))
            else {
                val intent = Intent(activity, CreateBlogActivity::class.java)
                startActivityForResult(intent, 2)
            }
        }


        getBlogs()
        getBlogsCategoryList()
        adapter_.action = object : PetsListAdapter.Action {
            override fun onAdClick(id: Int?) {
                val intent = Intent(context, BlogDetailsActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            clearData()
            searchList()
        }
    }

    private fun setSearchView() {

        searchview.apply {
            setOnCloseListener(SearchView.OnCloseListener {
                return@OnCloseListener false
            })
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    clearData()
                    searchList()
                    return false
                }

            })

        }
    }

    private fun searchList() {
        val newText = searchview.query.toString()
        if (newText.isNullOrEmpty())
            getBlogs()
        else getSearchBlogs(newText)
    }


    private fun getSearchBlogs(newText: String) {

        lifecycleScope.launchWhenStarted {
            viewModel.getSearchBlogs(newText).collect {
                adapter_.list.clear()
                if (!it.isNullOrEmpty()) {
                    adapter_.list = it as MutableList<BlogsModel>
                }
                binding.swipeRefresh.setRefreshing(false)
            }
        }
    }

    override fun selectedCategory(id: Int?) {
        Utiles.log_D("cmcmcmmcmcmcm5","$id")
        category = id
        clearData()
        searchList()
    }

    private fun clearData() {

        adapter_.list.clear()
        viewModel.page = 0
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager?.itemCount
            if (!isLoadingData && totalItemCount <= layoutManager.findLastVisibleItemPosition() + 2) {
                isLoadingData = true
                searchList()
            }
        }
    }

    private fun getBlogsCategoryList() {
        lifecycleScope.launchWhenStarted {
            viewModel.getBlogsCategoryList().collect {

                if (!it.isNullOrEmpty()) {
                    petsAdapter.list.addAll(it)
                    petsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun getBlogs() {
        lifecycleScope.launchWhenStarted {
            viewModel.getListOfBlogs(category).collect {
                if (!it.isNullOrEmpty()) {
                    adapter_.list = it as MutableList<BlogsModel>
                    isLoadingData = false
                }

                binding.swipeRefresh.setRefreshing(false)
            }
        }
    }

    override fun setBindingView(inflater: LayoutInflater): FragmentBlogsBinding {
        return FragmentBlogsBinding.inflate(inflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }
}