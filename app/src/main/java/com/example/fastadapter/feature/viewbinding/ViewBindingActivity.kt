package com.example.fastadapter.feature.viewbinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastadapter.databinding.ActivityViewBindingBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class ViewBindingActivity : AppCompatActivity() {

    private lateinit var mFastAdapter: FastAdapter<ViewBindingSimpleItem>
    private val headerAdapter = ItemAdapter<ViewBindingSimpleItem>()
    private val itemAdapter = ItemAdapter<ViewBindingSimpleItem>()
    private lateinit var binding: ActivityViewBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBindingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFastAdapter = FastAdapter.with(listOf(itemAdapter, headerAdapter))

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = mFastAdapter

        populateData()

        mFastAdapter.withSavedInstanceState(savedInstanceState)
    }

    private fun populateData() {
        val simpleItem = ViewBindingSimpleItem()
        simpleItem.name = "Header"
        simpleItem.identifier = 2
        simpleItem.isSelectable = false
        headerAdapter.add(simpleItem)
        val items = ArrayList<ViewBindingSimpleItem>()
        for (i in 1..100) {
            val item = ViewBindingSimpleItem()
            item.name = "Test $i"
            item.description = (100 + i).toString()
            items.add(item)
        }
        itemAdapter.add(items)
    }
}