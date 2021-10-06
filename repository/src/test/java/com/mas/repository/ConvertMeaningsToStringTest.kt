package com.mas.repository

import com.mas.dictionary.utils.convertMeaningsToString
import com.mas.model.Meanings
import com.mas.model.Translation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ConvertMeaningsToStringTest {
    private val meaningsList = listOf(
        Meanings(Translation("translation 1"), "", "", ""),
        Meanings(Translation("translation 2"), "", "", ""),
        Meanings(Translation("translation 3"), "", "", "")
    )

    @Test
    fun convertMeaningsToString_CompareWithReference_ReturnsTrue() {
        assertEquals(
            "translation 1, translation 2, translation 3",
            convertMeaningsToString(meaningsList)
        )
    }

    @Test
    fun convertMeaningsToString_NotNullReturnCheck1_ReturnsTrue() {
        assertNotNull(convertMeaningsToString(emptyList()))
    }

}