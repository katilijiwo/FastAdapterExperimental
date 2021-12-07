package com.example.fastadapter.feature.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fastadapter.R
import com.example.fastadapter.databinding.SampleItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ViewBindingSimpleItem : AbstractBindingItem<SampleItemBinding>() {
    var name: String? = null
    var description: String? = null

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.fastadapter_sample_item_id

    override fun bindView(binding: SampleItemBinding, payloads: List<Any>) {
        binding.materialDrawerName.text = name
        binding.materialDrawerDescription.text = description
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): SampleItemBinding {
        return SampleItemBinding.inflate(inflater, parent, false)
    }
}