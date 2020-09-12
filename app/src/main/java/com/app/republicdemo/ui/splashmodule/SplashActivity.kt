package com.app.republicdemo.ui.splashmodule

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.app.republicdemo.R
import com.app.republicdemo.databinding.ActivitySplashBinding
import com.app.republicdemo.presentation.base.BaseActivity
import com.app.republicdemo.ui.mainModule.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashNavigator {
    val splashViewModel: SplashViewModel by viewModel()
    var activitySplashBinding: ActivitySplashBinding? = null
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler()
        handler!!.postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)


    }


    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun getViewModel(): SplashViewModel {
        return splashViewModel
    }

    override fun setUp(savedInstanceState: Bundle?) {
        activitySplashBinding = getViewDataBinding()
    }
}