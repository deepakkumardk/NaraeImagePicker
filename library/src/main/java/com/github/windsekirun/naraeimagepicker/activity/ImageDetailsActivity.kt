package com.github.windsekirun.naraeimagepicker.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.event.ToolbarEvent
import com.github.windsekirun.naraeimagepicker.fragment.adapter.ImagePreviewAdapter
import com.github.windsekirun.naraeimagepicker.impl.OnImageSelected
import com.github.windsekirun.naraeimagepicker.item.FileItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.utils.applyCustomPickerTheme
import com.github.windsekirun.naraeimagepicker.utils.catchAll
import kotlinx.android.synthetic.main.activity_image_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pyxis.uzuki.live.naraeimagepicker.R

/**
 * NaraeImagePicker
 * Class: ImageDetailsActivity
 * Created by Pyxis on 1/6/18.
 *
 * Description:
 */

class ImageDetailsActivity : AppCompatActivity() {
    private lateinit var adapter: ImagePreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyCustomPickerTheme(PickerSet.getSettingItem())
        setContentView(R.layout.activity_image_details)

        val position = intent.getIntExtra(Constants.EXTRA_CURRENT_POSITION, 0)
        val fileItemList = intent.getParcelableArrayListExtra<FileItem>(Constants.EXTRA_FILE_ITEM_LIST)

        catchAll { EventBus.getDefault().register(this) }

        adapter = ImagePreviewAdapter(supportFragmentManager, fileItemList, object : OnImageSelected {
            override fun onClick(view: View, fileItem: FileItem) {
                adapter.notifyDataSetChanged()
            }
        })
        view_pager.adapter = adapter
        view_pager.currentItem = position
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when {
            menuItem.itemId == android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    @Subscribe
    fun onDetailToolbarChange(event: ToolbarEvent) {

    }

    override fun onDestroy() {
        super.onDestroy()
        catchAll { EventBus.getDefault().unregister(this) }
    }
}