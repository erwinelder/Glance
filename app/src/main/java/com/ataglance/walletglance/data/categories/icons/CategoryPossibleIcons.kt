package com.ataglance.walletglance.data.categories.icons

data class CategoryPossibleIcons(
    val foodAndDrinks: CategoryIcon = CategoryIcon.FoodAndDrinks,
    val groceries: CategoryIcon = CategoryIcon.Groceries,
    val restaurant: CategoryIcon = CategoryIcon.Restaurant,
    val housing: CategoryIcon = CategoryIcon.Housing,
    val housingPurchase: CategoryIcon = CategoryIcon.HousingPurchase,
    val shopping: CategoryIcon = CategoryIcon.Shopping,
    val clothes: CategoryIcon = CategoryIcon.Clothes,
    val transport: CategoryIcon = CategoryIcon.Transport,
    val vehicle: CategoryIcon = CategoryIcon.Vehicle,
    val digitalLife: CategoryIcon = CategoryIcon.DigitalLife,
    val professionalSubs: CategoryIcon = CategoryIcon.ProfessionalSubs,
    val entertainmentSubs: CategoryIcon = CategoryIcon.EntertainmentSubs,
    val games: CategoryIcon = CategoryIcon.Games,
    val medicine: CategoryIcon = CategoryIcon.Medicine,
    val education: CategoryIcon = CategoryIcon.Education,
    val travels: CategoryIcon = CategoryIcon.Travels,
    val accommodation: CategoryIcon = CategoryIcon.Accommodation,
    val travelTickets: CategoryIcon = CategoryIcon.TravelTickets,
    val entertainment: CategoryIcon = CategoryIcon.Entertainment,
    val investments: CategoryIcon = CategoryIcon.Investments,
    val other: CategoryIcon = CategoryIcon.Other,
    val transfers: CategoryIcon = CategoryIcon.Transfers,
    val missing: CategoryIcon = CategoryIcon.Missing,
    val salary: CategoryIcon = CategoryIcon.Salary,
    val scholarship: CategoryIcon = CategoryIcon.Scholarship,
    val sales: CategoryIcon = CategoryIcon.Sales,
    val refunds: CategoryIcon = CategoryIcon.Refunds,
    val gifts: CategoryIcon = CategoryIcon.Gifts
) {

    fun asList(): List<CategoryIcon> {
        return listOf(
            foodAndDrinks,
            groceries,
            restaurant,
            housing,
            housingPurchase,
            shopping,
            clothes,
            transport,
            vehicle,
            digitalLife,
            professionalSubs,
            entertainmentSubs,
            games,
            medicine,
            education,
            travels,
            accommodation,
            travelTickets,
            entertainment,
            investments,
            other,
            transfers,
            missing,
            salary,
            scholarship,
            sales,
            refunds,
            gifts
        )
    }

    fun getIconByName(name: String): CategoryIcon {
        return when (name) {
            CategoryIcon.FoodAndDrinks.name -> foodAndDrinks
            CategoryIcon.Groceries.name -> groceries
            CategoryIcon.Restaurant.name -> restaurant
            CategoryIcon.Housing.name -> housing
            CategoryIcon.HousingPurchase.name -> housingPurchase
            CategoryIcon.Shopping.name -> shopping
            CategoryIcon.Clothes.name -> clothes
            CategoryIcon.Transport.name -> transport
            CategoryIcon.Vehicle.name -> vehicle
            CategoryIcon.DigitalLife.name -> digitalLife
            CategoryIcon.ProfessionalSubs.name -> professionalSubs
            CategoryIcon.EntertainmentSubs.name -> entertainmentSubs
            CategoryIcon.Games.name -> games
            CategoryIcon.Medicine.name -> medicine
            CategoryIcon.Education.name -> education
            CategoryIcon.Travels.name -> travels
            CategoryIcon.Accommodation.name -> accommodation
            CategoryIcon.TravelTickets.name -> travelTickets
            CategoryIcon.Entertainment.name -> entertainment
            CategoryIcon.Investments.name -> investments
            CategoryIcon.Other.name -> other
            CategoryIcon.Transfers.name -> transfers
            CategoryIcon.Missing.name -> missing
            CategoryIcon.Salary.name -> salary
            CategoryIcon.Scholarship.name -> scholarship
            CategoryIcon.Sales.name -> sales
            CategoryIcon.Refunds.name -> refunds
            CategoryIcon.Gifts.name -> gifts
            else -> other
        }
    }

}
