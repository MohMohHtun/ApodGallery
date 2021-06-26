package com.example.mohmoh.apodgallery.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mohmoh.apodgallery.R
import com.example.mohmoh.apodgallery.databinding.ItemApodBinding
import com.example.mohmoh.apodgallery.model.Apod
import com.example.mohmoh.apodgallery.util.getProgressDrawable
import com.example.mohmoh.apodgallery.util.loadImage
import kotlinx.android.synthetic.main.fragment_second.view.*
import kotlinx.android.synthetic.main.fragment_second.view.date
import kotlinx.android.synthetic.main.fragment_second.view.title
import kotlinx.android.synthetic.main.item_apod.view.*

class ApodListAdapter(val apodList: ArrayList<Apod>): RecyclerView.Adapter<ApodListAdapter.ApodViewHolder>(), ApodClickListener{


    fun updateApodList(newApodList : List<Apod>){
        apodList.clear()
        apodList.addAll(newApodList)
        notifyDataSetChanged()
    }

    class ApodViewHolder(var view: ItemApodBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApodViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.item_apod, parent, false)
        val view: ItemApodBinding = DataBindingUtil.inflate<ItemApodBinding>(inflater, R.layout.item_apod, parent, false)
        return  ApodViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApodViewHolder, position: Int) {
        /*
        No Binding
         */

//        holder.view.title.text = apodList[position].title
//        holder.view.date.text = apodList[position].dateApod
//        holder.view.imageView.loadImage(apodList[position].url, getProgressDrawable(holder.view.imageView.context))

//        holder.view.setOnClickListener {
//            val action: FirstFragmentDirections.ActionFirstFragmentToSecondFragment = FirstFragmentDirections.actionFirstFragmentToSecondFragment()
//
//            action.dateArg = apodList[position].dateApod
//
//            Navigation.findNavController(it).navigate(action)
//        }


        /***
         * Binding
         */
        holder.view.apod = apodList[position]
        holder.view.listener = this
    }

    override fun onApodClicked(v: View) {
        val date_arg = v.apodId.text.toString()
        val action: ListFragmentDirections.ActionFirstFragmentToSecondFragment = ListFragmentDirections.actionFirstFragmentToSecondFragment()

        action.dateArg = date_arg
        Navigation.findNavController(v).navigate(action)
    }

    override fun getItemCount(): Int {
        return apodList.size
    }
}