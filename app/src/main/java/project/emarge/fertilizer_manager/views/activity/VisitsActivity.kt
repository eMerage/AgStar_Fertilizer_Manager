package project.emarge.fertilizer_manager.views.activity

import android.Manifest
import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.pddstudio.preferences.encrypted.EncryptedPreferences
import kotlinx.android.synthetic.main.activity_visits.*
import kotlinx.android.synthetic.main.dialog_visits_filter.*
import kotlinx.android.synthetic.main.nav_header_visits.view.*

import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.databinding.ActivityVisitsBinding
import project.emarge.fertilizer_manager.model.datamodel.Visits
import project.emarge.fertilizer_manager.viewModels.visits.VisitsViewModel
import project.emarge.fertilizer_manager.views.adaptor.visits.VisitsAdaptor
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VisitsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var bindingVisits: ActivityVisitsBinding


    private val USER_NAME = "userName"
    private val USER_IMAGE = "userImage"
    val READ_STORAGE_PERMISSION_REQUEST = 701
    private var filterDateStart = ""
    private var filterDateEnd = ""

    private lateinit var dialogVisitsFilter: Dialog

    var list = ArrayList<Visits>()

    lateinit var encryptedPreferences: EncryptedPreferences
    private val USER_REMEMBER = "userRemember"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        bindingVisits = DataBindingUtil.setContentView<ActivityVisitsBinding>(this, R.layout.activity_visits)
        bindingVisits.visits = ViewModelProviders.of(this).get(VisitsViewModel::class.java)


        encryptedPreferences =EncryptedPreferences.Builder(application).withEncryptionPassword("122547895511").build()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)


        var encryptedPreferences: EncryptedPreferences =
            EncryptedPreferences.Builder(this).withEncryptionPassword("122547895511").build()


        var heview = navView.getHeaderView(0)
        heview.textView_navi_visits.text = encryptedPreferences.getString("userName", "")



        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequestStorage()
        } else {
            bindingVisits.visits!!.uploadeMissingImages()
        }




        bindingVisits.visits!!.getVisitsFromServer().observe(this, Observer<ArrayList<Visits>> {
            it?.let { result ->
                list = result
                if(result.isEmpty()){
                    textview_novisits.visibility = View.VISIBLE
                    textview_novisits.text = "No Visits available"
                }else{
                    textview_novisits.visibility = View.GONE
                }
                recyclerView_visits.adapter = VisitsAdaptor(result, this)
            }
        })


    }


    private fun openDialogVisitsFilter() {
        dialogVisitsFilter = Dialog(this)
        dialogVisitsFilter.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogVisitsFilter.window!!.setBackgroundDrawableResource(android.R.color.white)
        dialogVisitsFilter.setContentView(R.layout.dialog_visits_filter)
        dialogVisitsFilter.setCancelable(true)

        val calendarView = dialogVisitsFilter.findViewById(R.id.calendarView) as DateRangeCalendarView


        var date = "Dates :"
        date = if (filterDateStart.isEmpty()) {
            "Date Not Selected"
        } else if (filterDateEnd.isEmpty()) {
            "$date $filterDateStart"
        } else {
            "$date $filterDateStart To $filterDateEnd"
        }
        var targetFormat = SimpleDateFormat("yyyy-MM-dd")


        lateinit var dateS: Date
        lateinit var dateE: Date
        try {
            dateS = targetFormat.parse(filterDateStart)
            dateE = targetFormat.parse(filterDateEnd)
        } catch (e: ParseException) {
            e.printStackTrace()
        }



        when {
            filterDateStart.isEmpty() -> {
            }
            filterDateEnd.isEmpty() -> {
                val startSelectionDate = Calendar.getInstance()
                startSelectionDate.time = dateS
                calendarView.setSelectedDateRange(startSelectionDate, startSelectionDate)
            }
            else -> {
                val startSelectionDate = Calendar.getInstance()
                startSelectionDate.time = dateS
                val endSelectionDate = startSelectionDate.clone() as Calendar
                endSelectionDate.time = dateE
                calendarView.setSelectedDateRange(startSelectionDate, endSelectionDate)
            }
        }
        calendarView.setCalendarListener(object : DateRangeCalendarView.CalendarListener {
            override fun onFirstDateSelected(startDate: Calendar) {
                filterDateStart = targetFormat.format(startDate.time)
            }

            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                filterDateStart = targetFormat.format(startDate.time)
                filterDateEnd = targetFormat.format(endDate.time)
            }
        })

        dialogVisitsFilter.button_dialog_visits_filter.setOnClickListener {
            bindingVisits.visits!!.getVisitsByFilter(filterDateStart, filterDateEnd, list)
                .observe(this, Observer<ArrayList<Visits>> {
                    it?.let { result ->
                        if (result.isEmpty()) {
                            textview_novisits.visibility = View.VISIBLE
                        } else {
                            textview_novisits.visibility = View.GONE
                        }

                        dialogVisitsFilter.dismiss()
                        recyclerView_visits.adapter = VisitsAdaptor(result, this)
                    }
                })


        }

        dialogVisitsFilter.button_dialog_visits_resetfilter.setOnClickListener {
            dialogVisitsFilter.dismiss()
            if (list.isEmpty()) {
                textview_novisits.visibility = View.VISIBLE
            } else {
                textview_novisits.visibility = View.GONE
            }
            recyclerView_visits.adapter = VisitsAdaptor(list, this)
        }


        dialogVisitsFilter.show()

    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Exit!")
            alertDialogBuilder.setMessage("Do you really want to exit ?")
            alertDialogBuilder.setPositiveButton(
                "YES"
            ) { _, _ -> super.onBackPressed() }
            alertDialogBuilder.setNegativeButton(
                "NO",
                DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
            alertDialogBuilder.show()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_visits -> {
                // Handle the camera action
            }

            R.id.nav_dealer -> {
                val intent = Intent(this, DealerActivity::class.java)
                val bndlanimation =
                    ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out).toBundle()
                startActivity(intent, bndlanimation)
                super.onBackPressed()
            }
            R.id.nav_product -> {
                val intent = Intent(this, ProductsActivity::class.java)
                val bndlanimation =
                    ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out).toBundle()
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
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.visits_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_filter -> {
                if (list.isNotEmpty()) {
                    openDialogVisitsFilter()
                } else {

                    Toast.makeText(this, "no visits loaded yet", Toast.LENGTH_LONG).show()
                }

                return true
            }

        }
        return super.onOptionsItemSelected(item)

    }


    private fun makeRequestStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_REQUEST
            )
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            READ_STORAGE_PERMISSION_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    bindingVisits.visits!!.uploadeMissingImages()
                } else {
                    Toast.makeText(this, "Oops! Permission Denied!!", Toast.LENGTH_SHORT).show()
                }
                return
            }

        }
    }
}
