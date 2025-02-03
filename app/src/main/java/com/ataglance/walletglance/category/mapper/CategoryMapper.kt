package com.ataglance.walletglance.category.mapper

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.category.domain.model.color.CategoryColorWithName
import com.ataglance.walletglance.category.domain.model.icons.CategoryIcon
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.utils.asChar


fun CategoryEntity.toDomainModel(icon: CategoryIcon, color: CategoryColorWithName): Category? {
    val categoryType = this.getCategoryType() ?: return null

    return Category(
        id = id,
        type = categoryType,
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        icon = icon,
        colorWithName = color
    )
}

fun List<CategoryEntity>.toDomainModels(
    iconProvider: (String) -> CategoryIcon,
    colorProvider: (String) -> CategoryColorWithName
): List<Category> {
    return this.mapNotNull {
        it.toDomainModel(
            icon = iconProvider(it.iconName),
            color = colorProvider(it.colorName)
        )
    }
}



fun Category.toDataModel(): CategoryEntity {
    return CategoryEntity(
        id = id,
        type = type.asChar(),
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        iconName = icon.name,
        colorName = colorWithName.getNameValue()
    )
}