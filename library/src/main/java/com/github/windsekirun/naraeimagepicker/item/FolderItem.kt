package com.github.windsekirun.naraeimagepicker.item

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * NaraeImagePicker
 * Class: FolderItem
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */
@Parcelize
data class FolderItem(val name: String, val imagePath: String) : Parcelable