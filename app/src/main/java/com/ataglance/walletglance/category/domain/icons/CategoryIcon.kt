package com.ataglance.walletglance.category.domain.icons

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import com.ataglance.walletglance.R

@Stable
sealed class CategoryIcon(val name: String, @DrawableRes val res: Int) {
    data object FoodAndDrinks : CategoryIcon("food_and_drinks", R.drawable.food_and_drinks_icon)
    data object Groceries : CategoryIcon("groceries", R.drawable.groceries_icon)
    data object Restaurant : CategoryIcon("restaurant", R.drawable.restaurant_icon)
    data object Housing : CategoryIcon("housing", R.drawable.housing_icon)
    data object HousingPurchase : CategoryIcon("housing_purchase", R.drawable.housing_purchase_icon)
    data object Shopping : CategoryIcon("shopping", R.drawable.shopping_icon)
    data object Clothes : CategoryIcon("clothes", R.drawable.clothes_icon)
    data object Transport : CategoryIcon("transport", R.drawable.transport_icon)
    data object Vehicle : CategoryIcon("vehicle", R.drawable.vehicle_icon)
    data object DigitalLife : CategoryIcon("digital_life", R.drawable.digital_life_icon)
    data object ProfessionalSubs : CategoryIcon("professional_subs", R.drawable.professional_subs_icon)
    data object EntertainmentSubs : CategoryIcon("entertainment_subs", R.drawable.entertainment_subs_icon)
    data object Games : CategoryIcon("games", R.drawable.games_icon)
    data object Medicine : CategoryIcon("medicine", R.drawable.medicine_icon)
    data object Education : CategoryIcon("education", R.drawable.education_icon)
    data object Travels : CategoryIcon("travels", R.drawable.travels_icon)
    data object Accommodation : CategoryIcon("accommodation", R.drawable.accommodation_icon)
    data object TravelTickets : CategoryIcon("travel_tickets", R.drawable.travel_tickets_icon)
    data object Entertainment : CategoryIcon("entertainment", R.drawable.entertainment_icon)
    data object CinemaTheater : CategoryIcon("cinema_theater", R.drawable.cinema_theater_icon)
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