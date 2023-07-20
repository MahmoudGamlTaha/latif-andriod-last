package com.latifapp.latif.ui.main.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.latifapp.latif.R
import com.latifapp.latif.databinding.ActivityMainBinding
import com.latifapp.latif.ui.auth.login.LoginActivity
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.filter.filter_form.FilterFormActivity
import com.latifapp.latif.ui.main.blogs.blogsDetails.BolgDetailsViewModel
import com.latifapp.latif.ui.main.pets.PetsAdapter
import com.latifapp.latif.ui.main.profile.ProfileActivity
import com.latifapp.latif.ui.subscribe.subscribList.SubscribeActivity
import com.latifapp.latif.utiles.AppConstants
import com.latifapp.latif.utiles.AppConstants.PETS_STR
import com.latifapp.latif.utiles.MyContextWrapper
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(),
    NavController.OnDestinationChangedListener,
    MenuAdapter.MenuAction, BottomNavItemsAdapter.Action, PetsAdapter.CategoryActions {
    override val viewModel by viewModels<MainViewModel>()
    private var selectedItemPosition: Int = 0
    private var isMappingDisplay = true
    private val bottomAdapter = BottomNavItemsAdapter(this@MainActivity)
    private lateinit var navigation: NavController
    lateinit var searchBtn: ImageView
    lateinit var searchView: SearchView
    lateinit var toolBarTitle: TextView
    private var type = ""
    private val petsAdapter = PetsAdapter()
    private val petsType = MainViewModel.TYPES(AppConstants.PETS, PETS_STR)
    private val petsCareType = MainViewModel.TYPES(AppConstants.PET_CARE, AppConstants.PET_CARE_STR)
    private val serviceType = MainViewModel.TYPES(AppConstants.SERVICE, AppConstants.SERVICE_STR)
    private val accessoriesType =
        MainViewModel.TYPES(AppConstants.ACCESSORIES, AppConstants.ACCESSORIES_STR)


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        searchBtn = binding.toolbar.searchBtn
        searchView = binding.toolbar.searchView
        toolBarTitle = binding.toolbar.title

        navigation = Navigation.findNavController(
            this,
            R.id.fragment_container
        )

        setTopBar()
        setBottomBarNav()

        navigation.addOnDestinationChangedListener(this)
        setMenu()
        searchBtn.setOnClickListener {
            val intent = Intent(this, FilterFormActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("isMap", isMappingDisplay)
            startActivity(intent)
        }

        setScaleView( listeBtn,mapBtn )
        binding.listeBtn.setOnClickListener {

           // onBackPressed()
            isMappingDisplay = false
           // if (isMappingDisplay) {
           //     setScaleView( mapBtn,listeBtn )

              //  listeBtn.visibility = GONE
                //mapBtn.visibility = VISIBLE
                selectedItem(selectedItemPosition)
            setScaleView(mapBtn, listeBtn)
           // }
        }
        binding.mapBtn.setOnClickListener {

            if (!isMappingDisplay &&  mapBtn.visibility == VISIBLE) {
               // listeBtn.visibility = VISIBLE
              //  mapBtn.visibility = GONE
                onBackPressed();
                // selectedItem(selectedItemPosition)
            }
            isMappingDisplay = true
            setScaleView(listeBtn,mapBtn)
        }
    }

    fun setScaleView(largeIcon: ImageView, smallView: ImageView) {
        largeIcon.scaleX = 0.6f
        largeIcon.scaleY = 0.6f
        largeIcon.visibility = VISIBLE
        smallView.visibility = GONE
       // smallView.scaleX = 0.6f
        //smallView.scaleY = 0.6f
    }

    private fun setTopBar() {
        binding.categoryList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            petsAdapter.isEnglish = viewModel.lang.equals("en")
            adapter = petsAdapter
            petsAdapter.action = this@MainActivity
        }
        lifecycleScope.launchWhenStarted {
            viewModel.typeFlow.collect {
                petsAdapter.clear()
                viewModel.categoryFlow.value = -1
                getCategoriesList(it.categoryType)

            }
        }

    }

    override fun onResume() {
        super.onResume()
//        val id = navigation.currentDestination?.id
//        navigation.popBackStack(id!!,true)
//        navigation.navigate(id)

    }

    private fun setBottomBarNav() {
        binding.bottomNavRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 5)
            adapter = bottomAdapter

        }
        selectedItem(0)
    }

    private fun setMenu() {
        binding.menuRecycleview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MenuAdapter(this@MainActivity)
        }
        binding.toolbar.menuBtn.setOnClickListener {
            if (binding.drawerLayout.isOpen)
                binding.drawerLayout.closeDrawers()
            else
                binding.drawerLayout.openDrawer(GravityCompat.START)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        isMappingDisplay = true
        setScaleView(mapBtn, listeBtn)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        Utiles.log_D("cncncncncncncncn", selectedItemPosition)
        when (destination.id) {
            R.id.pets_fragments -> {
                bottomAdapter.show(selectedItemPosition)
              //  isMappingDisplay = true
                displayCategoriesAndFilter(true)
             //   setScaleView(mapBtn, listeBtn)
            }
            R.id.items_fragments -> {
                bottomAdapter.show(selectedItemPosition)
            //    isMappingDisplay = false
                displayCategoriesAndFilter(true)
            //    setScaleView(listeBtn,mapBtn)
            }
            R.id.blogs_fragments -> {
                bottomAdapter.show(4)
                displayCategoriesAndFilter(false)
            }
        }
    }

    private fun displayCategoriesAndFilter(display: Boolean) {
        if (display) {
            searchView.visibility = GONE
            searchBtn.visibility = VISIBLE
            binding.togelContainer.visibility = VISIBLE
            binding.view55.visibility = VISIBLE
            binding.categoryList.visibility = VISIBLE
            binding.toolbar.titleContainer.visibility = VISIBLE
        } else {
            searchView.visibility = VISIBLE
            searchBtn.visibility = GONE
            binding.togelContainer.visibility = GONE
            binding.view55.visibility = GONE
            binding.categoryList.visibility = GONE
            binding.toolbar.titleContainer.visibility = GONE
        }
    }

    override fun menuClick(enum: MenuAdapter.MenuEnum) {

        when (enum) {
            MenuAdapter.MenuEnum.profile -> startActivity(
                if (token.isNullOrEmpty())
                    Intent(this, LoginActivity::class.java)
                else
                    Intent(this, ProfileActivity::class.java)
            )
            MenuAdapter.MenuEnum.blogs -> {
                isMappingDisplay = false
                navigation.navigate(R.id.nav_blogs_fragments)
            }
            MenuAdapter.MenuEnum.pets -> {
                isMappingDisplay = false
                selectedItem(0)
            }
            MenuAdapter.MenuEnum.items -> {
                isMappingDisplay = false
                selectedItem(1)
            }
            MenuAdapter.MenuEnum.service -> {
                isMappingDisplay = false
                selectedItem(3)
            }
            MenuAdapter.MenuEnum.clinic -> {
                isMappingDisplay = false
                selectedItem(2)
            }
            MenuAdapter.MenuEnum.subscribe -> startActivity(
                Intent(
                    this,
                    SubscribeActivity::class.java
                )
            )
        }
        runBlocking {
            binding.drawerLayout.closeDrawers()
        }

    }

    private fun getCategoriesList(categoryType: Int) {

        lifecycleScope.launchWhenStarted {
            viewModel.getCategoriesList(categoryType).collect {
                if (!it.isNullOrEmpty()) {
                    petsAdapter.list.addAll(it)
                    petsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun selectedItem(pos: Int) {
        if (pos != 4) // blogs when back >> back to  map on selected position of bottom filter
            selectedItemPosition = pos

        if (!isMappingDisplay) {
            navigation.navigate(R.id.nav_items_fragments)
        }
        when (pos) {
            0 -> {
                type = PETS_STR
                viewModel.typeFlow.value = petsType
            }
            1 -> {
                type = AppConstants.ACCESSORIES_STR
                viewModel.typeFlow.value = accessoriesType
            }
            2 -> {
                type = AppConstants.PET_CARE_STR
                viewModel.typeFlow.value = petsCareType
            }
            3 -> {
                type = AppConstants.SERVICE_STR
                viewModel.typeFlow.value = serviceType
            }
            4 -> menuClick(MenuAdapter.MenuEnum.blogs)
        }

    }

    override fun setBindingView(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun showLoader() {
    }

    override fun hideLoader() {
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.data != null) {
            Utiles.log_D("cncnncncncnncn", "${intent.data?.query}  ${intent.data?.encodedQuery}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun selectedCategory(id: Int) {
        viewModel.categoryFlow.value = id
    }
}