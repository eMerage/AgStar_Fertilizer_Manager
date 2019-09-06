package project.emarge.fertilizer_manager.views.activity

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.pddstudio.preferences.encrypted.EncryptedPreferences
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.nav_header_visits.view.*
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.databinding.ActivityProductsBinding
import project.emarge.fertilizer_manager.viewModels.products.ProductsViewModel
import project.emarge.fertilizer_manager.views.adaptor.product.ProductsPagerAdapter


class ProductsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {



    lateinit var bindingProducts: ActivityProductsBinding

    lateinit var encryptedPreferences: EncryptedPreferences
    private val USER_REMEMBER = "userRemember"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        bindingProducts = DataBindingUtil.setContentView<ActivityProductsBinding>(this, R.layout.activity_products)
        bindingProducts.products = ViewModelProviders.of(this).get(ProductsViewModel::class.java)


        encryptedPreferences =EncryptedPreferences.Builder(application).withEncryptionPassword("122547895511").build()

        val toolbar: Toolbar = findViewById(R.id.toolbar_products)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_products)
        val navView: NavigationView = findViewById(R.id.nav_view_products)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)



        var encryptedPreferences: EncryptedPreferences = EncryptedPreferences.Builder(this).withEncryptionPassword("122547895511").build()


        var heview= navView.getHeaderView(0)
        heview.textView_navi_visits.text = encryptedPreferences.getString("userName","")


        val sectionsPagerAdapter = ProductsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs_products.setupWithViewPager(view_pager)


    }



    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_products)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Exit!")
            alertDialogBuilder.setMessage("Do you really want to exit ?")
            alertDialogBuilder.setPositiveButton("YES"
            ) { _, _ -> super.onBackPressed() }
            alertDialogBuilder.setNegativeButton("NO", DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
            alertDialogBuilder.show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_visits -> {
                val intent = Intent(this, VisitsActivity::class.java)
                val bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out).toBundle()
                startActivity(intent, bndlanimation)
                super.onBackPressed()
            }

            R.id.nav_dealer -> {
                val intent = Intent(this, DealerActivity::class.java)
                val bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out).toBundle()
                startActivity(intent, bndlanimation)
                super.onBackPressed()
            }
            R.id.nav_product -> {
                // Handle the camera action
            }
            R.id.nav_logout -> {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Logout!")
                alertDialogBuilder.setMessage("Do you really want to Logout ?")
                alertDialogBuilder.setPositiveButton("YES"
                ) { _, _ ->

                    encryptedPreferences.edit().putBoolean(USER_REMEMBER, false).apply()
                    val intent = Intent(this, LoginActivity::class.java)
                    val bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out).toBundle()
                    startActivity(intent, bndlanimation)
                    super.onBackPressed()

                }
                alertDialogBuilder.setNegativeButton("NO", DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
                alertDialogBuilder.show()
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_products)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
