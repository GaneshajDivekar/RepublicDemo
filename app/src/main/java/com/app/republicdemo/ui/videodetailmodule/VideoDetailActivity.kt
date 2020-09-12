package com.app.republicdemo.ui.videodetailmodule

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import com.app.republicdemo.R
import com.app.republicdemo.data.model.MediaItem
import com.app.republicdemo.databinding.ActivityVideoDetailBinding
import com.app.republicdemo.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_video_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class VideoDetailActivity : BaseActivity<ActivityVideoDetailBinding, VideoDetailViewModel>(),
    VideoDetailNavigator, PopupMenu.OnMenuItemClickListener {
    var activityVideoDetailBinding: ActivityVideoDetailBinding? = null
    val videoDetailViewModel: VideoDetailViewModel by viewModel()
    var mediaItem: MediaItem? = null
    var videoSample: String = ""
    private var mCurrentPosition = 0
    private val PLAYBACK_TIME = "play_time"
    var mediaControls: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityVideoDetailBinding = getViewDataBinding()


        mediaItem = intent.getParcelableExtra("mediaItem")
        videoSample = mediaItem?.url.toString()
        val url = mediaItem?.url.toString()
        val paths: Array<String> = url.split("?").toTypedArray()
        videoSample = paths[0]


        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME)
        }
        if (mediaControls == null) {
            mediaControls = MediaController(this)
            mediaControls?.setAnchorView(activityVideoDetailBinding?.videoview);
        }
        mediaControls?.setMediaPlayer(activityVideoDetailBinding?.videoview)
        activityVideoDetailBinding?.videoview?.setMediaController(mediaControls)

        activityVideoDetailBinding?.videoview?.setOnErrorListener(object :
            MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                Log.d("video", "setOnErrorListener ");
                return true;
            }

        })

        activityVideoDetailBinding?.videoview?.setOnPreparedListener(OnPreparedListener { activityVideoDetailBinding?.videoview?.start() })

        activityVideoDetailBinding?.imgOverflow?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                clickOnOverflow(v!!)
            }

        })
        activityVideoDetailBinding?.imgBack?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onBackPressed()
            }

        })
        activityVideoDetailBinding?.imgFullScreen?.setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View?) {
                val metrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(metrics)
                val params =
                    activityVideoDetailBinding?.videoview?.getLayoutParams()
                params!!.width = (800*metrics.density.toInt())
                params!!.height = (700*metrics.density.toInt())
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
               activityVideoDetailBinding?.videoview?.setLayoutParams(params)

            }

        })


    }


    override fun getLayoutId(): Int {
        return R.layout.activity_video_detail
    }

    override fun getViewModel(): VideoDetailViewModel {
        return videoDetailViewModel
    }

    override fun setUp(savedInstanceState: Bundle?) {

    }

    override fun onPause() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pictureInPictureMode()
        } else {

            activityVideoDetailBinding!!.videoview!!.pause()
        }
        super.onPause()


    }


    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        if (isInPictureInPictureMode) {

        } else {

        }
    }


    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PLAYBACK_TIME, activityVideoDetailBinding?.videoview!!.currentPosition)
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pictureInPictureMode()
        } else {
            super.onBackPressed()
        }

    }


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun pictureInPictureMode() {
        // Calculate the aspect ratio of the PiP screen.
        val aspectRatio = Rational(videoview.getWidth(), videoview.getHeight())
        val mPictureInPictureParamsBuilder = PictureInPictureParams.Builder()
        mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio)
        enterPictureInPictureMode(mPictureInPictureParamsBuilder.build())
    }


    private fun initializePlayer() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val params =
            activityVideoDetailBinding?.videoview?.getLayoutParams()
        params!!.width = metrics.widthPixels
        params!!.height = metrics.heightPixels
        activityVideoDetailBinding?.videoview?.setLayoutParams(params)
        activityVideoDetailBinding?.buffering?.visibility = VideoView.VISIBLE
        val videoUri: Uri? = getMedia(videoSample)
        activityVideoDetailBinding?.videoview?.setVideoURI(videoUri)


        activityVideoDetailBinding?.videoview?.setOnPreparedListener { // Hide buffering message.
            activityVideoDetailBinding?.buffering?.visibility = VideoView.INVISIBLE

            if (mCurrentPosition > 0) {
                activityVideoDetailBinding?.videoview?.seekTo(mCurrentPosition)
            } else {
                activityVideoDetailBinding?.videoview?.seekTo(1)
            }
            activityVideoDetailBinding?.videoview?.start()
        }



        activityVideoDetailBinding?.videoview?.setOnCompletionListener {
            Toast.makeText(
                this@VideoDetailActivity,
                "toast message",
                Toast.LENGTH_SHORT
            ).show()

            activityVideoDetailBinding!!.videoview.seekTo(0)
        }
    }

    private fun releasePlayer() {
        activityVideoDetailBinding?.videoview?.stopPlayback()
    }

    private fun getMedia(mediaName: String): Uri? {
        return Uri.parse(mediaName)
    }


    override fun clickOnOverflow(v: View) {
        val popup = PopupMenu(this@VideoDetailActivity, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        Toast.makeText(this, "Selected Item: " + item?.getTitle(), Toast.LENGTH_SHORT).show();
        when (item?.itemId) {
            R.id.first ->
                Toast.makeText(this, "You set resolution 144p", Toast.LENGTH_SHORT).show()
            R.id.second ->
                Toast.makeText(this, "You set resolution 240p", Toast.LENGTH_SHORT).show()
            R.id.third ->
                Toast.makeText(this, "You set resolution 360p", Toast.LENGTH_SHORT).show()
            R.id.fourth ->
                Toast.makeText(this, "You set resolution 480p", Toast.LENGTH_SHORT).show()
            R.id.fifth -> {
                Toast.makeText(this, "You set resolution 720p", Toast.LENGTH_SHORT).show()
            }
            R.id.sixth -> {
                Toast.makeText(this, "You set resolution Auto", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }


}
