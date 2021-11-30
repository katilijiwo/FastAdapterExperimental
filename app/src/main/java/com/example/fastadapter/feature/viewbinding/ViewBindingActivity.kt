package com.example.fastadapter.feature.viewbinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastadapter.R
import com.example.fastadapter.databinding.ActivityDiifutilBinding
import com.example.fastadapter.databinding.ActivityViewBindingBinding
import com.example.fastadapter.feature.diffutil.SimpleItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil

class ViewBindingActivity : AppCompatActivity() {

    private lateinit var mFastAdapter: FastAdapter<SimpleItem>
    private val headerAdapter = ItemAdapter<SimpleItem>()
    private val itemAdapter = ItemAdapter<SimpleItem>()
    private lateinit var binding: ActivityViewBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBindingBinding.inflate(layoutInflater)

        mFastAdapter = FastAdapter.with(listOf(headerAdapter, itemAdapter))

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = mFastAdapter

        mFastAdapter.withSavedInstanceState(savedInstanceState)

        populateData()
    }

    private fun populateData() {
        val simpleItem = SimpleItem()
        simpleItem.name = "Header"
        simpleItem.identifier = 2
        simpleItem.isSelectable = false
        headerAdapter.add(simpleItem)
        val items = ArrayList<SimpleItem>()
        for (i in 1..100) {
            val item = SimpleItem()
            item.name = "Test $i"
            item.description = (100 + i).toString()
            items.add(item)
        }
        itemAdapter.add(items)
    }
}