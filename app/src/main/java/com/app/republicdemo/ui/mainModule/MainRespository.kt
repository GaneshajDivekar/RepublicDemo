package com.app.republicdemo.ui.mainModule

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.republicdemo.data.model.MediaItem
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream


class MainRespository {


    companion object {

        var mainRespository: MainRespository? = null
        var mContext: Application? = null


        @Synchronized
        @JvmStatic
        fun getInstance(context: Application): MainRespository {
            mContext = context
            if (mainRespository == null) mainRespository = MainRespository()
            return mainRespository!!
        }


    }

    fun getAllMediaFileData(): LiveData<List<MediaItem>> {
        val apicalltime = SystemClock.elapsedRealtime()
        var allitemData = MutableLiveData<List<MediaItem>>()




        try {
            val obj = JSONObject(loadJSONFromAsset())
            val m_jArry = obj.getJSONArray("MediaItems")
            var mediaItemListModel: MediaItem
            val list = ArrayList<MediaItem>()
            for (i in 0 until m_jArry.length()) {
                val jo_inside = m_jArry.getJSONObject(i)
                val title_value = jo_inside.getString("Title")
                val image_value = jo_inside.getString("Image")
                val url_value = jo_inside.getString("Url")
                val durtion_value = jo_inside.getString("Duration")
                mediaItemListModel = MediaItem()
                mediaItemListModel.title = title_value
                mediaItemListModel.image = image_value
                mediaItemListModel.url = url_value
                mediaItemListModel.duration = durtion_value
                list.add(mediaItemListModel)

            }

            allitemData.value = list
        } catch (e: JSONException) {
            e.printStackTrace()
        }






        return allitemData

    }

    fun loadJSONFromAsset(): String? {
        var json: String? = null
        json = try {
            val data: InputStream = mContext!!.getAssets().open("media_item.json")
            val size: Int = data.available()
            val buffer = ByteArray(size)
            data.read(buffer)
            data.close()
            String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }


}
