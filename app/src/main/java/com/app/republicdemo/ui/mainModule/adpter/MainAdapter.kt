package com.app.republicdemo.ui.mainModule.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.republicdemo.R
import com.app.republicdemo.data.model.MediaItem
import com.app.republicdemo.ui.mainModule.MainNavigator
import com.app.republicdemo.utils.GlideApp
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.main_adapter_item_view.view.*
import java.util.*


class MainAdapter(
    var context: Context,
    var customerDetailList: List<MediaItem>,
    var userItemClickListner: MainNavigator
) : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView? = null
        var txtTitle: TextView? = null
        var txtDuration: TextView? = null

        init {
            imageView = itemView.findViewById(R.id.imgView)
            txtTitle = itemView.findViewById(R.id.txtTitle)
            txtDuration = itemView.findViewById(R.id.txtDuration)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainAdapter.MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.main_adapter_item_view, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return customerDetailList.size
    }

    override fun onBindViewHolder(holder: MainAdapter.MyViewHolder, position: Int) {
        var customerDao = customerDetailList.get(position)
        holder.itemView.txtTitle.text = customerDao.title;
        holder.itemView.txtDuration.text = customerDao.duration
        GlideApp.with(context).load(customerDao.image).apply(RequestOptions.circleCropTransform()).into(holder.itemView.imgView

        )

      holder.itemView?.setOnClickListener(object : View.OnClickListener {
          override fun onClick(v: View?) {
              userItemClickListner.clickOnItemView(customerDao)
          }

      })

    }



}