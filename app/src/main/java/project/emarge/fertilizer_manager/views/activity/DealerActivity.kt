package project.emarge.fertilizer_manager.views.activity

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.pddstudio.preferences.encrypted.EncryptedPreferences
import kotlinx.android.synthetic.main.activity_dealer.*
import kotlinx.android.synthetic.main.nav_header_visits.view.*
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.views.adaptor.dealer.DealerPagerAdapter


class DealerActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {



    lateinit var encryptedPreferences: EncryptedPreferences
    private val USER_REMEMBER = "userRemember"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer)
        setSupportActionBar(toolbar_dealer)



        encryptedPreferences =EncryptedPreferences.Builder(application).withEncryptionPassword("122547895511").build()


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout_dealer, toolbar_dealer, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout_dealer.addDrawerListener(toggle)
        toggle.syncState()


        nav_dealer.setNavigationItemSelectedListener(this)



        var encryptedPreferences: EncryptedPreferences = EncryptedPreferences.Builder(this).withEncryptionPassword("122547895511").build()


        var heview= nav_dealer.getHeaderView(0)
        heview.textView_navi_visits.text = encryptedPreferences.getString("userName","")


        val sectionsPagerAdapter =
            DealerPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)





    }

    override fun onDestroy() {
        super.onDestroy()

    }



    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_dealer)
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
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_visits -> {
                val intent = Intent(this, VisitsActivity::class.java)
                val bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out).toBundle()
                startActivity(intent, bndlanimation)
                super.onBackPressed()
            }R.id.nav_dealer -> { }
            R.id.nav_product -> {
                val intent = Intent(this, ProductsActivity::class.java)
                val bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out).toBundle()
                startActivity(intent, bndlanimation)
                super.onBackPressed()
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

        drawer_layout_dealer.closeDrawer(GravityCompat.START)
        return true
    }
}
