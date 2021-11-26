package com.example.fastadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastadapter.adapter.FastItemAdapter
import com.example.fastadapter.adapter.GenericFastItemAdapter
import com.example.fastadapter.adapter.SimpleItem
import com.example.fastadapter.databinding.ActivityMainBinding
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /*
    private lateinit var mFastAdapter: FastAdapter<SimpleItem>
    private val headerAdapter = ItemAdapter<SimpleItem>()
    private val itemAdapter = ItemAdapter<SimpleItem>()
    */

    private lateinit var fastItemAdapter: GenericFastItemAdapter
    private val disposables = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*mFastAdapter = FastAdapter.with(listOf(headerAdapter, itemAdapter))

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = mFastAdapter

        mFastAdapter.withSavedInstanceState(savedInstanceState)

        populateData()*/


        //create our FastAdapter which will manage everything
        fastItemAdapter = FastItemAdapter()

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

    /* private fun populateData() {
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
    } */

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

    private fun setData(items: List<SimpleItem>) {
        FastAdapterDiffUtil[fastItemAdapter.itemAdapter] = items

        //Add all
        //fastItemAdapter.add(items)
    }

    private fun createData(): List<SimpleItem> {
        val itemAdapter = ItemAdapter<SimpleItem>()
        val items = ArrayList<SimpleItem>()
        for (i in 1..100) {
            val item = SimpleItem()
            item.name = "Test $i"
            item.description = (100 + i).toString()
            items.add(item)
        }
        itemAdapter.add(items)
        items.shuffle()
        return items
    }
}