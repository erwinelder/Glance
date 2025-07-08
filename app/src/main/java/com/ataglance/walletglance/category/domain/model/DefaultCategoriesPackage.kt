package com.ataglance.walletglance.category.domain.model

import android.content.Context
import com.ataglance.walletglance.R

data class DefaultCategoriesPackage(
    private val context: Context
) {

    fun getDefaultCategories(): GroupedCategoriesByType {
        return GroupedCategoriesByType(
            expense = listOf(
                GroupedCategories(
                    category = Category(
                        id = 1, type = CategoryType.Expense,
                        orderNum = 1, parentCategoryId = null,
                        name = context.getString(R.string.food_and_drinks),
                        icon = CategoryIcon.FoodAndDrinks,
                        color = CategoryColor.Olive
                    ),
                    subcategories = listOf(
                        Category(
                            id = 13, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 1,
                            name = context.getString(R.string.groceries),
                            icon = CategoryIcon.Groceries,
                            color = CategoryColor.Olive
                        ),
                        Category(
                            id = 14, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 1,
                            name = context.getString(R.string.restaurant),
                            icon = CategoryIcon.Restaurant,
                            color = CategoryColor.Olive
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 2, type = CategoryType.Expense,
                        orderNum = 2, parentCategoryId = null,
                        name = context.getString(R.string.housing),
                        icon = CategoryIcon.Housing,
                        color = CategoryColor.Camel
                    ),
                    subcategories = listOf(
                        Category(
                            id = 15, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 2,
                            name = context.getString(R.string.utilities),
                            icon = CategoryIcon.Housing,
                            color = CategoryColor.Camel
                        ),
                        Category(
                            id = 16, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 2,
                            name = context.getString(R.string.services),
                            icon = CategoryIcon.Housing,
                            color = CategoryColor.Camel
                        ),
                        Category(
                            id = 17, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 2,
                            name = context.getString(R.string.rent),
                            icon = CategoryIcon.Housing,
                            color = CategoryColor.Camel
                        ),
                        Category(
                            id = 18, type = CategoryType.Expense,
                            orderNum = 4, parentCategoryId = 2,
                            name = context.getString(R.string.mortgage),
                            icon = CategoryIcon.Housing,
                            color = CategoryColor.Camel
                        ),
                        Category(
                            id = 19, type = CategoryType.Expense,
                            orderNum = 5, parentCategoryId = 2,
                            name = context.getString(R.string.maintenance),
                            icon = CategoryIcon.Housing,
                            color = CategoryColor.Camel
                        ),
                        Category(
                            id = 20, type = CategoryType.Expense,
                            orderNum = 6, parentCategoryId = 2,
                            name = context.getString(R.string.purchase),
                            icon = CategoryIcon.HousingPurchase,
                            color = CategoryColor.Camel
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 3, type = CategoryType.Expense,
                        orderNum = 3, parentCategoryId = null,
                        name = context.getString(R.string.shopping),
                        icon = CategoryIcon.Shopping,
                        color = CategoryColor.Pink
                    ),
                    subcategories = listOf(
                        Category(
                            id = 21, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 3,
                            name = context.getString(R.string.clothes_and_shoes),
                            icon = CategoryIcon.Clothes,
                            color = CategoryColor.Pink
                        ),
                        Category(
                            id = 22, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 3,
                            name = context.getString(R.string.electronics_accessories),
                            icon = CategoryIcon.Shopping,
                            color = CategoryColor.Pink
                        ),
                        Category(
                            id = 23, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 3,
                            name = context.getString(R.string.cleaning_and_laundry),
                            icon = CategoryIcon.Shopping,
                            color = CategoryColor.Pink
                        ),
                        Category(
                            id = 24, type = CategoryType.Expense,
                            orderNum = 4, parentCategoryId = 3,
                            name = context.getString(R.string.health_and_beauty),
                            icon = CategoryIcon.Shopping,
                            color = CategoryColor.Pink
                        ),
                        Category(
                            id = 25, type = CategoryType.Expense,
                            orderNum = 5, parentCategoryId = 3,
                            name = context.getString(R.string.home_and_garden),
                            icon = CategoryIcon.Shopping,
                            color = CategoryColor.Pink
                        ),
                        Category(
                            id = 26, type = CategoryType.Expense,
                            orderNum = 6, parentCategoryId = 3,
                            name = context.getString(R.string.books),
                            icon = CategoryIcon.Shopping,
                            color = CategoryColor.Pink
                        ),
                        Category(
                            id = 27, type = CategoryType.Expense,
                            orderNum = 7, parentCategoryId = 3,
                            name = context.getString(R.string.joy_and_gifts),
                            icon = CategoryIcon.Shopping,
                            color = CategoryColor.Pink
                        ),
                        Category(
                            id = 28, type = CategoryType.Expense,
                            orderNum = 8, parentCategoryId = 3,
                            name = context.getString(R.string.kids), icon = CategoryIcon.Shopping,
                            color = CategoryColor.Pink
                        ),
                        Category(
                            id = 29, type = CategoryType.Expense,
                            orderNum = 9, parentCategoryId = 3,
                            name = context.getString(R.string.pets_and_animals),
                            icon = CategoryIcon.Shopping,
                            color = CategoryColor.Pink
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 4, type = CategoryType.Expense,
                        orderNum = 4, parentCategoryId = null,
                        name = context.getString(R.string.transport),
                        icon = CategoryIcon.Transport,
                        color = CategoryColor.Green
                    ),
                    subcategories = listOf(
                        Category(
                            id = 30, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 4,
                            name = context.getString(R.string.public_transport),
                            icon = CategoryIcon.Transport,
                            color = CategoryColor.Green
                        ),
                        Category(
                            id = 31, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 4,
                            name = context.getString(R.string.taxi),
                            icon = CategoryIcon.Transport,
                            color = CategoryColor.Green
                        ),
                        Category(
                            id = 32, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 4,
                            name = context.getString(R.string.business_trips),
                            icon = CategoryIcon.Transport,
                            color = CategoryColor.Green
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 5, type = CategoryType.Expense,
                        orderNum = 5, parentCategoryId = null,
                        name = context.getString(R.string.vehicle),
                        icon = CategoryIcon.Vehicle,
                        color = CategoryColor.Red
                    ),
                    subcategories = listOf(
                        Category(
                            id = 33, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 5,
                            name = context.getString(R.string.parking),
                            icon = CategoryIcon.Vehicle,
                            color = CategoryColor.Red
                        ),
                        Category(
                            id = 34, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 5,
                            name = context.getString(R.string.fuel),
                            icon = CategoryIcon.Vehicle,
                            color = CategoryColor.Red
                        ),
                        Category(
                            id = 35, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 5,
                            name = context.getString(R.string.leasing),
                            icon = CategoryIcon.Vehicle,
                            color = CategoryColor.Red
                        ),
                        Category(
                            id = 36, type = CategoryType.Expense,
                            orderNum = 4, parentCategoryId = 5,
                            name = context.getString(R.string.rent),
                            icon = CategoryIcon.Vehicle,
                            color = CategoryColor.Red
                        ),
                        Category(
                            id = 37, type = CategoryType.Expense,
                            orderNum = 5, parentCategoryId = 5,
                            name = context.getString(R.string.maintenance),
                            icon = CategoryIcon.Vehicle,
                            color = CategoryColor.Red
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 6, type = CategoryType.Expense,
                        orderNum = 6, parentCategoryId = null,
                        name = context.getString(R.string.digital_life),
                        icon = CategoryIcon.DigitalLife,
                        color = CategoryColor.LightBlue
                    ),
                    subcategories = listOf(
                        Category(
                            id = 38, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 6,
                            name = context.getString(R.string.internet_communication),
                            icon = CategoryIcon.DigitalLife,
                            color = CategoryColor.LightBlue
                        ),
                        Category(
                            id = 39, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 6,
                            name = context.getString(R.string.professional_subs),
                            icon = CategoryIcon.ProfessionalSubs,
                            color = CategoryColor.LightBlue
                        ),
                        Category(
                            id = 40, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 6,
                            name = context.getString(R.string.entertainment_subs),
                            icon = CategoryIcon.EntertainmentSubs,
                            color = CategoryColor.LightBlue
                        ),
                        Category(
                            id = 41, type = CategoryType.Expense,
                            orderNum = 4, parentCategoryId = 6,
                            name = context.getString(R.string.games),
                            icon = CategoryIcon.Games,
                            color = CategoryColor.LightBlue
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 7, type = CategoryType.Expense,
                        orderNum = 7, parentCategoryId = null,
                        name = context.getString(R.string.medicine),
                        icon = CategoryIcon.Medicine,
                        color = CategoryColor.Lavender
                    ),
                    subcategories = listOf(
                        Category(
                            id = 42, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 7,
                            name = context.getString(R.string.drugs_and_vitamins),
                            icon = CategoryIcon.Medicine,
                            color = CategoryColor.Lavender
                        ),
                        Category(
                            id = 43, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 7,
                            name = context.getString(R.string.insurance),
                            icon = CategoryIcon.Medicine,
                            color = CategoryColor.Lavender
                        ),
                        Category(
                            id = 44, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 7,
                            name = context.getString(R.string.doctor),
                            icon = CategoryIcon.Medicine,
                            color = CategoryColor.Lavender
                        ),
                        Category(
                            id = 45, type = CategoryType.Expense,
                            orderNum = 4, parentCategoryId = 7,
                            name = context.getString(R.string.teeth_care),
                            icon = CategoryIcon.Medicine,
                            color = CategoryColor.Lavender
                        ),
                        Category(
                            id = 46, type = CategoryType.Expense,
                            orderNum = 5, parentCategoryId = 7,
                            name = context.getString(R.string.vaccination),
                            icon = CategoryIcon.Medicine,
                            color = CategoryColor.Lavender
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 8, type = CategoryType.Expense,
                        orderNum = 8, parentCategoryId = null,
                        name = context.getString(R.string.education),
                        icon = CategoryIcon.Education,
                        color = CategoryColor.Blue
                    ),
                    subcategories = listOf(
                        Category(
                            id = 47, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 8,
                            name = context.getString(R.string.school_and_university),
                            icon = CategoryIcon.Education,
                            color = CategoryColor.Blue
                        ),
                        Category(
                            id = 48, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 8,
                            name = context.getString(R.string.courses),
                            icon = CategoryIcon.Education,
                            color = CategoryColor.Blue
                        ),
                        Category(
                            id = 49, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 8,
                            name = context.getString(R.string.studding_materials),
                            icon = CategoryIcon.Education,
                            color = CategoryColor.Blue
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 9, type = CategoryType.Expense,
                        orderNum = 9, parentCategoryId = null,
                        name = context.getString(R.string.travels),
                        icon = CategoryIcon.Travels,
                        color = CategoryColor.Aquamarine
                    ),
                    subcategories = listOf(
                        Category(
                            id = 50, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 9,
                            name = context.getString(R.string.tickets),
                            icon = CategoryIcon.TravelTickets,
                            color = CategoryColor.Aquamarine
                        ),
                        Category(
                            id = 51, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 9,
                            name = context.getString(R.string.accommodation),
                            icon = CategoryIcon.Accommodation,
                            color = CategoryColor.Aquamarine
                        ),
                        Category(
                            id = 52, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 9,
                            name = context.getString(R.string.food_and_drinks),
                            icon = CategoryIcon.FoodAndDrinks,
                            color = CategoryColor.Aquamarine
                        ),
                        Category(
                            id = 53, type = CategoryType.Expense,
                            orderNum = 4, parentCategoryId = 9,
                            name = context.getString(R.string.transport),
                            icon = CategoryIcon.Transport,
                            color = CategoryColor.Aquamarine
                        ),
                        Category(
                            id = 54, type = CategoryType.Expense,
                            orderNum = 5, parentCategoryId = 9,
                            name = context.getString(R.string.shopping),
                            icon = CategoryIcon.Shopping,
                            color = CategoryColor.Aquamarine
                        ),
                        Category(
                            id = 55, type = CategoryType.Expense,
                            orderNum = 6, parentCategoryId = 9,
                            name = context.getString(R.string.entertainment),
                            icon = CategoryIcon.Entertainment,
                            color = CategoryColor.Aquamarine
                        ),
                        Category(
                            id = 56, type = CategoryType.Expense,
                            orderNum = 7, parentCategoryId = 9,
                            name = context.getString(R.string.other_category),
                            icon = CategoryIcon.Travels,
                            color = CategoryColor.Aquamarine
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 10, type = CategoryType.Expense,
                        orderNum = 10, parentCategoryId = null,
                        name = context.getString(R.string.entertainment),
                        icon = CategoryIcon.Entertainment,
                        color = CategoryColor.Orange
                    ),
                    subcategories = listOf(
                        Category(
                            id = 57, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 10,
                            name = context.getString(R.string.hobbies),
                            icon = CategoryIcon.Entertainment,
                            color = CategoryColor.Orange
                        ),
                        Category(
                            id = 58, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 10,
                            name = context.getString(R.string.cinema_theater_concerts),
                            icon = CategoryIcon.CinemaTheater,
                            color = CategoryColor.Orange
                        ),
                        Category(
                            id = 59, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 10,
                            name = context.getString(R.string.sport),
                            icon = CategoryIcon.Entertainment,
                            color = CategoryColor.Orange
                        ),
                        Category(
                            id = 60, type = CategoryType.Expense,
                            orderNum = 4, parentCategoryId = 10,
                            name = context.getString(R.string.events_hanging_out),
                            icon = CategoryIcon.Entertainment,
                            color = CategoryColor.Orange
                        ),
                        Category(
                            id = 61, type = CategoryType.Expense,
                            orderNum = 5, parentCategoryId = 10,
                            name = context.getString(R.string.alcohol_and_smoking),
                            icon = CategoryIcon.Entertainment,
                            color = CategoryColor.Orange
                        ),
                        Category(
                            id = 62, type = CategoryType.Expense,
                            orderNum = 6, parentCategoryId = 10,
                            name = context.getString(R.string.gifts_and_charity),
                            icon = CategoryIcon.Gifts,
                            color = CategoryColor.Orange
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 11, type = CategoryType.Expense,
                        orderNum = 11, parentCategoryId = null,
                        name = context.getString(R.string.investments),
                        icon = CategoryIcon.Investments,
                        color = CategoryColor.Yellow
                    ),
                    subcategories = listOf(
                        Category(
                            id = 63, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 11,
                            name = context.getString(R.string.shares),
                            icon = CategoryIcon.Investments,
                            color = CategoryColor.Yellow
                        ),
                        Category(
                            id = 64, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 11,
                            name = context.getString(R.string.cryptocurrencies),
                            icon = CategoryIcon.Investments,
                            color = CategoryColor.Yellow
                        ),
                        Category(
                            id = 65, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 11,
                            name = context.getString(R.string.realty),
                            icon = CategoryIcon.Investments,
                            color = CategoryColor.Yellow
                        ),
                        Category(
                            id = 66, type = CategoryType.Expense,
                            orderNum = 4, parentCategoryId = 11,
                            name = context.getString(R.string.savings),
                            icon = CategoryIcon.Investments,
                            color = CategoryColor.Yellow
                        )
                    )
                ),
                GroupedCategories(
                    category = Category(
                        id = 12, type = CategoryType.Expense,
                        orderNum = 12, parentCategoryId = null,
                        name = context.getString(R.string.other_category),
                        icon = CategoryIcon.Other,
                        color = CategoryColor.GrayDefault
                    ),
                    subcategories = listOf(
                        Category(
                            id = 67, type = CategoryType.Expense,
                            orderNum = 1, parentCategoryId = 12,
                            name = context.getString(R.string.transfers),
                            icon = CategoryIcon.Transfers,
                            color = CategoryColor.GrayDefault
                        ),
                        Category(
                            id = 68, type = CategoryType.Expense,
                            orderNum = 2, parentCategoryId = 12,
                            name = context.getString(R.string.missing),
                            icon = CategoryIcon.Missing,
                            color = CategoryColor.GrayDefault
                        ),
                        Category(
                            id = 69, type = CategoryType.Expense,
                            orderNum = 3, parentCategoryId = 12,
                            name = context.getString(R.string.post_services),
                            icon = CategoryIcon.Missing,
                            color = CategoryColor.GrayDefault
                        ),
                        Category(
                            id = 70, type = CategoryType.Expense,
                            orderNum = 4, parentCategoryId = 12,
                            name = context.getString(R.string.public_toilets),
                            icon = CategoryIcon.Other,
                            color = CategoryColor.GrayDefault
                        ),
                        Category(
                            id = 71, type = CategoryType.Expense,
                            orderNum = 5, parentCategoryId = 12,
                            name = context.getString(R.string.other_category),
                            icon = CategoryIcon.Other,
                            color = CategoryColor.GrayDefault
                        )
                    )
                )
            ),
            income = listOf(
                GroupedCategories(
                    category = Category(
                        id = 72, type = CategoryType.Income,
                        orderNum = 1, parentCategoryId = null,
                        name = context.getString(R.string.salary),
                        icon = CategoryIcon.Salary,
                        color = CategoryColor.Green
                    ),
                    subcategories = emptyList()
                ),
                GroupedCategories(
                    category = Category(
                        id = 73, type = CategoryType.Income,
                        orderNum = 2, parentCategoryId = null,
                        name = context.getString(R.string.scholarship),
                        icon = CategoryIcon.Scholarship,
                        color = CategoryColor.Blue
                    ),
                    subcategories = emptyList()
                ),
                GroupedCategories(
                    category = Category(
                        id = 74, type = CategoryType.Income,
                        orderNum = 3, parentCategoryId = null,
                        name = context.getString(R.string.sale),
                        icon = CategoryIcon.Sales,
                        color = CategoryColor.Orange
                    ),
                    subcategories = emptyList()
                ),
                GroupedCategories(
                    category = Category(
                        id = 75, type = CategoryType.Income,
                        orderNum = 4, parentCategoryId = null,
                        name = context.getString(R.string.rent),
                        icon = CategoryIcon.Housing,
                        color = CategoryColor.Camel
                    ),
                    subcategories = emptyList()
                ),
                GroupedCategories(
                    category = Category(
                        id = 76, type = CategoryType.Income,
                        orderNum = 5, parentCategoryId = null,
                        name = context.getString(R.string.refunds),
                        icon = CategoryIcon.Refunds,
                        color = CategoryColor.LightBlue
                    ),
                    subcategories = emptyList()
                ),
                GroupedCategories(
                    category = Category(
                        id = 77, type = CategoryType.Income,
                        orderNum = 6, parentCategoryId = null,
                        name = context.getString(R.string.investments),
                        icon = CategoryIcon.Investments,
                        color = CategoryColor.Yellow
                    ),
                    subcategories = emptyList()
                ),
                GroupedCategories(
                    category = Category(
                        id = 78, type = CategoryType.Income,
                        orderNum = 7, parentCategoryId = null,
                        name = context.getString(R.string.gifts),
                        icon = CategoryIcon.Gifts,
                        color = CategoryColor.Lavender
                    ),
                    subcategories = emptyList()
                ),
                GroupedCategories(
                    category = Category(
                        id = 79, type = CategoryType.Income,
                        orderNum = 8, parentCategoryId = null,
                        name = context.getString(R.string.transfers),
                        icon = CategoryIcon.Transfers,
                        color = CategoryColor.GrayDefault
                    ),
                    subcategories = emptyList()
                )
            )
        )
    }

}