package com.ataglance.walletglance.ui.viewmodels

import android.content.Context
import com.ataglance.walletglance.R
import com.ataglance.walletglance.domain.entities.Category

data class DefaultCategoriesPackage(
    val context: Context
) {

    fun getDefaultCategories(): CategoriesUiState {
        return CategoriesUiState(
            parentCategories = ParentCategoriesLists(
                expense = listOf(
                    Category(
                        id = 1, type = '-', rank = 'c', orderNum = 1, parentCategoryId = 1,
                        name = context.getString(R.string.food_and_drinks), iconName = "food_and_drinks",
                        colorName = CategoryColors.Olive(null).color.name
                    ),
                    Category(
                        id = 2, type = '-', rank = 'c', orderNum = 2, parentCategoryId = 2,
                        name = context.getString(R.string.housing), iconName = "housing",
                        colorName = CategoryColors.Camel(null).color.name
                    ),
                    Category(
                        id = 3, type = '-', rank = 'c', orderNum = 3, parentCategoryId = 3,
                        name = context.getString(R.string.shopping), iconName = "shopping",
                        colorName = CategoryColors.Pink(null).color.name
                    ),
                    Category(
                        id = 4, type = '-', rank = 'c', orderNum = 4, parentCategoryId = 4,
                        name = context.getString(R.string.transport), iconName = "transport",
                        colorName = CategoryColors.Green(null).color.name
                    ),
                    Category(
                        id = 5, type = '-', rank = 'c', orderNum = 5, parentCategoryId = 5,
                        name = context.getString(R.string.vehicle), iconName = "vehicle",
                        colorName = CategoryColors.Red(null).color.name
                    ),
                    Category(
                        id = 6, type = '-', rank = 'c', orderNum = 6, parentCategoryId = 6,
                        name = context.getString(R.string.digital_life), iconName = "digital_life",
                        colorName = CategoryColors.LightBlue(null).color.name
                    ),
                    Category(
                        id = 7, type = '-', rank = 'c', orderNum = 7, parentCategoryId = 7,
                        name = context.getString(R.string.medicine), iconName = "medicine",
                        colorName = CategoryColors.Lavender(null).color.name
                    ),
                    Category(
                        id = 8, type = '-', rank = 'c', orderNum = 8, parentCategoryId = 8,
                        name = context.getString(R.string.education), iconName = "education",
                        colorName = CategoryColors.Blue(null).color.name
                    ),
                    Category(
                        id = 9, type = '-', rank = 'c', orderNum = 9, parentCategoryId = 9,
                        name = context.getString(R.string.travels), iconName = "travels",
                        colorName = CategoryColors.Aquamarine(null).color.name
                    ),
                    Category(
                        id = 10, type = '-', rank = 'c', orderNum = 10, parentCategoryId = 10,
                        name = context.getString(R.string.entertainment), iconName = "entertainment",
                        colorName = CategoryColors.Orange(null).color.name
                    ),
                    Category(
                        id = 11, type = '-', rank = 'c', orderNum = 11, parentCategoryId = 11,
                        name = context.getString(R.string.investments), iconName = "investments",
                        colorName = CategoryColors.Yellow(null).color.name
                    ),
                    Category(
                        id = 12, type = '-', rank = 'c', orderNum = 12, parentCategoryId = 12,
                        name = context.getString(R.string.other_category), iconName = "other",
                        colorName = CategoryColors.GrayDefault(null).color.name
                    )
                ),
                income = listOf(
                    Category(
                        id = 70, type = '+', rank = 'c', orderNum = 1, parentCategoryId = null,
                        name = context.getString(R.string.salary), iconName = "salary",
                        colorName = CategoryColors.Green(null).color.name
                    ),
                    Category(
                        id = 71, type = '+', rank = 'c', orderNum = 2, parentCategoryId = null,
                        name = context.getString(R.string.scholarship), iconName = "scholarship",
                        colorName = CategoryColors.Blue(null).color.name
                    ),
                    Category(
                        id = 72, type = '+', rank = 'c', orderNum = 3, parentCategoryId = null,
                        name = context.getString(R.string.sale), iconName = "sales",
                        colorName = CategoryColors.Orange(null).color.name
                    ),
                    Category(
                        id = 73, type = '+', rank = 'c', orderNum = 4, parentCategoryId = null,
                        name = context.getString(R.string.rent), iconName = "housing",
                        colorName = CategoryColors.Camel(null).color.name
                    ),
                    Category(
                        id = 74, type = '+', rank = 'c', orderNum = 5, parentCategoryId = null,
                        name = context.getString(R.string.refunds), iconName = "refunds",
                        colorName = CategoryColors.LightBlue(null).color.name
                    ),
                    Category(
                        id = 75, type = '+', rank = 'c', orderNum = 6, parentCategoryId = null,
                        name = context.getString(R.string.investments), iconName = "investments",
                        colorName = CategoryColors.Yellow(null).color.name
                    ),
                    Category(
                        id = 76, type = '+', rank = 'c', orderNum = 7, parentCategoryId = null,
                        name = context.getString(R.string.gifts), iconName = "gifts",
                        colorName = CategoryColors.Lavender(null).color.name
                    ),
                    Category(
                        id = 77, type = '+', rank = 'c', orderNum = 8, parentCategoryId = null,
                        name = context.getString(R.string.transfers), iconName = "transfers",
                        colorName = CategoryColors.GrayDefault(null).color.name
                    )
                )
            ),
            subcategories = SubcategoriesLists(
                expense = listOf(
                listOf(
                    Category(
                        id = 13, type = '-', rank = 's', orderNum = 1, parentCategoryId = 1,
                        name = context.getString(R.string.groceries), iconName = "groceries",
                        colorName = CategoryColors.Olive(null).color.name
                    ),
                    Category(
                        id = 14, type = '-', rank = 's', orderNum = 2, parentCategoryId = 1,
                        name = context.getString(R.string.restaurant), iconName = "restaurant",
                        colorName = CategoryColors.Olive(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 15, type = '-', rank = 's', orderNum = 1, parentCategoryId = 2,
                        name = context.getString(R.string.utilities), iconName = "housing",
                        colorName = CategoryColors.Camel(null).color.name
                    ),
                    Category(
                        id = 16, type = '-', rank = 's', orderNum = 2, parentCategoryId = 2,
                        name = context.getString(R.string.services), iconName = "housing",
                        colorName = CategoryColors.Camel(null).color.name
                    ),
                    Category(
                        id = 17, type = '-', rank = 's', orderNum = 3, parentCategoryId = 2,
                        name = context.getString(R.string.rent), iconName = "housing",
                        colorName = CategoryColors.Camel(null).color.name
                    ),
                    Category(
                        id = 18, type = '-', rank = 's', orderNum = 4, parentCategoryId = 2,
                        name = context.getString(R.string.mortgage), iconName = "housing",
                        colorName = CategoryColors.Camel(null).color.name
                    ),
                    Category(
                        id = 19, type = '-', rank = 's', orderNum = 5, parentCategoryId = 2,
                        name = context.getString(R.string.maintenance), iconName = "housing",
                        colorName = CategoryColors.Camel(null).color.name
                    ),
                    Category(
                        id = 20, type = '-', rank = 's', orderNum = 6, parentCategoryId = 2,
                        name = context.getString(R.string.purchase), iconName = "housing_purchase",
                        colorName = CategoryColors.Camel(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 21, type = '-', rank = 's', orderNum = 1, parentCategoryId = 3,
                        name = context.getString(R.string.clothes_and_shoes), iconName = "clothes",
                        colorName = CategoryColors.Pink(null).color.name
                    ),
                    Category(
                        id = 22, type = '-', rank = 's', orderNum = 2, parentCategoryId = 3,
                        name = context.getString(R.string.electronics_accessories), iconName = "shopping",
                        colorName = CategoryColors.Pink(null).color.name
                    ),
                    Category(
                        id = 23, type = '-', rank = 's', orderNum = 3, parentCategoryId = 3,
                        name = context.getString(R.string.cleaning_and_laundry), iconName = "shopping",
                        colorName = CategoryColors.Pink(null).color.name
                    ),
                    Category(
                        id = 24, type = '-', rank = 's', orderNum = 4, parentCategoryId = 3,
                        name = context.getString(R.string.health_and_beauty), iconName = "shopping",
                        colorName = CategoryColors.Pink(null).color.name
                    ),
                    Category(
                        id = 25, type = '-', rank = 's', orderNum = 5, parentCategoryId = 3,
                        name = context.getString(R.string.home_and_garden), iconName = "shopping",
                        colorName = CategoryColors.Pink(null).color.name
                    ),
                    Category(
                        id = 26, type = '-', rank = 's', orderNum = 6, parentCategoryId = 3,
                        name = context.getString(R.string.books), iconName = "shopping",
                        colorName = CategoryColors.Pink(null).color.name
                    ),
                    Category(
                        id = 27, type = '-', rank = 's', orderNum = 7, parentCategoryId = 3,
                        name = context.getString(R.string.joy_and_gifts), iconName = "shopping",
                        colorName = CategoryColors.Pink(null).color.name
                    ),
                    Category(
                        id = 28, type = '-', rank = 's', orderNum = 8, parentCategoryId = 3,
                        name = context.getString(R.string.kids), iconName = "shopping",
                        colorName = CategoryColors.Pink(null).color.name
                    ),
                    Category(
                        id = 29, type = '-', rank = 's', orderNum = 9, parentCategoryId = 3,
                        name = context.getString(R.string.pets_and_animals), iconName = "shopping",
                        colorName = CategoryColors.Pink(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 30, type = '-', rank = 's', orderNum = 1, parentCategoryId = 4,
                        name = context.getString(R.string.public_transport), iconName = "transport",
                        colorName = CategoryColors.Green(null).color.name
                    ),
                    Category(
                        id = 31, type = '-', rank = 's', orderNum = 2, parentCategoryId = 4,
                        name = context.getString(R.string.taxi), iconName = "transport",
                        colorName = CategoryColors.Green(null).color.name
                    ),
                    Category(
                        id = 32, type = '-', rank = 's', orderNum = 3, parentCategoryId = 4,
                        name = context.getString(R.string.business_trips), iconName = "transport",
                        colorName = CategoryColors.Green(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 33, type = '-', rank = 's', orderNum = 1, parentCategoryId = 5,
                        name = context.getString(R.string.parking), iconName = "vehicle",
                        colorName = CategoryColors.Red(null).color.name
                    ),
                    Category(
                        id = 34, type = '-', rank = 's', orderNum = 2, parentCategoryId = 5,
                        name = context.getString(R.string.fuel), iconName = "vehicle",
                        colorName = CategoryColors.Red(null).color.name
                    ),
                    Category(
                        id = 35, type = '-', rank = 's', orderNum = 3, parentCategoryId = 5,
                        name = context.getString(R.string.leasing), iconName = "vehicle",
                        colorName = CategoryColors.Red(null).color.name
                    ),
                    Category(
                        id = 36, type = '-', rank = 's', orderNum = 4, parentCategoryId = 5,
                        name = context.getString(R.string.rent), iconName = "vehicle",
                        colorName = CategoryColors.Red(null).color.name
                    ),
                    Category(
                        id = 37, type = '-', rank = 's', orderNum = 5, parentCategoryId = 5,
                        name = context.getString(R.string.maintenance), iconName = "vehicle",
                        colorName = CategoryColors.Red(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 38, type = '-', rank = 's', orderNum = 1, parentCategoryId = 6,
                        name = context.getString(R.string.internet_communication), iconName = "digital_life",
                        colorName = CategoryColors.LightBlue(null).color.name
                    ),
                    Category(
                        id = 39, type = '-', rank = 's', orderNum = 2, parentCategoryId = 6,
                        name = context.getString(R.string.professional_subs), iconName = "digital_life",
                        colorName = CategoryColors.LightBlue(null).color.name
                    ),
                    Category(
                        id = 40, type = '-', rank = 's', orderNum = 3, parentCategoryId = 6,
                        name = context.getString(R.string.entertainment_subs), iconName = "entertainment_subs",
                        colorName = CategoryColors.LightBlue(null).color.name
                    ),
                    Category(
                        id = 41, type = '-', rank = 's', orderNum = 4, parentCategoryId = 6,
                        name = context.getString(R.string.games), iconName = "digital_life",
                        colorName = CategoryColors.LightBlue(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 42, type = '-', rank = 's', orderNum = 1, parentCategoryId = 7,
                        name = context.getString(R.string.drugs_and_vitamins), iconName = "medicine",
                        colorName = CategoryColors.Lavender(null).color.name
                    ),
                    Category(
                        id = 43, type = '-', rank = 's', orderNum = 2, parentCategoryId = 7,
                        name = context.getString(R.string.insurance), iconName = "medicine",
                        colorName = CategoryColors.Lavender(null).color.name
                    ),
                    Category(
                        id = 44, type = '-', rank = 's', orderNum = 3, parentCategoryId = 7,
                        name = context.getString(R.string.doctor), iconName = "medicine",
                        colorName = CategoryColors.Lavender(null).color.name
                    ),
                    Category(
                        id = 45, type = '-', rank = 's', orderNum = 4, parentCategoryId = 7,
                        name = context.getString(R.string.vaccination), iconName = "medicine",
                        colorName = CategoryColors.Lavender(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 46, type = '-', rank = 's', orderNum = 1, parentCategoryId = 8,
                        name = context.getString(R.string.school_and_university), iconName = "education",
                        colorName = CategoryColors.Blue(null).color.name
                    ),
                    Category(
                        id = 47, type = '-', rank = 's', orderNum = 2, parentCategoryId = 8,
                        name = context.getString(R.string.courses), iconName = "education",
                        colorName = CategoryColors.Blue(null).color.name
                    ),
                    Category(
                        id = 48, type = '-', rank = 's', orderNum = 3, parentCategoryId = 8,
                        name = context.getString(R.string.studding_materials), iconName = "education",
                        colorName = CategoryColors.Blue(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 49, type = '-', rank = 's', orderNum = 1, parentCategoryId = 9,
                        name = context.getString(R.string.tickets), iconName = "travels",
                        colorName = CategoryColors.Aquamarine(null).color.name
                    ),
                    Category(
                        id = 50, type = '-', rank = 's', orderNum = 2, parentCategoryId = 9,
                        name = context.getString(R.string.accommodation), iconName = "travels",
                        colorName = CategoryColors.Aquamarine(null).color.name
                    ),
                    Category(
                        id = 51, type = '-', rank = 's', orderNum = 3, parentCategoryId = 9,
                        name = context.getString(R.string.food_and_drinks), iconName = "travels",
                        colorName = CategoryColors.Aquamarine(null).color.name
                    ),
                    Category(
                        id = 52, type = '-', rank = 's', orderNum = 4, parentCategoryId = 9,
                        name = context.getString(R.string.transport), iconName = "travels",
                        colorName = CategoryColors.Aquamarine(null).color.name
                    ),
                    Category(
                        id = 53, type = '-', rank = 's', orderNum = 5, parentCategoryId = 9,
                        name = context.getString(R.string.shopping), iconName = "travels",
                        colorName = CategoryColors.Aquamarine(null).color.name
                    ),
                    Category(
                        id = 54, type = '-', rank = 's', orderNum = 6, parentCategoryId = 9,
                        name = context.getString(R.string.entertainment), iconName = "travels",
                        colorName = CategoryColors.Aquamarine(null).color.name
                    ),
                    Category(
                        id = 55, type = '-', rank = 's', orderNum = 7, parentCategoryId = 9,
                        name = context.getString(R.string.other_category), iconName = "travels",
                        colorName = CategoryColors.Aquamarine(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 56, type = '-', rank = 's', orderNum = 1, parentCategoryId = 10,
                        name = context.getString(R.string.hobbies), iconName = "entertainment",
                        colorName = CategoryColors.Orange(null).color.name
                    ),
                    Category(
                        id = 57, type = '-', rank = 's', orderNum = 2, parentCategoryId = 10,
                        name = context.getString(R.string.cinema_theater_concerts), iconName = "entertainment",
                        colorName = CategoryColors.Orange(null).color.name
                    ),
                    Category(
                        id = 58, type = '-', rank = 's', orderNum = 3, parentCategoryId = 10,
                        name = context.getString(R.string.sport), iconName = "entertainment",
                        colorName = CategoryColors.Orange(null).color.name
                    ),
                    Category(
                        id = 59, type = '-', rank = 's', orderNum = 4, parentCategoryId = 10,
                        name = context.getString(R.string.events_hanging_out), iconName = "entertainment",
                        colorName = CategoryColors.Orange(null).color.name
                    ),
                    Category(
                        id = 60, type = '-', rank = 's', orderNum = 5, parentCategoryId = 10,
                        name = context.getString(R.string.alcohol_and_smoking), iconName = "entertainment",
                        colorName = CategoryColors.Orange(null).color.name
                    ),
                    Category(
                        id = 61, type = '-', rank = 's', orderNum = 6, parentCategoryId = 10,
                        name = context.getString(R.string.gifts_and_charity), iconName = "entertainment",
                        colorName = CategoryColors.Orange(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 62, type = '-', rank = 's', orderNum = 1, parentCategoryId = 11,
                        name = context.getString(R.string.shares), iconName = "investments",
                        colorName = CategoryColors.Yellow(null).color.name
                    ),
                    Category(
                        id = 63, type = '-', rank = 's', orderNum = 2, parentCategoryId = 11,
                        name = context.getString(R.string.cryptocurrencies), iconName = "investments",
                        colorName = CategoryColors.Yellow(null).color.name
                    ),
                    Category(
                        id = 64, type = '-', rank = 's', orderNum = 3, parentCategoryId = 11,
                        name = context.getString(R.string.realty), iconName = "investments",
                        colorName = CategoryColors.Yellow(null).color.name
                    ),
                    Category(
                        id = 65, type = '-', rank = 's', orderNum = 4, parentCategoryId = 11,
                        name = context.getString(R.string.savings), iconName = "investments",
                        colorName = CategoryColors.Yellow(null).color.name
                    )
                ),
                listOf(
                    Category(
                        id = 66, type = '-', rank = 's', orderNum = 1, parentCategoryId = 12,
                        name = context.getString(R.string.transfers), iconName = "transfers",
                        colorName = CategoryColors.GrayDefault(null).color.name
                    ),
                    Category(
                        id = 67, type = '-', rank = 's', orderNum = 2, parentCategoryId = 12,
                        name = context.getString(R.string.missing), iconName = "missing",
                        colorName = CategoryColors.GrayDefault(null).color.name
                    ),
                    Category(
                        id = 68, type = '-', rank = 's', orderNum = 3, parentCategoryId = 12,
                        name = context.getString(R.string.public_toilets), iconName = "other",
                        colorName = CategoryColors.GrayDefault(null).color.name
                    ),
                    Category(
                        id = 69, type = '-', rank = 's', orderNum = 4, parentCategoryId = 12,
                        name = context.getString(R.string.other_category), iconName = "other",
                        colorName = CategoryColors.GrayDefault(null).color.name
                    )
                )
            ),
                income = listOf(
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList()
            )
            )
        )
    }

    fun translateDefaultCategories(
        categoriesUiState: CategoriesUiState
    ): List<Category> {
        val currentCategories = CategoryController().concatenateCategoryLists(
            categoriesUiState.parentCategories,
            categoriesUiState.subcategories
        )
        val translatedDefaultCategories = getDefaultCategories().let {
            CategoryController().concatenateCategoryLists(it.parentCategories, it.subcategories)
        }
        val currentCategoriesToTranslate = currentCategories.filter { currentCategory ->
            translatedDefaultCategories.find { it.id == currentCategory.id } != null
        }

        return currentCategoriesToTranslate.map { currentCategory ->
            translatedDefaultCategories.find {
                it.id == currentCategory.id
            }?.let{
                it.copy(
                    id = currentCategory.id,
                    type = currentCategory.type,
                    rank = currentCategory.rank,
                    orderNum = currentCategory.orderNum,
                    parentCategoryId = currentCategory.parentCategoryId,
                    name = it.name,
                    iconName = currentCategory.iconName,
                    colorName = currentCategory.colorName
                )
            } ?: currentCategory
        }
    }

}