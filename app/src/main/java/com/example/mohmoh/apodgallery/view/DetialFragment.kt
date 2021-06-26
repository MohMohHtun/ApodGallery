package com.example.mohmoh.apodgallery.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mohmoh.apodgallery.R
import com.example.mohmoh.apodgallery.databinding.FragmentDetailBinding
import com.example.mohmoh.apodgallery.model.ApodPlatte
import com.example.mohmoh.apodgallery.util.getProgressDrawable
import com.example.mohmoh.apodgallery.util.loadImage
import com.example.mohmoh.apodgallery.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.android.synthetic.main.item_apod.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetialFragment : Fragment() {

    private lateinit var dataBinding: FragmentDetailBinding

    private lateinit var viewModel: DetailViewModel
    val sdf = SimpleDateFormat("yyyy-mm-dd")
    private var dateApod = sdf.format(Date())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.toolBar) as Toolbar

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            activity?.onBackPressed()
        })

        arguments?.let {
            dateApod = DetialFragmentArgs.fromBundle(it).dateArg
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch(dateApod)

        observeViewModel()
    }


    @SuppressLint("FragmentLiveDataObserve")
    fun observeViewModel(){
        viewModel.apodLiveData.observe(this, Observer {
            it?.let {
//                title.text = it.title
//                date.text = it.dateApod
//                explanation.text = it.explanation

                dataBinding.apod = it
                it.url?.let { it1 ->
                    image.loadImage(it1, getProgressDrawable(image.context))
                    setUpBackgroundColor(it1)
                }

            }
        })
    }

    private fun setUpBackgroundColor(url: String){
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate {
                            val intColor = it?.vibrantSwatch?.rgb ?: 0
                            val myPalette = ApodPlatte(intColor)
                            dataBinding.palette = myPalette
                        }
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
    }



}