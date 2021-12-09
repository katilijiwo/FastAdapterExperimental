package com.example.fastadapter.feature.draggable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastadapter.R
import com.example.fastadapter.databinding.ActivityDraggableBinding
import com.example.fastadapter.feature.draggable.customview.DraggableFrameLayout
import com.example.fastadapter.feature.draggable.customview.RecyclerViewBackgroundDrawable
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.drag.ItemTouchCallback
import com.mikepenz.fastadapter.drag.SimpleDragCallback
import com.mikepenz.fastadapter.utils.DragDropUtil
import com.example.fastadapter.feature.draggable.DragHandleTouchEvent

class DraggableActivity : AppCompatActivity(), ItemTouchCallback {

    private lateinit var fastAdapter: FastAdapter<GenericItem>
    private lateinit var itemAdapter: ItemAdapter<GenericItem>
    private lateinit var touchHelper: ItemTouchHelper
    private lateinit var touchCallback: SimpleDragCallback

    private lateinit var binding: ActivityDraggableBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDraggableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create empty ItemAdapter
        itemAdapter = ItemAdapter.items()

        // Create FastAdapter instance that will manage the whole list
        fastAdapter = FastAdapter.with(itemAdapter).apply {
            // Add an event hook that manages touching the drag handle
            addEventHook(
                DragHandleTouchEvent{ position ->
                    binding.rv.findViewHolderForAdapterPosition(position)?.let { viewHolder ->
                        // Start dragging
                        touchHelper.startDrag(viewHolder)
                    }
                }
            )
        }

        // Handle clicks on our list items
        fastAdapter.onClickListener = { v: View?, _: IAdapter<GenericItem>, item: GenericItem, _: Int ->
            if (v != null) {
                // Perform an action depending on the type of the item
            }
            false
        }

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.itemAnimator = DefaultItemAnimator()
        binding.rv.adapter = fastAdapter


        val recyclerViewBackgroundColor = ResourcesCompat.getColor(resources, R.color.behindRecyclerView, theme)
        RecyclerViewBackgroundDrawable(recyclerViewBackgroundColor).attachTo(binding.rv)

        val listData = buildSampleItemList()
        itemAdapter.add(listData)

        touchCallback = SimpleDragCallback(itemTouchCallback = this).apply {
            // Disable drag & drop on long-press
            isDragEnabled = false
        }
        touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(binding.rv)

        // Restore the adapter state (this has to be done after adding the items)
        fastAdapter.withSavedInstanceState(savedInstanceState)
    }

    /* private fun populateData() {
        val items = ArrayList<GenericItem>()
        val itemsDraggable = ArrayList<DraggableSingleLineItem>()
        for (i in 1..100) {
            val item = DraggableSingleLineItem()
            item.name = "Test $i"
            item.description = (100 + i).toString()
            itemsDraggable.add(item)
        }
        itemAdapter.add(itemsDraggable)
    } */

    private fun buildSampleItemList(): ArrayList<GenericItem> {
        val items = ArrayList<GenericItem>()

        val accountItems = (1..5).map { i ->
            DraggableSingleLineItem().apply {
                name  = "Test $i"
                description = (100 + i).toString()
                identifier = (100 + i).toLong()
            }
        }

        items.addAll(accountItems)
        return items
    }

    override fun itemTouchStartDrag(viewHolder: RecyclerView.ViewHolder) {
        // Add visual highlight to the dragged item
        (viewHolder.itemView as DraggableFrameLayout).isDragged = true
    }

    override fun itemTouchStopDrag(viewHolder: RecyclerView.ViewHolder) {
        // Remove visual highlight from the dropped item
        (viewHolder.itemView as DraggableFrameLayout).isDragged = false
    }

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean {
        // Determine the "drop area"
        val firstDropPosition = itemAdapter.adapterItems.indexOfFirst { it is DraggableSingleLineItem }
        val lastDropPosition = itemAdapter.adapterItems.indexOfLast { it is DraggableSingleLineItem }

        // Only move the item if the new position is inside the "drop area"
        return if (newPosition in firstDropPosition..lastDropPosition) {
            // Change the item's position in the adapter
            DragDropUtil.onMove(itemAdapter, oldPosition, newPosition)
            true
        } else {
            false
        }    }

}