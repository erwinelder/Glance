package com.ataglance.walletglance.errorHandling.mapper

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultWithButtonState


fun Result<AuthSuccess, AuthError>.toResultState(): ResultState {
    return when (this) {
        is Result.Success -> success.toResultState()
        is Result.Error -> error.toResultState()
    }
}

fun Result<AuthSuccess, AuthError>.toResultWithButtonState(): ResultWithButtonState {
    return when (this) {
        is Result.Success -> success.toResultWithButtonState()
        is Result.Error -> error.toResultWithButtonState()
    }
}


fun AuthSuccess.toResultState(): ResultState {
    return ResultState(
        isSuccessful = true,
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageRes()
    )
}

fun AuthSuccess.toResultWithButtonState(): ResultWithButtonState {
    return ResultWithButtonState(
        isSuccessful = true,
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageResOrNull(),
        buttonTextRes = this.asButtonTextRes(),
        buttonIconRes = this.asButtonIconResOrNull()
    )
}


fun AuthError.toResultState(): ResultState {
    return ResultState(
        isSuccessful = false,
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageRes()
    )
}

fun AuthError.toResultWithButtonState(): ResultWithButtonState {
    return ResultWithButtonState(
        isSuccessful = false,
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageResOrNull(),
        buttonTextRes = this.asButtonTextRes(),
        buttonIconRes = this.asButtonIconResOrNull()
    )
}


@StringRes private fun AuthSuccess.asTitleRes(): Int {
    return when (this) {
        AuthSuccess.SignedIn -> R.string.welcome_back_to_glance
        AuthSuccess.SignedUp -> R.string.welcome_to_glance
        AuthSuccess.EmailUpdated, AuthSuccess.PasswordUpdated ->
            R.string.all_set
        AuthSuccess.SignUpEmailVerificationSent, AuthSuccess.UpdateEmailEmailVerificationSent,
        AuthSuccess.ResetPasswordEmailSent ->
            R.string.email_sent
        AuthSuccess.AccountDeleted -> R.string.account_deleted
    }
}

@StringRes private fun AuthSuccess.asMessageRes(): Int {
    return when (this) {
        AuthSuccess.SignedIn -> R.string.welcome_back_to_glance
        AuthSuccess.SignedUp -> R.string.welcome_to_glance
        AuthSuccess.SignUpEmailVerificationSent -> R.string.sign_up_email_verification_sent_description
        AuthSuccess.UpdateEmailEmailVerificationSent -> R.string.update_email_email_verification_sent
        AuthSuccess.EmailUpdated -> R.string.email_update_success
        AuthSuccess.PasswordUpdated -> R.string.password_update_success
        AuthSuccess.ResetPasswordEmailSent -> R.string.reset_password_email_sent
        AuthSuccess.AccountDeleted -> R.string.your_account_has_been_deleted_successfully
    }
}

@StringRes private fun AuthSuccess.asMessageResOrNull(): Int? {
    return when (this) {
        AuthSuccess.SignedIn -> null
        AuthSuccess.SignedUp -> null
        AuthSuccess.SignUpEmailVerificationSent -> R.string.sign_up_email_verification_sent_description
        AuthSuccess.UpdateEmailEmailVerificationSent -> R.string.update_email_email_verification_sent
        AuthSuccess.EmailUpdated -> R.string.email_update_success
        AuthSuccess.PasswordUpdated -> R.string.password_update_success
        AuthSuccess.ResetPasswordEmailSent -> R.string.reset_password_email_sent
        AuthSuccess.AccountDeleted -> R.string.your_account_has_been_deleted_successfully
    }
}

@StringRes private fun AuthSuccess.asButtonTextRes(): Int {
    return when (this) {
        AuthSuccess.SignedIn -> R.string.continue_to_app
        AuthSuccess.SignedUp -> R.string.continue_to_app
        AuthSuccess.SignUpEmailVerificationSent -> R.string.check
        AuthSuccess.UpdateEmailEmailVerificationSent -> R.string.update_email_email_verification_sent
        AuthSuccess.EmailUpdated -> R.string.email_update_success
        AuthSuccess.PasswordUpdated -> R.string.password_update_success
        AuthSuccess.ResetPasswordEmailSent -> R.string.reset_password_email_sent
        AuthSuccess.AccountDeleted -> R.string.your_account_has_been_deleted_successfully
    }
}

