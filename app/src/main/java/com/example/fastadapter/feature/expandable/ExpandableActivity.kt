package com.example.fastadapter.feature.expandable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastadapter.databinding.ActivityExpandableBinding
import com.example.fastadapter.feature.diffutil.FastItemAdapterDiffUtil
import com.example.fastadapter.feature.diffutil.GenericFastItemAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.expandable.getExpandableExtension
import com.mikepenz.fastadapter.select.getSelectExtension
import com.mikepenz.itemanimators.SlideDownAlphaAnimator
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class ExpandableActivity: AppCompatActivity() {

    private lateinit var binding: ActivityExpandableBinding
    private lateinit var fastItemAdapter: GenericFastItemAdapter
    private lateinit var itemToBeExpanded: SimpleSubExpandableItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpandableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fastItemAdapter = FastItemAdapterDiffUtil()
        fastItemAdapter.getExpandableExtension()
        val selectExtension = fastItemAdapter.getSelectExtension()
        selectExtension.isSelectable = true

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.itemAnimator = SlideDownAlphaAnimator()
        binding.rv.adapter = fastItemAdapter

        val items = createSampleData()
        fastItemAdapter.add(items)

        //restore selections (this has to be done after the items were added)
        fastItemAdapter.withSavedInstanceState(savedInstanceState)

        //set the back arrow in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)

        //expand the whole path for the previously selected item
        fastItemAdapter.getExpandableExtension().expandAllOnPath(itemToBeExpanded)
    }

    private fun createSampleData(): ArrayList<GenericItem> {
        val items = ArrayList<GenericItem>()
        val identifier = AtomicLong(1)
        for (i in 1..100) {
            val parent = createFirstData(i, identifier.getAndIncrement())

            val subItems = LinkedList<SimpleSubExpandableItem>()
            for (ii in 1..5) {
                val subItem = createSecondData(ii, identifier.getAndIncrement())

                val subSubItems = LinkedList<SimpleSubExpandableItem>()
                for (iii in 1..3) {
                    val subSubItem = createThirdData(iii, identifier.getAndIncrement())

                    val subSubSubItems = LinkedList<SimpleSubExpandableItem>()
                    for (iiii in 1..4) {
                        val subSubSubItem = createFourthData(iii, identifier.getAndIncrement())
                        subSubSubItems.add(subSubSubItem)

                        //save 7th item just to demonstrate how expandAllOnPath works
                        if (identifier.get() == 7L) {
                            itemToBeExpanded = subSubSubItem
                        }
                    }
                    subSubItem.subItems.addAll(subSubSubItems)
                    subSubItems.add(subSubItem)
                }
                subItem.subItems.addAll(subSubItems)
                subItems.add(subItem)
            }
            parent.subItems.addAll(subItems)
            items.add(parent)
        }

        return items
    }

    private fun createFirstData(i: Int, identifier: Long): SimpleSubExpandableItem {
        val item = SimpleSubExpandableItem()
        item.name = "item $i"
        item.identifier = identifier
        return item
    }

    private fun createSecondData(ii: Int, identifier: Long): SimpleSubExpandableItem {
        val subItem = SimpleSubExpandableItem()
        subItem.name = "subItem $ii"
        subItem.identifier = identifier
        return subItem
    }

    private fun createThirdData(iii: Int, identifier: Long): SimpleSubExpandableItem {
        val subSubItem = SimpleSubExpandableItem()
        subSubItem.identifier = identifier
        subSubItem.name = "subSubItem $iii"
        return subSubItem
    }

    private fun createFourthData(iii: Int, identifier: Long): SimpleSubExpandableItem {
        val subSubSubItem = SimpleSubExpandableItem()
        subSubSubItem.identifier = identifier
        subSubSubItem.name = "subSubSubItem $iii"
        return subSubSubItem
    }

}