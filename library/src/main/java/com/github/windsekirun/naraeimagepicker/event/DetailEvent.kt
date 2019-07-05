package com.github.windsekirun.naraeimagepicker.event

import com.github.windsekirun.naraeimagepicker.item.FileItem

/**
 * NaraeImagePicker
 * Class: DetailEvent
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

data class DetailEvent(val position: Int, val itemList: ArrayList<FileItem>)