package com.pluu.sample.codeforreadability.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pluu.sample.codeforreadability.model.SampleItem
import com.pluu.sample.codeforreadability.provider.SampleItemGenerator
import com.pluu.sample.codeforreadability.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel

    @Mock
    private lateinit var sampleItemGenerator: SampleItemGenerator

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        sampleItemGenerator = mock {
            on { generate() } doReturn SampleItem("", 0)
        }
        viewModel = SearchViewModel(
            generator = sampleItemGenerator,
            savingRepository = mock())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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