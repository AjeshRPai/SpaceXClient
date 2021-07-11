package com.android.spacexclient.database

import com.android.spacexclient.api.NetworkRocketModel
import org.junit.Assert
import org.junit.Test

class RocketDtoMapperTest
{
    val sut = RocketDtoMapper()

    @Test
    fun `should assign correct values`() {
       val inputModel = NetworkRocketModel("1234",
           "Space x",
           "US",
           false,
           NetworkRocketModel.Engines(1),
           listOf("Image1Url", "Image2Url"),
           "2019-06-23")

        val actual = sut.map(inputModel)

        Assert.assertEquals(inputModel.active,actual.active,)
        Assert.assertEquals(inputModel.country,actual.country)
        Assert.assertEquals(inputModel.engines.number,actual.engines)
        Assert.assertEquals(inputModel.flickrImages,actual.flickrImages)
        Assert.assertEquals(inputModel.id,actual.id)
        Assert.assertEquals(inputModel.name,actual.name)
        Assert.assertEquals("2019",actual.yearOfLaunch)
    }

    @Test
    fun `should assign default value for date and rest everything should be same in case of invalid date format`() {
        val invalidDate = "2019-08"
        val inputModel = NetworkRocketModel("1234",
            "Space x",
            "US",
            false,
            NetworkRocketModel.Engines(1),
            listOf("Image1Url", "Image2Url"),
            invalidDate)

        val actual = sut.map(inputModel)

        Assert.assertEquals(inputModel.active,actual.active,)
        Assert.assertEquals(inputModel.country,actual.country)
        Assert.assertEquals(inputModel.engines.number,actual.engines)
        Assert.assertEquals(inputModel.flickrImages,actual.flickrImages)
        Assert.assertEquals(inputModel.id,actual.id)
        Assert.assertEquals(inputModel.name,actual.name)
        Assert.assertTrue(actual.yearOfLaunch.isEmpty())
    }


}