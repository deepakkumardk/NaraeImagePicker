package com.github.windsekirun.naraeimagepicker.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.base.BaseFragment
import com.github.windsekirun.naraeimagepicker.event.ToolbarEvent
import com.github.windsekirun.naraeimagepicker.fragment.adapter.ImageAdapter
import com.github.windsekirun.naraeimagepicker.item.FileItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.module.SelectedItem
import kotlinx.android.synthetic.main.fragment_list.*
import org.greenrobot.eventbus.EventBus
import pyxis.uzuki.live.richutilskt.utils.runAsync
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread
import pyxis.uzuki.live.richutilskt.utils.toFile
import pyxis.uzuki.live.richutilskt.utils.toast

/**
 * NaraeImagePicker
 * Class: AllFragment
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class AllFragment : BaseFragment<FileItem>() {
    private lateinit var adapter: ImageAdapter
    private val itemList = arrayListOf<FileItem>()

    override fun getItemList() = itemList
    override fun getColumnCount(): Int = PickerSet.getSettingItem().uiSetting.fileSpanCount

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ImageAdapter(itemList) { fileItem: FileItem, _: Int, _: View ->
             onImageClick(fileItem)
        }
        recyclerView.adapter = adapter

        if (PickerSet.isEmptyList()) {
            runAsync { PickerSet.loadImageFirst(requireContext()) { bindList() } }
        } else {
            bindList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.RC_IMAGE_DETAIL && data != null) {
            this.activity?.toast("onActivityResult")
            adapter.notifyDataSetChanged()
//            EventBus.getDefault().post(ToolbarEvent("${SelectedItem.size} Selected", PickerSet.getSettingItem().uiSetting.enableUpInParentView))
        }
    }

    private fun bindList() {
        itemList.addAll(PickerSet.getImageList())
        itemList.sortedBy { it.imagePath.toFile().lastModified() }

        runOnUiThread {
            recyclerView?.notifyDataSetChanged()
        }
    }

    private fun onImageClick(fileItem: FileItem) {
        if (SelectedItem.contains(fileItem)) {
            SelectedItem.removeItem(fileItem)
        } else {
            addSelectedItem(fileItem)
        }
        sendEvent(ToolbarEvent("${SelectedItem.size} Selected", PickerSet.getSettingItem().uiSetting.enableUpInParentView))
        adapter.notifyDataSetChanged()
    }

    private fun addSelectedItem(item: FileItem) {
        SelectedItem.addItem(item) {
            if (!it) this.activity?.toast(PickerSet.getLimitMessage())
        }
    }
}