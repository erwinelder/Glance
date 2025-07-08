package com.ataglance.walletglance.auth.mapper

import com.ataglance.walletglance.auth.data.model.UserDto
import com.ataglance.walletglance.auth.data.model.UserRoleDto
import com.ataglance.walletglance.auth.data.model.UserWithTokenDto
import com.ataglance.walletglance.auth.domain.model.user.User
import com.ataglance.walletglance.auth.domain.model.user.UserRole
import com.ataglance.walletglance.auth.domain.model.user.UserWithToken
import com.ataglance.walletglance.billing.mapper.toDomainModel
import com.ataglance.walletglance.core.domain.app.AppLanguage


fun UserRoleDto.toDomainModel(): UserRole {
    return when (this) {
        UserRoleDto.User -> UserRole.User
        UserRoleDto.Admin -> UserRole.Admin
    }
}


fun UserDto.toDomainModel(): User? {
    return User(
        id = id,
        email = email,
        role = role.toDomainModel(),
        name = name,
        language = AppLanguage.fromLangCode(langCode = langCode) ?: return null,
        subscription = subscription.toDomainModel()
    )
}


fun UserWithTokenDto.toDomainModel(): UserWithToken? {
    return UserWithToken(
        id = id,
        email = email,
        role = role.toDomainModel(),
        name = name,
        langCode = AppLanguage.fromLangCode(langCode = langCode) ?: return null,
        subscription = subscription.toDomainModel(),
        token = token
    )
}