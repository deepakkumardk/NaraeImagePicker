package com.github.windsekirun.naraeimagepicker.impl

import android.view.View
import com.github.windsekirun.naraeimagepicker.item.FileItem

/**
 * NaraeImagePicker
 * Class: ImagePreviewFragment
 * Created by Deepak Kumar on 1st July 2019
 *
 * Description: Listener for the ImagePreviewAdapter
 */

interface OnImageSelected {
    fun onClick(view:View,fileItem: FileItem)
}