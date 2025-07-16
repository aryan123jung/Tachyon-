package com.example.kokoro.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kokoro.R
import com.example.kokoro.adapter.StoryAdapter
import com.example.kokoro.databinding.ActivityDashboardBinding
import com.example.kokoro.repository.StoryRepositoryImpl
import com.example.kokoro.viewModel.StoryViewModel

class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding
    lateinit var storyViewModel: StoryViewModel
    lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = StoryAdapter(this@DashboardActivity,
            ArrayList())

        var repo = StoryRepositoryImpl()
        storyViewModel = StoryViewModel(repo)
        storyViewModel.getAllStory()

        storyViewModel.allStories.observe(this) { stories ->
            stories?.let {
                adapter.updateData(it)
            }
        }
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)

        binding.floatingActionButton2.setOnClickListener {
            var intent = Intent(this@DashboardActivity,
                AddStoryActivity::class.java
            )
            startActivity(intent)
        }

        binding.BookBtn.setOnClickListener {
            val intent2 = Intent(this@DashboardActivity, BookActivity::class.java)
            startActivity(intent2)
        }

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var storyId = adapter.getStoryId(viewHolder.adapterPosition)

                storyViewModel.deleteStory(storyId){
                    success,message->
                    if(success){
                        Toast.makeText(this@DashboardActivity,
                            message,Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@DashboardActivity,message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
            .attachToRecyclerView(binding.recycler)



            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}