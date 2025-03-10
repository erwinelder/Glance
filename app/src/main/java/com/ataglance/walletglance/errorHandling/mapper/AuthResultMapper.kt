package com.ataglance.walletglance.errorHandling.mapper

import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.presentation.model.ResultUiState


fun Result<AuthSuccess, AuthError>.toUiState(): ResultUiState {
    return when (this) {
        is Result.Success -> success.toUiState()
        is Result.Error -> error.toUiState()
    }
}


fun AuthSuccess.toUiState(): ResultUiState {
    return ResultUiState(
        isSuccessful = true,
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageRes()
    )
}

fun AuthError.toUiState(): ResultUiState {
    return ResultUiState(
        isSuccessful = false,
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageRes()
    )
}


@StringRes private fun AuthSuccess.asTitleRes(): Int {
    return when (this) {
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
        AuthSuccess.SignUpEmailVerificationSent -> R.string.sign_up_email_verification_sent
        AuthSuccess.UpdateEmailEmailVerificationSent -> R.string.update_email_email_verification_sent
        AuthSuccess.EmailUpdated -> R.string.email_update_success
        AuthSuccess.PasswordUpdated -> R.string.password_update_success
        AuthSuccess.ResetPasswordEmailSent -> R.string.reset_password_email_sent
        AuthSuccess.AccountDeleted -> R.string.your_account_has_been_deleted_successfully
    }
}

@StringRes private fun AuthError.asTitleRes(): Int {
    return when (this) {
        AuthError.UserAlreadyExists, AuthError.UserNotCreated, AuthError.UserDataNotSaved,
        AuthError.UserNotFound, AuthError.UserNotSignedIn, AuthError.InvalidEmail,
        AuthError.InvalidCode, AuthError.WrongCredentials, AuthError.SignInError,
        AuthError.ReauthenticationError, AuthError.SignUpEmailVerificationError,
        AuthError.EmailForPasswordResetError, AuthError.EmailVerificationError,
        AuthError.PasswordResetError, AuthError.UpdatePasswordError,
        AuthError.DataDeletionError, AuthError.AccountDeletionError ->
            R.string.oops
    }
}

@StringRes private fun AuthError.asMessageRes(): Int {
    return when (this) {
        AuthError.UserAlreadyExists -> R.string.user_already_exists_error
        AuthError.UserNotCreated -> R.string.user_not_created_error
        AuthError.UserDataNotSaved -> R.string.user_data_not_saved_error
        AuthError.UserNotFound -> R.string.user_not_found_error
        AuthError.UserNotSignedIn -> R.string.user_not_signed_in_error
        AuthError.InvalidEmail -> R.string.invalid_email_error
        AuthError.InvalidCode -> R.string.invalid_code_error
        AuthError.WrongCredentials -> R.string.wrong_credentials_error
        AuthError.SignInError -> R.string.sign_in_error
        AuthError.ReauthenticationError -> R.string.reauthentication_error
        AuthError.SignUpEmailVerificationError -> R.string.sign_up_email_verification_error
        AuthError.EmailVerificationError -> R.string.email_verification_error
        AuthError.EmailForPasswordResetError -> R.string.email_for_password_reset_error
        AuthError.PasswordResetError -> R.string.password_reset_error
        AuthError.UpdatePasswordError -> R.string.update_password_error
        AuthError.DataDeletionError -> R.string.deleting_user_data_error
        AuthError.AccountDeletionError -> R.string.deleting_user_account_error
    }
}