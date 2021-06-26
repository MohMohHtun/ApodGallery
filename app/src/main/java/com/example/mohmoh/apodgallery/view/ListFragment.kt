package com.example.mohmoh.apodgallery.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mohmoh.apodgallery.R
import com.example.mohmoh.apodgallery.viewmodel.ListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_first.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    var last30Days = "2021-05-29";

    val MONTHS = arrayOf<String>("01","02","03", "04","05","06","07","08","09","10", "11", "12")

    private lateinit var viewModel: ListViewModel
    private val apodListAdapter = ApodListAdapter(arrayListOf())

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val date = Calendar.getInstance()
            date.add(Calendar.DATE, -30)
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            last30Days = sdf.format(date.getTime())
        }catch (e : Exception){

        }

        fab.setOnClickListener { view ->
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)



            val dpd =
                activity?.let {
                    DatePickerDialog(it, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                        // Display Selected date in textbox
                        val newStrDate:String = "" + year + "-" + MONTHS[monthOfYear] + "-" +dayOfMonth
                        val newDate: Date = SimpleDateFormat("yyyy-MM-dd").parse(newStrDate)
//                        if (newDate.before(SimpleDateFormat("yyyy-MM-dd").parse(last30Days))){
                            viewModel.fetchApod(newStrDate)
//                        }else{
//                            val dialogBuilder = AlertDialog.Builder(context)
//
//                            // set message of alert dialog
//                            dialogBuilder.setMessage("Please Choose A date before" + last30Days)
//                                // if the dialog is cancelable
//                                .setCancelable(false)
//                                // positive button text and action
//                                .setPositiveButton("Ok", DialogInterface.OnClickListener {
//                                        dialog, id -> dialog.cancel()
//                                })
//
//                            // create dialog box
//                            val alert = dialogBuilder.create()
//                            // set title for alert dialog box
//                            alert.setTitle("Date Must be before"+last30Days)
//                            // show alert dialog
//                            alert.show()
//                        }


                    }, year, month, day)
                }
            dpd!!.datePicker.maxDate = c.timeInMillis

            if (dpd != null) {
                dpd.show()
            }
        }

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh(last30Days)

        listApod.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = apodListAdapter
        }

        refreshLayout.setOnRefreshListener {
            listApod.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.fetchApods(last30Days)
            refreshLayout.isRefreshing = false
        }

        obseraveViewModel()
    }

    @SuppressLint("FragmentLiveDataObserve")
    fun obseraveViewModel(){
        viewModel.apods.observe(this, Observer {
            it?.let {
                listApod.visibility = View.VISIBLE
                apodListAdapter.updateApodList(it)
            }
        })

        viewModel.apodLoadError.observe(this, Observer {
            it?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer {
            it?.let {
                loadingView.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    listError.visibility = View.GONE
                    listApod.visibility = View.GONE
                }
            }
        })
    }
}