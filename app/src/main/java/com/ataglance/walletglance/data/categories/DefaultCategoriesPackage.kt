package com.ataglance.walletglance.data.categories

import android.content.Context
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.categories.color.CategoryColors
import com.ataglance.walletglance.data.categories.icons.CategoryIcon
import com.ataglance.walletglance.ui.utils.findById
import com.ataglance.walletglance.ui.utils.toCategoryColorWithName

data class DefaultCategoriesPackage(
    val context: Context
) {

    fun getDefaultCategories(): CategoriesWithSubcategories {
        return CategoriesWithSubcategories(
            expense = listOf(
                CategoryWithSubcategories(
                    category = Category(
                        id = 1, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 1, parentCategoryId = null,
                        name = context.getString(R.string.food_and_drinks),
                        icon = CategoryIcon.FoodAndDrinks,
                        colorWithName = CategoryColors.Olive.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 13, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 1,
                            name = context.getString(R.string.groceries),
                            icon = CategoryIcon.Groceries,
                            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
                        ),
                        Category(
                            id = 14, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 1,
                            name = context.getString(R.string.restaurant),
                            icon = CategoryIcon.Restaurant,
                            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 2, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 2, parentCategoryId = null,
                        name = context.getString(R.string.housing),
                        icon = CategoryIcon.Housing,
                        colorWithName = CategoryColors.Camel.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 15, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 2,
                            name = context.getString(R.string.utilities),
                            icon = CategoryIcon.Housing,
                            colorWithName = CategoryColors.Camel.toCategoryColorWithName()
                        ),
                        Category(
                            id = 16, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 2,
                            name = context.getString(R.string.services),
                            icon = CategoryIcon.Housing,
                            colorWithName = CategoryColors.Camel.toCategoryColorWithName()
                        ),
                        Category(
                            id = 17, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 2,
                            name = context.getString(R.string.rent),
                            icon = CategoryIcon.Housing,
                            colorWithName = CategoryColors.Camel.toCategoryColorWithName()
                        ),
                        Category(
                            id = 18, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 4, parentCategoryId = 2,
                            name = context.getString(R.string.mortgage),
                            icon = CategoryIcon.Housing,
                            colorWithName = CategoryColors.Camel.toCategoryColorWithName()
                        ),
                        Category(
                            id = 19, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 5, parentCategoryId = 2,
                            name = context.getString(R.string.maintenance),
                            icon = CategoryIcon.Housing,
                            colorWithName = CategoryColors.Camel.toCategoryColorWithName()
                        ),
                        Category(
                            id = 20, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 6, parentCategoryId = 2,
                            name = context.getString(R.string.purchase),
                            icon = CategoryIcon.HousingPurchase,
                            colorWithName = CategoryColors.Camel.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 3, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 3, parentCategoryId = null,
                        name = context.getString(R.string.shopping),
                        icon = CategoryIcon.Shopping,
                        colorWithName = CategoryColors.Pink.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 21, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 3,
                            name = context.getString(R.string.clothes_and_shoes),
                            icon = CategoryIcon.Clothes,
                            colorWithName = CategoryColors.Pink.toCategoryColorWithName()
                        ),
                        Category(
                            id = 22, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 3,
                            name = context.getString(R.string.electronics_accessories),
                            icon = CategoryIcon.Shopping,
                            colorWithName = CategoryColors.Pink.toCategoryColorWithName()
                        ),
                        Category(
                            id = 23, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 3,
                            name = context.getString(R.string.cleaning_and_laundry),
                            icon = CategoryIcon.Shopping,
                            colorWithName = CategoryColors.Pink.toCategoryColorWithName()
                        ),
                        Category(
                            id = 24, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 4, parentCategoryId = 3,
                            name = context.getString(R.string.health_and_beauty),
                            icon = CategoryIcon.Shopping,
                            colorWithName = CategoryColors.Pink.toCategoryColorWithName()
                        ),
                        Category(
                            id = 25, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 5, parentCategoryId = 3,
                            name = context.getString(R.string.home_and_garden),
                            icon = CategoryIcon.Shopping,
                            colorWithName = CategoryColors.Pink.toCategoryColorWithName()
                        ),
                        Category(
                            id = 26, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 6, parentCategoryId = 3,
                            name = context.getString(R.string.books),
                            icon = CategoryIcon.Shopping,
                            colorWithName = CategoryColors.Pink.toCategoryColorWithName()
                        ),
                        Category(
                            id = 27, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 7, parentCategoryId = 3,
                            name = context.getString(R.string.joy_and_gifts),
                            icon = CategoryIcon.Shopping,
                            colorWithName = CategoryColors.Pink.toCategoryColorWithName()
                        ),
                        Category(
                            id = 28, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 8, parentCategoryId = 3,
                            name = context.getString(R.string.kids), icon = CategoryIcon.Shopping,
                            colorWithName = CategoryColors.Pink.toCategoryColorWithName()
                        ),
                        Category(
                            id = 29, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 9, parentCategoryId = 3,
                            name = context.getString(R.string.pets_and_animals),
                            icon = CategoryIcon.Shopping,
                            colorWithName = CategoryColors.Pink.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 4, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 4, parentCategoryId = null,
                        name = context.getString(R.string.transport),
                        icon = CategoryIcon.Transport,
                        colorWithName = CategoryColors.Green.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 30, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 4,
                            name = context.getString(R.string.public_transport),
                            icon = CategoryIcon.Transport,
                            colorWithName = CategoryColors.Green.toCategoryColorWithName()
                        ),
                        Category(
                            id = 31, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 4,
                            name = context.getString(R.string.taxi),
                            icon = CategoryIcon.Transport,
                            colorWithName = CategoryColors.Green.toCategoryColorWithName()
                        ),
                        Category(
                            id = 32, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 4,
                            name = context.getString(R.string.business_trips),
                            icon = CategoryIcon.Transport,
                            colorWithName = CategoryColors.Green.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 5, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 5, parentCategoryId = null,
                        name = context.getString(R.string.vehicle),
                        icon = CategoryIcon.Vehicle,
                        colorWithName = CategoryColors.Red.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 33, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 5,
                            name = context.getString(R.string.parking),
                            icon = CategoryIcon.Vehicle,
                            colorWithName = CategoryColors.Red.toCategoryColorWithName()
                        ),
                        Category(
                            id = 34, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 5,
                            name = context.getString(R.string.fuel),
                            icon = CategoryIcon.Vehicle,
                            colorWithName = CategoryColors.Red.toCategoryColorWithName()
                        ),
                        Category(
                            id = 35, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 5,
                            name = context.getString(R.string.leasing),
                            icon = CategoryIcon.Vehicle,
                            colorWithName = CategoryColors.Red.toCategoryColorWithName()
                        ),
                        Category(
                            id = 36, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 4, parentCategoryId = 5,
                            name = context.getString(R.string.rent),
                            icon = CategoryIcon.Vehicle,
                            colorWithName = CategoryColors.Red.toCategoryColorWithName()
                        ),
                        Category(
                            id = 37, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 5, parentCategoryId = 5,
                            name = context.getString(R.string.maintenance),
                            icon = CategoryIcon.Vehicle,
                            colorWithName = CategoryColors.Red.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 6, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 6, parentCategoryId = null,
                        name = context.getString(R.string.digital_life),
                        icon = CategoryIcon.DigitalLife,
                        colorWithName = CategoryColors.LightBlue.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 38, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 6,
                            name = context.getString(R.string.internet_communication),
                            icon = CategoryIcon.DigitalLife,
                            colorWithName = CategoryColors.LightBlue.toCategoryColorWithName()
                        ),
                        Category(
                            id = 39, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 6,
                            name = context.getString(R.string.professional_subs),
                            icon = CategoryIcon.ProfessionalSubs,
                            colorWithName = CategoryColors.LightBlue.toCategoryColorWithName()
                        ),
                        Category(
                            id = 40, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 6,
                            name = context.getString(R.string.entertainment_subs),
                            icon = CategoryIcon.EntertainmentSubs,
                            colorWithName = CategoryColors.LightBlue.toCategoryColorWithName()
                        ),
                        Category(
                            id = 41, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 4, parentCategoryId = 6,
                            name = context.getString(R.string.games),
                            icon = CategoryIcon.Games,
                            colorWithName = CategoryColors.LightBlue.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 7, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 7, parentCategoryId = null,
                        name = context.getString(R.string.medicine),
                        icon = CategoryIcon.Medicine,
                        colorWithName = CategoryColors.Lavender.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 42, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 7,
                            name = context.getString(R.string.drugs_and_vitamins),
                            icon = CategoryIcon.Medicine,
                            colorWithName = CategoryColors.Lavender.toCategoryColorWithName()
                        ),
                        Category(
                            id = 43, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 7,
                            name = context.getString(R.string.insurance),
                            icon = CategoryIcon.Medicine,
                            colorWithName = CategoryColors.Lavender.toCategoryColorWithName()
                        ),
                        Category(
                            id = 44, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 7,
                            name = context.getString(R.string.doctor),
                            icon = CategoryIcon.Medicine,
                            colorWithName = CategoryColors.Lavender.toCategoryColorWithName()
                        ),
                        Category(
                            id = 45, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 4, parentCategoryId = 7,
                            name = context.getString(R.string.vaccination),
                            icon = CategoryIcon.Medicine,
                            colorWithName = CategoryColors.Lavender.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 8, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 8, parentCategoryId = null,
                        name = context.getString(R.string.education),
                        icon = CategoryIcon.Education,
                        colorWithName = CategoryColors.Blue.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 46, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 8,
                            name = context.getString(R.string.school_and_university),
                            icon = CategoryIcon.Education,
                            colorWithName = CategoryColors.Blue.toCategoryColorWithName()
                        ),
                        Category(
                            id = 47, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 8,
                            name = context.getString(R.string.courses),
                            icon = CategoryIcon.Education,
                            colorWithName = CategoryColors.Blue.toCategoryColorWithName()
                        ),
                        Category(
                            id = 48, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 8,
                            name = context.getString(R.string.studding_materials),
                            icon = CategoryIcon.Education,
                            colorWithName = CategoryColors.Blue.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 9, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 9, parentCategoryId = null,
                        name = context.getString(R.string.travels),
                        icon = CategoryIcon.Travels,
                        colorWithName = CategoryColors.Aquamarine.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 49, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 9,
                            name = context.getString(R.string.tickets),
                            icon = CategoryIcon.Travels,
                            colorWithName = CategoryColors.Aquamarine.toCategoryColorWithName()
                        ),
                        Category(
                            id = 50, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 9,
                            name = context.getString(R.string.accommodation),
                            icon = CategoryIcon.Travels,
                            colorWithName = CategoryColors.Aquamarine.toCategoryColorWithName()
                        ),
                        Category(
                            id = 51, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 9,
                            name = context.getString(R.string.food_and_drinks),
                            icon = CategoryIcon.Travels,
                            colorWithName = CategoryColors.Aquamarine.toCategoryColorWithName()
                        ),
                        Category(
                            id = 52, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 4, parentCategoryId = 9,
                            name = context.getString(R.string.transport),
                            icon = CategoryIcon.Travels,
                            colorWithName = CategoryColors.Aquamarine.toCategoryColorWithName()
                        ),
                        Category(
                            id = 53, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 5, parentCategoryId = 9,
                            name = context.getString(R.string.shopping),
                            icon = CategoryIcon.Travels,
                            colorWithName = CategoryColors.Aquamarine.toCategoryColorWithName()
                        ),
                        Category(
                            id = 54, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 6, parentCategoryId = 9,
                            name = context.getString(R.string.entertainment),
                            icon = CategoryIcon.Travels,
                            colorWithName = CategoryColors.Aquamarine.toCategoryColorWithName()
                        ),
                        Category(
                            id = 55, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 7, parentCategoryId = 9,
                            name = context.getString(R.string.other_category),
                            icon = CategoryIcon.Travels,
                            colorWithName = CategoryColors.Aquamarine.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 10, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 10, parentCategoryId = null,
                        name = context.getString(R.string.entertainment),
                        icon = CategoryIcon.Entertainment,
                        colorWithName = CategoryColors.Orange.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 56, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 10,
                            name = context.getString(R.string.hobbies),
                            icon = CategoryIcon.Entertainment,
                            colorWithName = CategoryColors.Orange.toCategoryColorWithName()
                        ),
                        Category(
                            id = 57, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 10,
                            name = context.getString(R.string.cinema_theater_concerts),
                            icon = CategoryIcon.Entertainment,
                            colorWithName = CategoryColors.Orange.toCategoryColorWithName()
                        ),
                        Category(
                            id = 58, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 10,
                            name = context.getString(R.string.sport),
                            icon = CategoryIcon.Entertainment,
                            colorWithName = CategoryColors.Orange.toCategoryColorWithName()
                        ),
                        Category(
                            id = 59, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 4, parentCategoryId = 10,
                            name = context.getString(R.string.events_hanging_out),
                            icon = CategoryIcon.Entertainment,
                            colorWithName = CategoryColors.Orange.toCategoryColorWithName()
                        ),
                        Category(
                            id = 60, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 5, parentCategoryId = 10,
                            name = context.getString(R.string.alcohol_and_smoking),
                            icon = CategoryIcon.Entertainment,
                            colorWithName = CategoryColors.Orange.toCategoryColorWithName()
                        ),
                        Category(
                            id = 61, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 6, parentCategoryId = 10,
                            name = context.getString(R.string.gifts_and_charity),
                            icon = CategoryIcon.Entertainment,
                            colorWithName = CategoryColors.Orange.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 11, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 11, parentCategoryId = null,
                        name = context.getString(R.string.investments),
                        icon = CategoryIcon.Investments,
                        colorWithName = CategoryColors.Yellow.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 62, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 11,
                            name = context.getString(R.string.shares),
                            icon = CategoryIcon.Investments,
                            colorWithName = CategoryColors.Yellow.toCategoryColorWithName()
                        ),
                        Category(
                            id = 63, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 11,
                            name = context.getString(R.string.cryptocurrencies),
                            icon = CategoryIcon.Investments,
                            colorWithName = CategoryColors.Yellow.toCategoryColorWithName()
                        ),
                        Category(
                            id = 64, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 11,
                            name = context.getString(R.string.realty),
                            icon = CategoryIcon.Investments,
                            colorWithName = CategoryColors.Yellow.toCategoryColorWithName()
                        ),
                        Category(
                            id = 65, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 4, parentCategoryId = 11,
                            name = context.getString(R.string.savings),
                            icon = CategoryIcon.Investments,
                            colorWithName = CategoryColors.Yellow.toCategoryColorWithName()
                        )
                    )
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 12, type = CategoryType.Expense, rank = CategoryRank.Parent,
                        orderNum = 12, parentCategoryId = null,
                        name = context.getString(R.string.other_category),
                        icon = CategoryIcon.Other,
                        colorWithName = CategoryColors.GrayDefault.toCategoryColorWithName()
                    ),
                    subcategoryList = listOf(
                        Category(
                            id = 66, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 1, parentCategoryId = 12,
                            name = context.getString(R.string.transfers),
                            icon = CategoryIcon.Transfers,
                            colorWithName = CategoryColors.GrayDefault.toCategoryColorWithName()
                        ),
                        Category(
                            id = 67, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 2, parentCategoryId = 12,
                            name = context.getString(R.string.missing),
                            icon = CategoryIcon.Missing,
                            colorWithName = CategoryColors.GrayDefault.toCategoryColorWithName()
                        ),
                        Category(
                            id = 68, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 3, parentCategoryId = 12,
                            name = context.getString(R.string.public_toilets),
                            icon = CategoryIcon.Other,
                            colorWithName = CategoryColors.GrayDefault.toCategoryColorWithName()
                        ),
                        Category(
                            id = 69, type = CategoryType.Expense, rank = CategoryRank.Sub,
                            orderNum = 4, parentCategoryId = 12,
                            name = context.getString(R.string.other_category),
                            icon = CategoryIcon.Other,
                            colorWithName = CategoryColors.GrayDefault.toCategoryColorWithName()
                        )
                    )
                )
            ),
            income = listOf(
                CategoryWithSubcategories(
                    category = Category(
                        id = 70, type = CategoryType.Income, rank = CategoryRank.Parent,
                        orderNum = 1, parentCategoryId = null,
                        name = context.getString(R.string.salary),
                        icon = CategoryIcon.Salary,
                        colorWithName = CategoryColors.Green.toCategoryColorWithName()
                    ),
                    subcategoryList = emptyList()
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 71, type = CategoryType.Income, rank = CategoryRank.Parent,
                        orderNum = 2, parentCategoryId = null,
                        name = context.getString(R.string.scholarship),
                        icon = CategoryIcon.Scholarship,
                        colorWithName = CategoryColors.Blue.toCategoryColorWithName()
                    ),
                    subcategoryList = emptyList()
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 72, type = CategoryType.Income, rank = CategoryRank.Parent,
                        orderNum = 3, parentCategoryId = null,
                        name = context.getString(R.string.sale),
                        icon = CategoryIcon.Sales,
                        colorWithName = CategoryColors.Orange.toCategoryColorWithName()
                    ),
                    subcategoryList = emptyList()
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 73, type = CategoryType.Income, rank = CategoryRank.Parent,
                        orderNum = 4, parentCategoryId = null,
                        name = context.getString(R.string.rent),
                        icon = CategoryIcon.Housing,
                        colorWithName = CategoryColors.Camel.toCategoryColorWithName()
                    ),
                    subcategoryList = emptyList()
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 74, type = CategoryType.Income, rank = CategoryRank.Parent,
                        orderNum = 5, parentCategoryId = null,
                        name = context.getString(R.string.refunds),
                        icon = CategoryIcon.Refunds,
                        colorWithName = CategoryColors.LightBlue.toCategoryColorWithName()
                    ),
                    subcategoryList = emptyList()
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 75, type = CategoryType.Income, rank = CategoryRank.Parent,
                        orderNum = 6, parentCategoryId = null,
                        name = context.getString(R.string.investments),
                        icon = CategoryIcon.Investments,
                        colorWithName = CategoryColors.Yellow.toCategoryColorWithName()
                    ),
                    subcategoryList = emptyList()
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 76, type = CategoryType.Income, rank = CategoryRank.Parent,
                        orderNum = 7, parentCategoryId = null,
                        name = context.getString(R.string.gifts),
                        icon = CategoryIcon.Gifts,
                        colorWithName = CategoryColors.Lavender.toCategoryColorWithName()
                    ),
                    subcategoryList = emptyList()
                ),
                CategoryWithSubcategories(
                    category = Category(
                        id = 77, type = CategoryType.Income, rank = CategoryRank.Parent,
                        orderNum = 8, parentCategoryId = null,
                        name = context.getString(R.string.transfers),
                        icon = CategoryIcon.Transfers,
                        colorWithName = CategoryColors.GrayDefault.toCategoryColorWithName()
                    ),
                    subcategoryList = emptyList()
                )
            )
        )
    }

    fun translateDefaultCategoriesIn(
        categoriesWithSubcategories: CategoriesWithSubcategories
    ): List<Category> {
        val translatedDefaultCategories = getDefaultCategories().concatenateAsCategoryList()
        val currentCategoriesToTranslate = categoriesWithSubcategories.concatenateAsCategoryList()
            .filter { translatedDefaultCategories.findById(it.id) != null }

        return currentCategoriesToTranslate.map { currentCategory ->
            translatedDefaultCategories.findById(currentCategory.id)?.let{
                currentCategory.copy(name = it.name)
            } ?: currentCategory
        }
    }

}