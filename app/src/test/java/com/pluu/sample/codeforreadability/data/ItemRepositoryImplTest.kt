package com.pluu.sample.codeforreadability.data

import com.pluu.sample.codeforreadability.provider.RandomGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class ItemRepositoryImplTest {

    private lateinit var itemRepository: ItemRepository

    private val generator: RandomGenerator = mock()

    @Before
    fun setUp() {
        itemRepository = ItemRepositoryImpl(generator)
    }

    @Test
    fun generate() {
        var callCount = 0
        whenever(generator.randomAlphabet())
            .then {
                ('a' + callCount++).toString()
            }

        repeat(26) {
            val result = itemRepository.generate()
            assertTrue(result.isSuccess)
            assertEquals(('a' + it).toString(), result.getOrThrow().text)
        }
    }

    @Test
    fun reset() {
        assertTrue(itemRepository.data.isEmpty())

        whenever(generator.randomAlphabet())
            .thenReturn("0")
        itemRepository.generate()
        assertEquals(1, itemRepository.data.size)

        itemRepository.reset()
        assertTrue(itemRepository.data.isEmpty())
    }
}