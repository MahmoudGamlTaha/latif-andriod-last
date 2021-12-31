package com.latifapp.latif.ui.splashimport android.content.Intentimport android.os.Bundleimport android.os.Handlerimport android.os.Looperimport android.view.LayoutInflaterimport android.view.WindowManagerimport androidx.activity.viewModelsimport androidx.appcompat.app.AppCompatActivityimport com.fujiyuu75.sequent.Animationimport com.fujiyuu75.sequent.Directionimport com.fujiyuu75.sequent.Sequentimport com.latifapp.latif.Rimport com.latifapp.latif.databinding.ActivitySplashBindingimport com.latifapp.latif.ui.base.BaseActivityimport com.latifapp.latif.ui.main.home.MainActivityimport com.latifapp.latif.ui.sell.SellViewModelimport com.latifapp.latif.utiles.AppConstantsimport com.latifapp.latif.utiles.GpsUtilsimport com.latifapp.latif.utiles.Permissionsimport com.latifapp.latif.utiles.Utilesimport dagger.hilt.android.AndroidEntryPoint@AndroidEntryPointclass SplashActivity : BaseActivity<SplashViewModel,ActivitySplashBinding>() {    override val viewModel by viewModels<SplashViewModel>()    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        Utiles.setLocalization(this, lang)        window.setFlags(            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS        )        Sequent            .origin(findViewById(R.id.logo))            .duration(1500) // option.            .flow(Direction.RANDOM) // option. Flow of animations in (FORWARD/BACKWARD/RANDOM).            .anim(this, Animation.BOUNCE_IN)            .start()        turnGPSOn()        // intent    }    private fun intentToNextPage() {        Handler(Looper.getMainLooper()).postDelayed({            /* Create an Intent that will start the Menu-Activity. */            startActivity(Intent(this@SplashActivity, MainActivity::class.java))            finish()        }, 3000)    }    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {        super.onActivityResult(requestCode, resultCode, data)         turnGPSOn()    }    override fun onRequestPermissionsResult(        requestCode: Int,        permissions: Array<out String>,        grantResults: IntArray    ) {        super.onRequestPermissionsResult(requestCode, permissions, grantResults)        turnGPSOn()    }    private fun turnGPSOn() {        if (!Permissions.checkLocationPermissions(this)) {            Permissions.showPermissionsDialog(                this,                "Request Location permission Is Needed",                Permissions.locationManifestPermissionsList,                0            )        }/*else if (!Permissions.checkCameraPermissions(this)) {            Permissions.showPermissionsDialog(                this,                "External Storage  Permission Is Needed",                Permissions.cameraManifestPermissionsList,                Permissions.galleryRequest            )        }*/ else            GpsUtils(this).turnGPSOn { isGPSEnable, mlocation -> // turn on GPS                if (isGPSEnable) intentToNextPage()            }    }    override fun setBindingView(inflater: LayoutInflater): ActivitySplashBinding {        return ActivitySplashBinding.inflate(inflater)    }    override fun showLoader() {    }    override fun hideLoader() {    }}