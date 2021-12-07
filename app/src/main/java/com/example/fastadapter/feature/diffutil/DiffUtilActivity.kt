package com.example.fastadapter.feature.diffutil

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastadapter.R
import com.example.fastadapter.databinding.ActivityDiifutilBinding
import com.example.fastadapter.databinding.ActivityMainBinding
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList

class DiffUtilActivity: AppCompatActivity() {


    private lateinit var binding: ActivityDiifutilBinding

    private lateinit var fastItemAdapter: GenericFastItemAdapter
    private val disposables = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiifutilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //create our FastAdapter which will manage everything
        fastItemAdapter = FastItemAdapterDiffUtil()

        //get our recyclerView and do basic setup
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = fastItemAdapter

        setupListener()

        val items = createData()
        setData(items)
    }

    private fun setupListener() {
        binding.btnSuffle.setOnClickListener {
            setDataAsync()
        }
    }

    private fun setDataAsync() {
        disposables.add(Single.fromCallable { createData() }
            .map { simpleItems -> FastAdapterDiffUtil.calculateDiff(fastItemAdapter.itemAdapter, simpleItems) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    FastAdapterDiffUtil[fastItemAdapter.itemAdapter] = result
                }, { err ->
                    Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show()
                }))
    }

    private fun setData(itemDiffUtils: List<DiffUtilSimpleItem>) {
        FastAdapterDiffUtil[fastItemAdapter.itemAdapter] = itemDiffUtils

        //Add all
        //fastItemAdapter.add(items)
    }

    private fun createData(): List<DiffUtilSimpleItem> {
        val itemAdapter = ItemAdapter<DiffUtilSimpleItem>()
        val items = ArrayList<DiffUtilSimpleItem>()
        for (i in 1..100) {
            val item = DiffUtilSimpleItem()
            item.name = "Test $i"
            item.description = (100 + i).toString()
            items.add(item)
        }
        itemAdapter.add(items)
        items.shuffle()
        return items
    }
}