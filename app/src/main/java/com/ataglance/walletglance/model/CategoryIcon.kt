package com.ataglance.walletglance.model

import androidx.annotation.DrawableRes
import com.ataglance.walletglance.R

sealed class CategoryIcon(val name: String, @DrawableRes val res: Int) {
    data object FoodAndDrinks : CategoryIcon("food_and_drinks", R.drawable.food_and_drinks_icon)
    data object Groceries : CategoryIcon("groceries", R.drawable.groceries_icon)
    data object Restaurant : CategoryIcon("restaurant", R.drawable.restaurant_icon)
    data object Housing : CategoryIcon("housing", R.drawable.housing_icon)
    data object HousingPurchase : CategoryIcon("housing_purchase", R.drawable.housing_purchase_icon)
    data object Shopping : CategoryIcon("shopping", R.drawable.shopping_icon)
    data object Transport : CategoryIcon("transport", R.drawable.transport_icon)
    data object Vehicle : CategoryIcon("vehicle", R.drawable.vehicle_icon)
    data object DigitalLife : CategoryIcon("digital_life", R.drawable.digital_life_icon)
    data object Medicine : CategoryIcon("medicine", R.drawable.medicine_icon)
    data object Education : CategoryIcon("education", R.drawable.education_icon)
    data object Travels : CategoryIcon("travels", R.drawable.travels_icon)
    data object Entertainment : CategoryIcon("entertainment", R.drawable.entertainment_icon)
    data object Investments : CategoryIcon("investments", R.drawable.investments_icon)
    data object Other : CategoryIcon("other", R.drawable.other_icon)
    data object Transfers : CategoryIcon("transfers", R.drawable.transfers_icon)
    data object Missing : CategoryIcon("missing", R.drawable.missing_icon)
    data object Salary : CategoryIcon("salary", R.drawable.salary_icon)
    data object Scholarship : CategoryIcon("scholarship", R.drawable.scholarship_icon)
    data object Sales : CategoryIcon("sales", R.drawable.sales_icon)
    data object Refunds : CategoryIcon("refunds", R.drawable.refunds_icon)
    data object Gifts : CategoryIcon("gifts", R.drawable.gifts_icon)
}