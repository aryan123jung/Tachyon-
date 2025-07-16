package com.example.kokoro.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kokoro.R
import com.example.kokoro.adapter.BookAdapter
import com.example.kokoro.databinding.ActivityBookBinding
import com.example.kokoro.repository.BookRepositoryImpl
import com.example.kokoro.viewModel.BookViewModel

class BookActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookBinding
    lateinit var bookViewModel: BookViewModel
    lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BookAdapter(this@BookActivity,
            ArrayList())

        var repo = BookRepositoryImpl()
        bookViewModel = BookViewModel(repo)
        bookViewModel.getAllBook()

        bookViewModel.allBooks.observe(this){ books ->
            books?.let {
                adapter.updateData(it)
            }
        }
        binding.recycler2.adapter = adapter
        binding.recycler2.layoutManager = LinearLayoutManager(this)

        binding.floatingActionButton3.setOnClickListener {
            var intent = Intent(this@BookActivity,
                AddBookActivity::class.java
            )
            startActivity(intent)
        }

        binding.StoriesBtn.setOnClickListener {
            val intent2 = Intent(this@BookActivity,
                DashboardActivity::class.java)
            startActivity(intent2)
        }
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,
             ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var bookId = adapter.getBookId(viewHolder.adapterPosition)

                bookViewModel.deleteBook(bookId){
                        success,message->
                    if(success){
                        Toast.makeText(this@BookActivity,
                            message, Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@BookActivity,message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
            .attachToRecyclerView(binding.recycler2)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}