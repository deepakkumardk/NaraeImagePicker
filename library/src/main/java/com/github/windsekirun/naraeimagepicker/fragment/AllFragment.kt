package com.github.windsekirun.naraeimagepicker.fragment

import android.os.Bundle
import android.view.View
import com.github.windsekirun.naraeimagepicker.base.BaseFragment
import com.github.windsekirun.naraeimagepicker.event.ToolbarEvent
import com.github.windsekirun.naraeimagepicker.fragment.adapter.ImageAdapter
import com.github.windsekirun.naraeimagepicker.item.FileItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.module.SelectedItem
import com.github.windsekirun.naraeimagepicker.utils.doAsync
import com.github.windsekirun.naraeimagepicker.utils.runOnUiThread
import com.github.windsekirun.naraeimagepicker.utils.toast
import com.github.windsekirun.naraeimagepicker.widget.EmptyRecyclerView
import pyxis.uzuki.live.naraeimagepicker.R
import java.io.File

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

    private lateinit var recyclerView: EmptyRecyclerView

    override fun getItemList() = itemList
    override fun getColumnCount(): Int = PickerSet.getSettingItem().uiSetting.fileSpanCount

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ImageAdapter(itemList) { fileItem: FileItem, _: Int, _: View ->
            onImageClick(fileItem)
        }
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter

        if (PickerSet.isEmptyList()) {
            doAsync { PickerSet.loadImageFirst(requireContext()) { bindList() } }
        } else {
            bindList()
        }
    }

    private fun bindList() {
        itemList.addAll(PickerSet.getImageList())
        itemList.sortedBy { File(it.imagePath).lastModified() }

        runOnUiThread {
            recyclerView.notifyDataSetChanged()
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