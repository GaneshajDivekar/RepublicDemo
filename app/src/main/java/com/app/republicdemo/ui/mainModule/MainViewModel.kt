package com.app.republicdemo.ui.mainModule

import android.app.Application
import androidx.lifecycle.LiveData
import com.app.republicdemo.data.model.MediaItem
import com.app.republicdemo.data.model.MediaItemModel
import com.app.republicdemo.presentation.base.BaseViewModel

class MainViewModel(application: Application) : BaseViewModel(application) {
    fun parseData(): LiveData<List<MediaItem>> {
        return MainRespository.getInstance(getApplication()).getAllMediaFileData()
    }
}
