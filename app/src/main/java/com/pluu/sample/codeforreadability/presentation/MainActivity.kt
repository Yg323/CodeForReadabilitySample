package com.pluu.sample.codeforreadability.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.pluu.sample.codeforreadability.databinding.ActivityMainBinding
import com.pluu.sample.codeforreadability.utils.dp

class MainActivity : AppCompatActivity() {

    private lateinit var sampleAdapter: SampleAdapter

    private lateinit var binding: ActivityMainBinding

    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("sample", Context.MODE_PRIVATE)
    }

    private val viewModel by lazy { SearchViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        sampleAdapter = SampleAdapter(
            onFavorite = {
                preferences.edit {
                    putString("KEY", it)
                }
                sampleAdapter.updateFavorite(it)
                sampleAdapter.notifyDataSetChanged()
            }
        )

        binding.btnGenerate.setOnClickListener {
            viewModel.generate()
        }
        binding.btnClear.setOnClickListener {
            viewModel.reset()
        }
        with(binding.recyclerView) {
            adapter = sampleAdapter
            addItemDecoration(SampleItemDecoration(4.dp))
        }

        sampleAdapter.updateFavorite(preferences.getString("KEY", null).orEmpty())
    }

    private fun setUpObservers() {
        viewModel.items.observe(this) { list ->
            sampleAdapter.submitList(list)
            binding.recyclerView.scrollToPosition(0)
        }
        viewModel.messageEvent.observe(this) { msg ->
            Toast.makeText(this, "Duplicate item : $msg", Toast.LENGTH_SHORT).show()
        }
    }
}