@DrawableRes private fun AuthSuccess.asButtonIconResOrNull(): Int? {
    return when (this) {
        AuthSuccess.SignedIn -> null
        AuthSuccess.SignedUp -> null
        AuthSuccess.SignUpEmailVerificationSent -> null
        AuthSuccess.UpdateEmailEmailVerificationSent -> null
        AuthSuccess.EmailUpdated -> null
        AuthSuccess.PasswordUpdated -> null
        AuthSuccess.ResetPasswordEmailSent -> null
        AuthSuccess.AccountDeleted -> null
    }
}


@StringRes private fun AuthError.asTitleRes(): Int {
    return when (this) {
        AuthError.DataNotValid,
        AuthError.InvalidCredentials,
        AuthError.EmailNotVerified,
        AuthError.UserNotFound,
        AuthError.SignInError,
        AuthError.UserAlreadyExists,
        AuthError.SignUpEmailVerificationError,
        AuthError.SignUpError,
        AuthError.CheckEmailVerificationError,
        AuthError.OobCodeIsInvalid,
        AuthError.FinishSignUpError,
        AuthError.SessionExpired,

        AuthError.UserNotCreated,
        AuthError.UserDataNotSaved,
        AuthError.UserNotSignedIn,
        AuthError.InvalidEmail,
        AuthError.InvalidCode,
        AuthError.WrongCredentials,
        AuthError.ReauthenticationError,
        AuthError.EmailForPasswordResetError,
        AuthError.EmailVerificationError,
        AuthError.PasswordResetError,
        AuthError.UpdatePasswordError,
        AuthError.DataDeletionError,
        AuthError.AccountDeletionError ->
            R.string.oops
    }
}

@StringRes private fun AuthError.asMessageRes(): Int {
    return when (this) {
        AuthError.DataNotValid,
        AuthError.InvalidCredentials,
        AuthError.EmailNotVerified,
        AuthError.UserNotFound -> R.string.user_not_found_error
        AuthError.SignInError -> R.string.sign_in_error
        AuthError.UserAlreadyExists -> R.string.user_already_exists_error
        AuthError.SignUpEmailVerificationError -> R.string.sign_up_email_verification_error
        AuthError.SignUpError,
        AuthError.CheckEmailVerificationError,
        AuthError.OobCodeIsInvalid,
        AuthError.FinishSignUpError,
        AuthError.SessionExpired,

        AuthError.UserNotCreated -> R.string.user_not_created_error
        AuthError.UserDataNotSaved -> R.string.user_data_not_saved_error
        AuthError.UserNotSignedIn -> R.string.user_not_signed_in_error
        AuthError.InvalidEmail -> R.string.invalid_email_error
        AuthError.InvalidCode -> R.string.invalid_code_error
        AuthError.WrongCredentials -> R.string.wrong_credentials_error
        AuthError.ReauthenticationError -> R.string.reauthentication_error
        AuthError.EmailVerificationError -> R.string.email_verification_error
        AuthError.EmailForPasswordResetError -> R.string.email_for_password_reset_error
        AuthError.PasswordResetError -> R.string.password_reset_error
        AuthError.UpdatePasswordError -> R.string.update_password_error
        AuthError.DataDeletionError -> R.string.deleting_user_data_error
        AuthError.AccountDeletionError -> R.string.deleting_user_account_error
    }
}

