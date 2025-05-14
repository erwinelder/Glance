package com.ataglance.walletglance.auth.mapper

import com.ataglance.walletglance.auth.data.model.UserRoleDto
import com.ataglance.walletglance.auth.data.model.UserWithTokenDto
import com.ataglance.walletglance.auth.domain.model.UserRole
import com.ataglance.walletglance.auth.domain.model.UserWithToken
import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.core.domain.app.AppLanguage
import com.ataglance.walletglance.core.utils.enumValueOrNull


fun UserRoleDto.toDomainModel(): UserRole {
    return when (this) {
        UserRoleDto.User -> UserRole.User
        UserRoleDto.Admin -> UserRole.Admin
    }
}


fun UserWithTokenDto.toDomainModel(): UserWithToken? {
    return UserWithToken(
        id = id,
        email = email,
        role = role.toDomainModel(),
        name = name,
        langCode = AppLanguage.fromLangCode(langCode = langCode) ?: return null,
        subscription = enumValueOrNull<AppSubscription>(name = subscription) ?: return null,
        token = token
    )
}