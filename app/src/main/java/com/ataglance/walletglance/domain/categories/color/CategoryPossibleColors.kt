package com.ataglance.walletglance.domain.categories.color

import com.ataglance.walletglance.domain.app.AppTheme
import com.ataglance.walletglance.domain.color.ColorWithName
import com.ataglance.walletglance.domain.utils.toColorWithName

data class CategoryPossibleColors(
    val olive: CategoryColors = CategoryColors.Olive,
    val camel: CategoryColors = CategoryColors.Camel,
    val pink: CategoryColors = CategoryColors.Pink,
    val green: CategoryColors = CategoryColors.Green,
    val red: CategoryColors = CategoryColors.Red,
    val lightBlue: CategoryColors = CategoryColors.LightBlue,
    val lavender: CategoryColors = CategoryColors.Lavender,
    val blue: CategoryColors = CategoryColors.Blue,
    val aquamarine: CategoryColors = CategoryColors.Aquamarine,
    val orange: CategoryColors = CategoryColors.Orange,
    val yellow: CategoryColors = CategoryColors.Yellow,
    val grayDefault: CategoryColors = CategoryColors.GrayDefault
) {

    private fun asList(): List<CategoryColors> {
        return listOf(
            olive,
            camel,
            pink,
            green,
            red,
            lightBlue,
            lavender,
            blue,
            aquamarine,
            orange,
            yellow,
            grayDefault
        )
    }

    fun getByName(name: String): CategoryColorWithName {
        return when (name) {
            CategoryColorName.Olive.name -> CategoryColorWithName(olive.name, olive.color)
            CategoryColorName.Camel.name -> CategoryColorWithName(camel.name, camel.color)
            CategoryColorName.Pink.name -> CategoryColorWithName(pink.name, pink.color)
            CategoryColorName.Green.name -> CategoryColorWithName(green.name, green.color)
            CategoryColorName.Red.name -> CategoryColorWithName(red.name, red.color)
            CategoryColorName.LightBlue.name -> CategoryColorWithName(lightBlue.name, lightBlue.color)
            CategoryColorName.Lavender.name -> CategoryColorWithName(lavender.name, lavender.color)
            CategoryColorName.Blue.name -> CategoryColorWithName(blue.name, blue.color)
            CategoryColorName.Aquamarine.name -> CategoryColorWithName(aquamarine.name, aquamarine.color)
            CategoryColorName.Orange.name -> CategoryColorWithName(orange.name, orange.color)
            CategoryColorName.Yellow.name -> CategoryColorWithName(yellow.name, yellow.color)
            CategoryColorName.GrayDefault.name -> CategoryColorWithName(grayDefault.name, grayDefault.color)
            else -> CategoryColorWithName(grayDefault.name, grayDefault.color)
        }
    }

    fun asColorWithNameList(theme: AppTheme?): List<ColorWithName> {
        return asList().map { it.toColorWithName(theme) }
    }

}
