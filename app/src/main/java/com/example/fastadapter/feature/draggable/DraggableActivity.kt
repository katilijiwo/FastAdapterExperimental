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

class DraggableActivity : AppCompatActivity(), ItemTouchCallback {

    private lateinit var fastAdapter: FastAdapter<GenericItem>
    private lateinit var itemAdapter: ItemAdapter<GenericItem>
    private lateinit var touchHelper: ItemTouchHelper
    private lateinit var binding: ActivityDraggableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDraggableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemAdapter = ItemAdapter.items()

        setDraggableEventHook()

        fastAdapter.onClickListener =
            { v: View?, _: IAdapter<GenericItem>, item: GenericItem, _: Int ->
                if (v != null) {

                }
                false
            }

        initRecyclerView()
        addItemList()

        touchHelper = ItemTouchHelper(SimpleDragCallback(itemTouchCallback = this).apply {
            // Disable drag & drop on long-press
            isDragEnabled = false
        })
        touchHelper.attachToRecyclerView(binding.rv)

        // Restore the adapter state (this has to be done after adding the items)
        fastAdapter.withSavedInstanceState(savedInstanceState)
    }

    private fun initRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.itemAnimator = DefaultItemAnimator()
        binding.rv.adapter = fastAdapter

        val recyclerViewBackgroundColor =
            ResourcesCompat.getColor(resources, R.color.behindRecyclerView, theme)
        RecyclerViewBackgroundDrawable(recyclerViewBackgroundColor).attachTo(binding.rv)
    }

    private fun setDraggableEventHook() {
        fastAdapter = FastAdapter.with(itemAdapter).apply {
            // Add an event hook that manages touching the drag handle
            addEventHook(
                DragHandleTouchEvent { position ->
                    binding.rv.findViewHolderForAdapterPosition(position)?.let { viewHolder ->
                        // Start dragging
                        touchHelper.startDrag(viewHolder)
                    }
                }
            )
        }
    }

    private fun addItemList() {
        val items = ArrayList<GenericItem>()
        val accountItems = (1..5).map { i ->
            DraggableSingleLineItem().apply {
                name = "Test $i"
                description = (100 + i).toString()
                identifier = (100 + i).toLong()
            }
        }
        items.addAll(accountItems)
        itemAdapter.add(items)
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
        val firstDropPosition =
            itemAdapter.adapterItems.indexOfFirst { it is DraggableSingleLineItem }
        val lastDropPosition =
            itemAdapter.adapterItems.indexOfLast { it is DraggableSingleLineItem }

        // Only move the item if the new position is inside the "drop area"
        return if (newPosition in firstDropPosition..lastDropPosition) {
            // Change the item's position in the adapter
            DragDropUtil.onMove(itemAdapter, oldPosition, newPosition)
            true
        } else {
            false
        }
    }

}