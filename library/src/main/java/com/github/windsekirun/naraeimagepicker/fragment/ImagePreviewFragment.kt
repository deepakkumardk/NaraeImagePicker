package com.github.windsekirun.naraeimagepicker.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.windsekirun.naraeimagepicker.Constants
import com.github.windsekirun.naraeimagepicker.event.ToolbarEvent
import com.github.windsekirun.naraeimagepicker.impl.OnImageSelected
import com.github.windsekirun.naraeimagepicker.item.FileItem
import com.github.windsekirun.naraeimagepicker.module.PickerSet
import com.github.windsekirun.naraeimagepicker.module.SelectedItem
import com.github.windsekirun.naraeimagepicker.utils.catchAll
import com.github.windsekirun.naraeimagepicker.utils.hide
import com.github.windsekirun.naraeimagepicker.utils.loadImage
import com.github.windsekirun.naraeimagepicker.utils.show
import kotlinx.android.synthetic.main.item_image_preview.*
import kotlinx.android.synthetic.main.toolbar_image_preview.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pyxis.uzuki.live.naraeimagepicker.R
import pyxis.uzuki.live.richutilskt.utils.toast

/**
 * NaraeImagePicker
 * Class: ImagePreviewFragment
 * Created by Deepak Kumar on 1st July 2019
 *
 * Description: Fragment to handle full image preview for the view pager
 */

class ImagePreviewFragment : Fragment() {

    object ImagePreviewObject {
        var position = 0
        var listener: OnImageSelected? = null

        fun newInstance(item: FileItem, position: Int, listener: OnImageSelected?): ImagePreviewFragment {
            this.listener = listener
            val fragment = ImagePreviewFragment()
            val bundle = Bundle()
            this.position = position
            bundle.putParcelable(Constants.EXTRA_FILE_ITEM, item)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_image_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTheme()

        catchAll { EventBus.getDefault().register(this) }
        val item = arguments?.getParcelable<FileItem>(Constants.EXTRA_FILE_ITEM)

        image_preview.loadImage(item?.imagePath!!)
        image_preview.setOnViewTapListener { v, x, y ->
            //            ImagePreviewObject.listener?.onClick(v, item)
            if (toolbar_detail.visibility == View.VISIBLE)
                toolbar_detail.hide()
            else
                toolbar_detail.show()
        }
        updateToolbar()
        img_check_detail.isSelected = SelectedItem.contains(item)

        img_check_detail.setOnClickListener {
            //            ImagePreviewObject.listener?.onClick(it, item)
            if (SelectedItem.contains(item)) {
                SelectedItem.removeItem(item)
            } else {
                addSelectedItem(item)
            }
            img_check_detail.isSelected = SelectedItem.contains(item)
            updateToolbar()
        }
        back_toolbar.setOnClickListener {
            this.activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        catchAll { EventBus.getDefault().unregister(this) }
    }

    private fun setToolbarTheme() {
        val typedValue = TypedValue()
        activity?.theme?.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        val primaryColor = typedValue.data
        toolbar_detail.setBackgroundColor(primaryColor)
    }

    private fun updateToolbar() {
        EventBus.getDefault().post(ToolbarEvent("${SelectedItem.size} Selected", true))
    }

    private fun addSelectedItem(item: FileItem) {
        SelectedItem.addItem(item) {
            if (!it) Toast.makeText(this.activity, PickerSet.getLimitMessage(), Toast.LENGTH_SHORT).show()
        }
    }

    @Subscribe
    fun onDetailToolbarChange(event: ToolbarEvent) {
        toolbar_title?.text = event.item
    }
}