@StringRes private fun AuthError.asMessageResOrNull(): Int {
    return when (this) {
        AuthError.DataNotValid,
        AuthError.InvalidCredentials,
        AuthError.EmailNotVerified,
        AuthError.UserNotFound -> R.string.user_not_found_error
        AuthError.SignInError -> R.string.sign_in_error
        AuthError.UserAlreadyExists -> R.string.user_already_exists_error
        AuthError.SignUpEmailVerificationError -> R.string.sign_up_email_verification_error
        AuthError.SignUpError,
        AuthError.CheckEmailVerificationError,
        AuthError.OobCodeIsInvalid,
        AuthError.FinishSignUpError,
        AuthError.SessionExpired,

        AuthError.UserNotCreated -> R.string.user_not_created_error
        AuthError.UserDataNotSaved -> R.string.user_data_not_saved_error
        AuthError.UserNotSignedIn -> R.string.user_not_signed_in_error
        AuthError.InvalidEmail -> R.string.invalid_email_error
        AuthError.InvalidCode -> R.string.invalid_code_error
        AuthError.WrongCredentials -> R.string.wrong_credentials_error
        AuthError.ReauthenticationError -> R.string.reauthentication_error
        AuthError.EmailVerificationError -> R.string.email_verification_error
        AuthError.EmailForPasswordResetError -> R.string.email_for_password_reset_error
        AuthError.PasswordResetError -> R.string.password_reset_error
        AuthError.UpdatePasswordError -> R.string.update_password_error
        AuthError.DataDeletionError -> R.string.deleting_user_data_error
        AuthError.AccountDeletionError -> R.string.deleting_user_account_error
    }
}

@StringRes private fun AuthError.asButtonTextRes(): Int {
    return when (this) {
        AuthError.DataNotValid,
        AuthError.InvalidCredentials,
        AuthError.EmailNotVerified,
        AuthError.UserNotFound -> R.string.user_not_found_error
        AuthError.SignInError -> R.string.sign_in_error
        AuthError.UserAlreadyExists -> R.string.user_already_exists_error
        AuthError.SignUpEmailVerificationError -> R.string.sign_up_email_verification_error
        AuthError.SignUpError,
        AuthError.CheckEmailVerificationError,
        AuthError.OobCodeIsInvalid,
        AuthError.FinishSignUpError,
        AuthError.SessionExpired,

        AuthError.UserNotCreated -> R.string.user_not_created_error
        AuthError.UserDataNotSaved -> R.string.user_data_not_saved_error
        AuthError.UserNotSignedIn -> R.string.user_not_signed_in_error
        AuthError.InvalidEmail -> R.string.invalid_email_error
        AuthError.InvalidCode -> R.string.invalid_code_error
        AuthError.WrongCredentials -> R.string.wrong_credentials_error
        AuthError.ReauthenticationError -> R.string.reauthentication_error
        AuthError.EmailVerificationError -> R.string.email_verification_error
        AuthError.EmailForPasswordResetError -> R.string.email_for_password_reset_error
        AuthError.PasswordResetError -> R.string.password_reset_error
        AuthError.UpdatePasswordError -> R.string.update_password_error
        AuthError.DataDeletionError -> R.string.deleting_user_data_error
        AuthError.AccountDeletionError -> R.string.deleting_user_account_error
    }
}

@DrawableRes private fun AuthError.asButtonIconResOrNull(): Int? {
    return when (this) {
        AuthError.DataNotValid -> null
        AuthError.InvalidCredentials -> null
        AuthError.EmailNotVerified -> null
        AuthError.UserNotFound -> null
        AuthError.SignInError -> null
        AuthError.UserAlreadyExists -> null
        AuthError.SignUpEmailVerificationError -> null
        AuthError.SignUpError -> null
        AuthError.CheckEmailVerificationError -> null
        AuthError.OobCodeIsInvalid -> null
        AuthError.FinishSignUpError -> null
        AuthError.SessionExpired -> null

        AuthError.UserNotCreated -> null
        AuthError.UserDataNotSaved -> null
        AuthError.UserNotSignedIn -> null
        AuthError.InvalidEmail -> null
        AuthError.InvalidCode -> null
        AuthError.WrongCredentials -> null
        AuthError.ReauthenticationError -> null
        AuthError.EmailVerificationError -> null
        AuthError.EmailForPasswordResetError -> null
        AuthError.PasswordResetError -> null
        AuthError.UpdatePasswordError -> null
        AuthError.DataDeletionError -> null
        AuthError.AccountDeletionError -> null
    }
}