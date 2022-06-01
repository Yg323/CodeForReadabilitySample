package com.pluu.sample.codeforreadability.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pluu.sample.codeforreadability.data.ItemRepository
import com.pluu.sample.codeforreadability.model.ColorValue
import com.pluu.sample.codeforreadability.model.GenerateItem
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel

    @Mock
    private val itemRepository: ItemRepository = mock()

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        viewModel = SearchViewModel(
            itemRepository = itemRepository,
            savingRepository = mock(),
            logRepository = mock()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun generate() {

        whenever(itemRepository.generate())
            .thenReturn(Result.success(GenerateItem("", ColorValue(0))))

        whenever(itemRepository.data)
            .thenReturn(listOf(GenerateItem("", ColorValue(0))))

        // 1
        viewModel.generate()
        assertTrue(viewModel.items.getOrAwaitValue().isNotEmpty())

        whenever(itemRepository.data)
            .thenReturn(emptyList())

        // 2
        viewModel.reset()
        assertTrue(viewModel.items.getOrAwaitValue().isEmpty())
    }
}