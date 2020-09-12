package com.app.republicdemo.ui.mainModule


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.republicdemo.R
import com.app.republicdemo.data.model.MediaItem
import com.app.republicdemo.databinding.ActivityMainBinding
import com.app.republicdemo.presentation.base.BaseActivity
import com.app.republicdemo.ui.mainModule.adpter.MainAdapter
import com.app.republicdemo.ui.videodetailmodule.VideoDetailActivity
import logi.retail.utils.DialogUtils
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {

    var activityMainBinding: ActivityMainBinding? = null
    val mainViewModel: MainViewModel by viewModel()
    var mainAdapter: MainAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        return mainViewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = getViewDataBinding()
        DialogUtils.startProgressDialog(this@MainActivity)

        mainViewModel.parseData().observe(this, Observer { allMediaItemList ->
            if (allMediaItemList != null) {
                DataSetToRecyclerView(allMediaItemList)
            } else {
                Toast.makeText(applicationContext,"Please try again",Toast.LENGTH_SHORT).show()

            }
        })


    }

    private fun DataSetToRecyclerView(allMediaItemList: List<MediaItem>) {
        Log.e("MainActivity Data", allMediaItemList.size.toString())
        DialogUtils.stopProgressDialog()

        mainAdapter = MainAdapter(
            this@MainActivity, allMediaItemList,
            this
        )
        val mLayoutManager = LinearLayoutManager(applicationContext)
        activityMainBinding!!.recyclerViewMediaList.setLayoutManager(mLayoutManager)
        activityMainBinding!!.recyclerViewMediaList.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        activityMainBinding!!.recyclerViewMediaList.setItemAnimator(DefaultItemAnimator())

        activityMainBinding!!.recyclerViewMediaList.setAdapter(mainAdapter)


    }

    override fun setUp(savedInstanceState: Bundle?) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun clickOnItemView(mediaItem: MediaItem) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var intent = Intent(this@MainActivity, VideoDetailActivity::class.java)
            intent.putExtra("mediaItem", mediaItem)
            startActivity(intent)
        } else {
            Toast.makeText(this@MainActivity,"PIP mode not supported",Toast.LENGTH_SHORT).show()
        }


    }


}









