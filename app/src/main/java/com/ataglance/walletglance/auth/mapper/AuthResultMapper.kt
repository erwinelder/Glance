package com.ataglance.walletglance.auth.mapper

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.request.presentation.model.ResultState


fun AuthSuccess.toResultStateButton(): ResultState.ButtonState {
    return ResultState.ButtonState(
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageResOrNull(),
        buttonTextRes = this.asButtonTextRes(),
        buttonIconRes = this.asButtonIconResOrNull()
    )
}


fun AuthError.toResultStateButton(): ResultState.ButtonState {
    return ResultState.ButtonState(
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageResOrNull(),
        buttonTextRes = this.asButtonTextRes(),
        buttonIconRes = this.asButtonIconResOrNull()
    )
}


@StringRes private fun AuthSuccess.asTitleRes(): Int {
    return when (this) {
        AuthSuccess.SignedIn -> R.string.welcome_back_to_glance
        AuthSuccess.SignUpEmailVerificationSent -> R.string.email_sent
        AuthSuccess.SignUpVerificationCodeReceived -> R.string.verify_email
        AuthSuccess.SignedUp -> R.string.welcome_to_glance
        AuthSuccess.EmailUpdateEmailVerificationSent -> R.string.email_sent
        AuthSuccess.EmailUpdateVerificationCodeReceived -> R.string.verify_email
        AuthSuccess.EmailUpdated -> R.string.all_set
        AuthSuccess.ResetPasswordEmailSent -> R.string.email_sent
        AuthSuccess.PasswordUpdated -> R.string.all_set
        AuthSuccess.AccountDeleted -> R.string.account_deleted
    }
}

@StringRes private fun AuthSuccess.asMessageResOrNull(): Int? {
    return when (this) {
        AuthSuccess.SignedIn -> null
        AuthSuccess.SignUpEmailVerificationSent -> R.string.sign_up_email_verification_sent_message
        AuthSuccess.SignUpVerificationCodeReceived -> R.string.sign_up_verify_email_description
        AuthSuccess.SignedUp -> null
        AuthSuccess.EmailUpdateEmailVerificationSent -> R.string.update_email_email_verification_sent_message
        AuthSuccess.EmailUpdateVerificationCodeReceived -> R.string.email_update_verify_email_description
        AuthSuccess.EmailUpdated -> R.string.email_updated_successfully_message
        AuthSuccess.ResetPasswordEmailSent -> R.string.reset_password_email_sent_message
        AuthSuccess.PasswordUpdated -> R.string.password_updated_successfully_message
        AuthSuccess.AccountDeleted -> R.string.your_account_deleted_successfully_message
    }
}

@StringRes private fun AuthSuccess.asButtonTextRes(): Int {
    return when (this) {
        AuthSuccess.SignedIn -> R.string.continue_to_app
        AuthSuccess.SignUpEmailVerificationSent -> R.string.check
        AuthSuccess.SignUpVerificationCodeReceived -> R.string.verify
        AuthSuccess.SignedUp -> R.string.continue_setup
        AuthSuccess.EmailUpdateEmailVerificationSent -> R.string.check
        AuthSuccess.EmailUpdateVerificationCodeReceived -> R.string.verify
        AuthSuccess.EmailUpdated -> R.string._continue
        AuthSuccess.ResetPasswordEmailSent -> R.string.back
        AuthSuccess.PasswordUpdated -> R.string._continue
        AuthSuccess.AccountDeleted -> R.string._continue
    }
}

@DrawableRes private fun AuthSuccess.asButtonIconResOrNull(): Int? {
    return when (this) {
        AuthSuccess.SignedIn -> null
        AuthSuccess.SignUpEmailVerificationSent -> null
        AuthSuccess.SignUpVerificationCodeReceived -> null
        AuthSuccess.SignedUp -> null
        AuthSuccess.EmailUpdateEmailVerificationSent -> null
        AuthSuccess.EmailUpdateVerificationCodeReceived -> null
        AuthSuccess.EmailUpdated -> null
        AuthSuccess.ResetPasswordEmailSent -> R.drawable.short_arrow_left_icon
        AuthSuccess.PasswordUpdated -> null
        AuthSuccess.AccountDeleted -> null
    }
}


@StringRes private fun AuthError.asTitleRes(): Int {
    return when (this) {
        AuthError.UserNotSignedIn,
        AuthError.RequestDataNotValid,
        AuthError.SessionExpired -> R.string.oops
        AuthError.AppUpdateRequired -> R.string.update_required
        AuthError.InvalidCredentials,
        AuthError.ReauthenticationError,
        AuthError.EmailNotVerified,
        AuthError.UserNotFound,
        AuthError.SignInError,
        AuthError.UserAlreadyExists,
        AuthError.SignUpEmailVerificationError,
        AuthError.SignUpError,
        AuthError.CheckEmailVerificationError,
        AuthError.OobCodeIsInvalid,
        AuthError.FinishSignUpError,
        AuthError.RequestEmailUpdateError,
        AuthError.EmailUpdateError,
        AuthError.PasswordUpdateError,
        AuthError.ResetPasswordRequestError,
        AuthError.PasswordResetError,
        AuthError.AccountNotDeleted -> R.string.oops
    }
}

