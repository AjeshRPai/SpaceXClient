package com.android.spacexclient.domain

import com.android.spacexclient.database.LocalRocketModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RocketDomainMapperTest {

    val sut = RocketDomainMapper()

    @Test
    fun `should assign correct values`() {
        val inputModel = LocalRocketModel("1234",
            "Space x",
            "US",
            false,
            1,
            listOf("Image1Url", "Image2Url"),
            "2019")

        val actual = sut.map(inputModel)

        assertEquals(inputModel.active,actual.active,)
        assertEquals(inputModel.country,actual.country)
        assertEquals(inputModel.engines,actual.engines)
        assertEquals(inputModel.flickrImages,actual.flickrImages)
        assertEquals(inputModel.id,actual.id)
        assertEquals(inputModel.name,actual.name)
        assertEquals(inputModel.yearOfLaunch,actual.year)
    }

    @Test
    fun `should assign default value for date in case of default value from local model`() {
        val inputModel = LocalRocketModel(
            "1234",
            "Space x",
            "US",
            false,
            1,
            listOf("Image1Url", "Image2Url"),
            ""
        )

        val actual = sut.map(inputModel)

        assertEquals(inputModel.active, actual.active,)
        assertEquals(inputModel.country, actual.country)
        assertEquals(inputModel.engines, actual.engines)
        assertEquals(inputModel.flickrImages, actual.flickrImages)
        assertEquals(inputModel.id, actual.id)
        assertEquals(inputModel.name, actual.name)
        assertTrue(actual.year.isEmpty())
    }
}