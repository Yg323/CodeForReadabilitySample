package com.pluu.sample.codeforreadability.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pluu.sample.codeforreadability.utils.getOrAwaitValue
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class SearchViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        viewModel = SearchViewModel()
    }

    @Test
    fun generate() {

        // 1
        viewModel.generate()
        assertTrue(viewModel.items.getOrAwaitValue().isNotEmpty())

        // 2
        viewModel.reset()
        assertTrue(viewModel.items.getOrAwaitValue().isEmpty())
    }
}