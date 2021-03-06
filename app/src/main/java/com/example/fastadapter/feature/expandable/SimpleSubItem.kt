package com.example.fastadapter.feature.expandable

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fastadapter.R
import com.mikepenz.fastadapter.IExpandable
import com.mikepenz.fastadapter.drag.IDraggable
import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem
import com.mikepenz.fastadapter.ui.utils.FastAdapterUIUtils
import com.mikepenz.fastadapter.ui.utils.StringHolder

open class SimpleSubItem : AbstractExpandableItem<SimpleSubItem.ViewHolder>(), IDraggable, IExpandable<SimpleSubItem.ViewHolder> {

    var name: String? = null
    var description: String? = null

    override var isDraggable = true

    /**
     * defines the type defining this item. must be unique. preferably an id
     *
     * @return the type
     */
    override val type: Int
        get() = R.id.fastadapter_sub_item_id

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    override val layoutRes: Int
        get() = R.layout.sample_item


    /**
     * binds the data of this item onto the viewHolder
     *
     * @param holder the viewHolder of this item
     */
    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        //get the context
        val ctx = holder.itemView.context

        //set the background for the item
        holder.view.clearAnimation()
        ViewCompat.setBackground(holder.view, FastAdapterUIUtils.getSelectableBackground(ctx, Color.RED, true))
        //set the text for the name
        holder.name.text = name
        //set the text for the description or hide
        holder.description.text = description
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.name.text = null
        holder.description.text = null
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    /**
     * our ViewHolder
     */
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.material_drawer_name)
        var description: TextView = view.findViewById(R.id.material_drawer_description)
    }
}
