package com.service.khdmaa.ui.main.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.internal.Objects
import com.service.khdmaa.R
import com.service.khdmaa.data.models.MsgNotification
import com.service.khdmaa.databinding.ActivityMainBinding
import com.service.khdmaa.ui.auth.login.LoginActivity
import com.service.khdmaa.ui.base.BaseActivity
import com.service.khdmaa.ui.details.DetailsActivity
import com.service.khdmaa.ui.filter.filter_form.FilterFormActivity
import com.service.khdmaa.ui.main.chat.chatPage.ChatPageActivity
import com.service.khdmaa.ui.main.pets.PetsAdapter
import com.service.khdmaa.ui.main.profile.ProfileActivity
import com.service.khdmaa.ui.subscribe.subscribList.SubscribeActivity
import com.service.khdmaa.utiles.AppConstants
import com.service.khdmaa.utiles.AppConstants.PETS_STR
import com.service.khdmaa.utiles.MyContextWrapper
import com.service.khdmaa.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(),
    NavController.OnDestinationChangedListener,
    MenuAdapter.MenuAction, BottomNavItemsAdapter.Action, PetsAdapter.CategoryActions {
    private var selectedItemPosition: Int = 0
    private var isMappingDisplay = true
    private val bottomAdapter = BottomNavItemsAdapter(this@MainActivity)
    private lateinit var navigation: NavController
    lateinit var searchBtn: ImageView
    lateinit var searchView: SearchView
    lateinit var toolBarTitle: TextView
    private var type = ""
    private val petsAdapter = PetsAdapter()
    private val MedicalType = MainViewModel.TYPES(AppConstants.MEDICAL, AppConstants.MEDICAL_STR)
    private val occasinalType = MainViewModel.TYPES(AppConstants.OCCASIONAL, AppConstants.OCCASIONAL_STR)
    private val serviceType = MainViewModel.TYPES(AppConstants.SERVICE, AppConstants.SERVICE_STR)
    private val commericalType = MainViewModel.TYPES(AppConstants.COMMERICIAL, AppConstants.COMMERCIAL_STR)
    private val otherType = MainViewModel.TYPES(AppConstants.OTHERS, AppConstants.OTHERS_STR)


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
        if(intent.extras?.get("chat") != null){
            val chatIntent = Intent(this, ChatPageActivity::class.java)
            var body = intent.extras?.get("chat")
            if( body is MsgNotification?) {
                chatIntent.putExtra("model", body)
                startActivity(chatIntent)
            }
        }
        val check = intent.extras?.get("click_action")
        if(check!= null && check.toString().equals("DetailsActivity")){
            val prod_id = intent.extras?.get("prod_id")
            if(prod_id != null || prod_id!= 0){
                val detailsIntent = Intent(this, DetailsActivity::class.java)
                detailsIntent.putExtra("ID", prod_id.toString().toInt())
                startActivity(detailsIntent)
            }
        }
        navigation.addOnDestinationChangedListener(this)
        setMenu()
        searchBtn.setOnClickListener {
            val intent = Intent(this, FilterFormActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("isMap", isMappingDisplay)
            startActivity(intent)
        }

        setScaleView(listeBtn,mapBtn)
        binding.listeBtn.setOnClickListener {

           // if (isMappingDisplay) {
                isMappingDisplay = false
                selectedItem(selectedItemPosition)
           // }
            setScaleView(mapBtn,listeBtn)
        }
        binding.mapBtn.setOnClickListener {
            if (!isMappingDisplay) {
                onBackPressed()


                // selectedItem(selectedItemPosition)
            }
            isMappingDisplay = true
            setScaleView(listeBtn,mapBtn)
        }
        if(intent.extras?.get("chat") != null){
              val chatIntent = Intent(this, ChatPageActivity::class.java)
              var body = intent.extras?.get("chat")
            Toast.makeText(this, "be", Toast.LENGTH_LONG)
              if( body is MsgNotification?) {
                  Toast.makeText(this, "bet", Toast.LENGTH_LONG)
                  chatIntent.putExtra("model", body)
                  startActivity(chatIntent)
              }
        }
    }

    fun setScaleView(largeIcon: ImageView, smallView: ImageView) {
        largeIcon.scaleX = 1.2f
        largeIcon.scaleY = 1.2f
        largeIcon.visibility = VISIBLE
      //  smallView.scaleX = 0.6f
       // smallView.scaleY = 0.6f
        smallView.visibility = GONE
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
                isMappingDisplay = true
                displayCategoriesAndFilter(true)
                setScaleView(mapBtn, listeBtn)
            }
            R.id.items_fragments -> {
                bottomAdapter.show(selectedItemPosition)
                isMappingDisplay = false
                displayCategoriesAndFilter(true)
                setScaleView(listeBtn,mapBtn)
            }
            R.id.other_fragment -> {
                bottomAdapter.show(selectedItemPosition)
                isMappingDisplay = false
                displayCategoriesAndFilter(true)
                setScaleView(listeBtn,mapBtn)
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
            MenuAdapter.MenuEnum.others -> {
                isMappingDisplay = false
                //navigation.navigate(R.id.nav_blogs_fragments)
                selectedItem(4)
            }
            MenuAdapter.MenuEnum.medical -> {
                isMappingDisplay = false
                selectedItem(0)
            }
            MenuAdapter.MenuEnum.occasion -> {
                isMappingDisplay = false
                selectedItem(1)
            }
            MenuAdapter.MenuEnum.commerical -> {
                isMappingDisplay = false
                selectedItem(2)
            }
            MenuAdapter.MenuEnum.service -> {
                isMappingDisplay = false
                selectedItem(3)
            }
            MenuAdapter.MenuEnum.subscribe -> startActivity(
                Intent(
                    this,
                    SubscribeActivity::class.java
                )
            )
            MenuAdapter.MenuEnum.profile ->{
                isMappingDisplay = false
                selectedItem(6)
            }


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
        if (pos != 5) // blogs when back >> back to  map on selected position of bottom filter
            selectedItemPosition = pos

        if (!isMappingDisplay) {
            navigation.navigate(R.id.nav_items_fragments)
        }
        when (pos) {
            0 -> {
                type = AppConstants.MEDICAL_STR
                viewModel.typeFlow.value = MedicalType
            }
            1 -> {
                type = AppConstants.OCCASIONAL_STR
                viewModel.typeFlow.value = occasinalType
            }
            2 -> {
                type = AppConstants.COMMERCIAL_STR
                viewModel.typeFlow.value = commericalType
            }
            3 -> {
                type = AppConstants.SERVICE_STR
                viewModel.typeFlow.value = serviceType
            }
            4 -> {
                type = AppConstants.OTHERS_STR
                viewModel.typeFlow.value = otherType
            }
           // 4 -> menuClick(MenuAdapter.MenuEnum.others)

            5 -> menuClick(MenuAdapter.MenuEnum.subscribe)

            6 ->menuClick(MenuAdapter.MenuEnum.profile)
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