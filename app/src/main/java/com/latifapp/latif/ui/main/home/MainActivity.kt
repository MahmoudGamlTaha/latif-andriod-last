package com.latifapp.latif.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.latifapp.latif.R
import com.latifapp.latif.databinding.ActivityMainBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.filter.FilterFormActivity
import com.latifapp.latif.ui.main.profile.ProfileActivity
import com.latifapp.latif.ui.subscribe.SubscribeActivity
import com.latifapp.latif.utiles.AppConstants
import com.latifapp.latif.utiles.AppConstants.PETS_STR
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), NavController.OnDestinationChangedListener,
    MenuAdapter.MenuAction, BottomNavItemsAdapter.Action {
    private val bottomAdapter=BottomNavItemsAdapter(this@MainActivity)
    private lateinit var navigation: NavController
    public lateinit var searchBtn:ImageView
    public lateinit var searchView:SearchView
    public lateinit var toolBarTitle:TextView
    private var type=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBtn=binding.toolbar.searchBtn
        searchView=binding.toolbar.searchView
        toolBarTitle=binding.toolbar.title

         navigation = Navigation.findNavController(
             this,
             R.id.fragment_container
         )


        setBottomBarNav()
        navigation.addOnDestinationChangedListener(this)
        setMenu()
         searchBtn.setOnClickListener {
            val intent =Intent(this, FilterFormActivity::class.java)
            intent.putExtra("type", type)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        val id = navigation.currentDestination?.id
        navigation.popBackStack(id!!,true)
        navigation.navigate(id)

    }
    private fun setBottomBarNav() {
        binding.bottomNavRecyclerView.apply {
            layoutManager=GridLayoutManager(this@MainActivity, 5)
            adapter=bottomAdapter
        }
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
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        searchView.visibility= GONE
        searchBtn.visibility= VISIBLE
        binding.toolbar.titleContainer.visibility=VISIBLE
        when (destination.id) {

            R.id.pets_fragments -> {
                bottomAdapter.show(0)
                type = PETS_STR
            }
            R.id.items_fragments -> {
                bottomAdapter.show(1)
                type = AppConstants.ACCESSORIES_STR
            }
            R.id.clinic_fragments -> {
                bottomAdapter.show(2)
                type = AppConstants.PET_CARE_STR
            }
            R.id.services_fragments -> {
                bottomAdapter.show(3)
                type = AppConstants.SERVICE_STR
            }
            R.id.chat_fragments -> {
                bottomAdapter.show(4)
                searchBtn.visibility = GONE
            }
            R.id.blogs_fragments -> {
                searchView.visibility = VISIBLE
                searchBtn.visibility = GONE
                binding.toolbar.titleContainer.visibility = GONE
            }
        }
    }

    override fun menuClick(enum: MenuAdapter.MenuEnum) {
        when (enum) {
            MenuAdapter.MenuEnum.profile -> startActivity(
                Intent(this, ProfileActivity::class.java)
            )
            MenuAdapter.MenuEnum.blogs -> navigation.navigate(R.id.nav_blogs_fragments)
            MenuAdapter.MenuEnum.pets -> navigation.navigate(R.id.nav_pets_list_fragments)
            MenuAdapter.MenuEnum.items -> navigation.navigate(R.id.nav_items_fragments)
            MenuAdapter.MenuEnum.service -> navigation.navigate(R.id.nav_services_fragments)
            MenuAdapter.MenuEnum.subscribe -> startActivity(Intent(this,SubscribeActivity::class.java))
         }

        runBlocking {
            binding.drawerLayout.closeDrawers()
        }

    }

    override fun selectedItem(pos: Int) {
         when(pos){
             0 -> navigation.navigate(R.id.nav_pets_fragments)
             1 -> navigation.navigate(R.id.nav_items_fragments)
             2 -> navigation.navigate(R.id.nav_clinic_fragments)
             3 -> navigation.navigate(R.id.nav_services_fragments)
             4 -> navigation.navigate(R.id.nav_chat_fragments)
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
            Utiles.log_D("cncnncncncnncn","${intent.data?.query}  ${intent.data?.encodedQuery}")
        }
    }
}