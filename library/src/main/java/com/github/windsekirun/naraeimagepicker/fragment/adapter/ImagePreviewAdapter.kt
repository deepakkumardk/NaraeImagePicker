package com.github.windsekirun.naraeimagepicker.fragment.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.github.windsekirun.naraeimagepicker.fragment.ImagePreviewFragment
import com.github.windsekirun.naraeimagepicker.impl.OnImageSelected
import com.github.windsekirun.naraeimagepicker.item.FileItem

/**
 * NaraeImagePicker
 * Class: ImagePreviewFragment
 * Created by Deepak Kumar on 1st July 2019
 *
 * Description: Adapter for the full image preview
 */

class ImagePreviewAdapter(fm: FragmentManager?,
                          private val itemList: ArrayList<FileItem>,
                          private val listener: OnImageSelected) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return ImagePreviewFragment.ImagePreviewObject.newInstance(itemList[position], position, listener)
    }

    override fun getCount(): Int = itemList.size

}