@StringRes private fun AuthError.asMessageResOrNull(): Int? {
    return when (this) {
        AuthError.UserNotSignedIn -> R.string.user_not_signed_in_message
        AuthError.RequestDataNotValid -> R.string.request_contains_invalid_data_message
        AuthError.SessionExpired -> R.string.your_session_expired_message
        AuthError.AppUpdateRequired -> R.string.app_update_required_message
        AuthError.InvalidCredentials -> R.string.credentials_are_invalid_message
        AuthError.ReauthenticationError -> R.string.reauthentication_error
        AuthError.EmailNotVerified -> R.string.email_not_verified_message
        AuthError.UserNotFound -> R.string.user_not_found_message
        AuthError.SignInError -> R.string.sign_in_failed_message
        AuthError.UserAlreadyExists -> R.string.user_already_exists_message
        AuthError.SignUpEmailVerificationError -> R.string.sign_up_email_verification_failed_message
        AuthError.SignUpError -> R.string.sign_up_failed_message
        AuthError.CheckEmailVerificationError -> R.string.email_verification_check_failed_message
        AuthError.OobCodeIsInvalid -> R.string.verification_code_invalid_or_expired_message
        AuthError.FinishSignUpError -> R.string.sign_up_completing_failed_message
        AuthError.RequestEmailUpdateError -> R.string.email_update_request_failed_message
        AuthError.EmailUpdateError -> R.string.email_update_failed_message
        AuthError.PasswordUpdateError -> R.string.password_update_failed_message
        AuthError.ResetPasswordRequestError -> R.string.password_reset_request_failed_message
        AuthError.PasswordResetError -> R.string.password_reset_failed_message
        AuthError.AccountNotDeleted -> R.string.account_deletion_failed_message
    }
}

@StringRes private fun AuthError.asButtonTextRes(): Int {
    return when (this) {
        AuthError.UserNotSignedIn -> R.string.close
        AuthError.RequestDataNotValid -> R.string.close
        AuthError.SessionExpired -> R.string.sign_in
        AuthError.AppUpdateRequired -> R.string.update
        AuthError.InvalidCredentials -> R.string.close
        AuthError.ReauthenticationError -> R.string.close
        AuthError.EmailNotVerified -> R.string.close
        AuthError.UserNotFound -> R.string.close
        AuthError.SignInError -> R.string.close
        AuthError.UserAlreadyExists -> R.string.close
        AuthError.SignUpEmailVerificationError -> R.string.close
        AuthError.SignUpError -> R.string.close
        AuthError.CheckEmailVerificationError -> R.string.close
        AuthError.OobCodeIsInvalid -> R.string.close
        AuthError.FinishSignUpError -> R.string.close
        AuthError.RequestEmailUpdateError -> R.string.close
        AuthError.EmailUpdateError -> R.string.close
        AuthError.PasswordUpdateError -> R.string.close
        AuthError.ResetPasswordRequestError -> R.string.close
        AuthError.PasswordResetError -> R.string.close
        AuthError.AccountNotDeleted -> R.string.close
    }
}

@DrawableRes private fun AuthError.asButtonIconResOrNull(): Int? {
    return when (this) {
        AuthError.UserNotSignedIn -> R.drawable.close_icon
        AuthError.RequestDataNotValid -> R.drawable.close_icon
        AuthError.SessionExpired -> null
        AuthError.AppUpdateRequired -> null
        AuthError.InvalidCredentials -> R.drawable.close_icon
        AuthError.ReauthenticationError -> R.drawable.close_icon
        AuthError.EmailNotVerified -> R.drawable.close_icon
        AuthError.UserNotFound -> R.drawable.close_icon
        AuthError.SignInError -> R.drawable.close_icon
        AuthError.UserAlreadyExists -> R.drawable.close_icon
        AuthError.SignUpEmailVerificationError -> R.drawable.close_icon
        AuthError.SignUpError -> R.drawable.close_icon
        AuthError.CheckEmailVerificationError -> R.drawable.close_icon
        AuthError.OobCodeIsInvalid -> R.drawable.close_icon
        AuthError.FinishSignUpError -> R.drawable.close_icon
        AuthError.RequestEmailUpdateError -> R.drawable.close_icon
        AuthError.EmailUpdateError -> R.drawable.close_icon
        AuthError.PasswordUpdateError -> R.drawable.close_icon
        AuthError.ResetPasswordRequestError -> R.drawable.close_icon
        AuthError.PasswordResetError -> R.drawable.close_icon
        AuthError.AccountNotDeleted -> R.drawable.close_icon
    }
}
