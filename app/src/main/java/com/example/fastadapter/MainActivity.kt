package com.example.fastadapter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fastadapter.databinding.ActivityMainBinding
import com.example.fastadapter.feature.diffutil.DiffUtilActivity
import com.example.fastadapter.feature.draggable.DraggableActivity
import com.example.fastadapter.feature.viewbinding.ViewBindingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
    }

    private fun setupListener() {
        binding.btnViewBinding.setOnClickListener {
            val intent = Intent(this@MainActivity, ViewBindingActivity::class.java)
            startActivities(arrayOf(intent))
        }
        binding.btnDiffUtil.setOnClickListener {
            val intent = Intent(this@MainActivity, DiffUtilActivity::class.java)
            startActivities(arrayOf(intent))
        }
        binding.btnDraggable.setOnClickListener {
            val intent = Intent(this@MainActivity, DraggableActivity::class.java)
            startActivities(arrayOf(intent))
        }
    }

}