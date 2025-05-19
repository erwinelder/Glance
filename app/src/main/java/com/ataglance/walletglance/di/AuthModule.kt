package com.ataglance.walletglance.di

import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.data.repository.AuthRepositoryImpl
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.domain.usecase.CheckEmailVerificationUseCase
import com.ataglance.walletglance.auth.domain.usecase.CheckEmailVerificationUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.CheckTokenValidityUseCase
import com.ataglance.walletglance.auth.domain.usecase.CheckTokenValidityUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.DeleteAccountUseCase
import com.ataglance.walletglance.auth.domain.usecase.DeleteAccountUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.DeleteAuthTokenFromSecureStorageUseCase
import com.ataglance.walletglance.auth.domain.usecase.DeleteAuthTokenFromSecureStorageUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.FinishSignUpUseCase
import com.ataglance.walletglance.auth.domain.usecase.FinishSignUpUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.RequestEmailUpdateUseCase
import com.ataglance.walletglance.auth.domain.usecase.RequestEmailUpdateUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.RequestPasswordResetUseCase
import com.ataglance.walletglance.auth.domain.usecase.RequestPasswordResetUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.ResetPasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.ResetPasswordUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignInWithEmailAndPasswordUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.SignOutUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignOutUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.SignUpUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignUpUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.UpdatePasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.UpdatePasswordUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.VerifyEmailUpdateUseCase
import com.ataglance.walletglance.auth.domain.usecase.VerifyEmailUpdateUseCaseImpl
import com.ataglance.walletglance.auth.presentation.viewmodel.DeleteAccountViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.FinishSignUpViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.ProfileViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.RequestPasswordResetViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.ResetPasswordViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.SignInViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.SignUpViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.UpdateEmailViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.UpdatePasswordViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.VerifyEmailUpdateViewModel
import com.ataglance.walletglance.settings.domain.usecase.authToken.GetAuthTokenFromSecureStorageUseCase
import com.ataglance.walletglance.settings.domain.usecase.authToken.GetAuthTokenFromSecureStorageUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.authToken.SaveAuthTokenToSecureStorageUseCase
import com.ataglance.walletglance.settings.domain.usecase.authToken.SaveAuthTokenToSecureStorageUseCaseImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    /* ---------- Other ---------- */

    single {
        UserContext(
            getAuthTokenFromSecureStorageUseCase = get(),
            saveAuthTokenToSecureStorageUseCase = get(),
            deleteAuthTokenFromSecureStorageUseCase = get()
        )
    }

    /* ---------- Repositories ---------- */

    single<AuthRepository> {
        AuthRepositoryImpl(userContext = get())
    }

    /* ---------- Use Cases ---------- */

    single<CheckTokenValidityUseCase> {
        CheckTokenValidityUseCaseImpl(
            secureStorage = get(),
            authRepository = get(),
            userContext = get(),
            getUserProfileLocalTimestampUseCase = get(),
            getLanguagePreferenceUseCase = get(),
            saveLanguageLocallyUseCase = get(),
            saveLanguagePreferenceRemotelyUseCase = get()
        )
    }
    single<SignInWithEmailAndPasswordUseCase> {
        SignInWithEmailAndPasswordUseCaseImpl(
            authRepository = get(),
            userContext = get(),
            saveLanguageLocallyUseCase = get()
        )
    }
    single<SignUpUseCase> {
        SignUpUseCaseImpl(
            authRepository = get(),
            getLanguagePreferenceUseCase = get()
        )
    }
    single<CheckEmailVerificationUseCase> {
        CheckEmailVerificationUseCaseImpl(
            authRepository = get(),
            userContext = get()
        )
    }
    single<FinishSignUpUseCase> {
        FinishSignUpUseCaseImpl(
            authRepository = get(),
            userContext = get()
        )
    }
    single<RequestEmailUpdateUseCase> {
        RequestEmailUpdateUseCaseImpl(authRepository = get())
    }
    single<VerifyEmailUpdateUseCase> {
        VerifyEmailUpdateUseCaseImpl(
            authRepository = get(),
            userContext = get()
        )
    }
    single<UpdatePasswordUseCase> {
        UpdatePasswordUseCaseImpl(authRepository = get())
    }
    single<RequestPasswordResetUseCase> {
        RequestPasswordResetUseCaseImpl(authRepository = get())
    }
    single<ResetPasswordUseCase> {
        ResetPasswordUseCaseImpl(authRepository = get())
    }
    single<DeleteAccountUseCase> {
        DeleteAccountUseCaseImpl(
            authRepository = get(),
            userContext = get(),
            deleteAllDataLocallyUseCase = get()
        )
    }
    single<SignOutUseCase> {
        SignOutUseCaseImpl(userContext = get())
    }

    single<GetAuthTokenFromSecureStorageUseCase> {
        GetAuthTokenFromSecureStorageUseCaseImpl(secureStorage = get())
    }
    single<SaveAuthTokenToSecureStorageUseCase> {
        SaveAuthTokenToSecureStorageUseCaseImpl(secureStorage = get())
    }
    single<DeleteAuthTokenFromSecureStorageUseCase> {
        DeleteAuthTokenFromSecureStorageUseCaseImpl(secureStorage = get())
    }

    /* ---------- View models ---------- */

    viewModel { params ->
        SignInViewModel(
            email = params.get(),
            signInUseCase = get()
        )
    }

    viewModel { params ->
        SignUpViewModel(
            email = params.get(),
            signUpUseCase = get(),
            checkEmailVerificationUseCase = get()
        )
    }

    viewModel { params ->
        FinishSignUpViewModel(
            oobCode = params.get(),
            finishSignUpUseCase = get()
        )
    }

    viewModel {
        UpdateEmailViewModel(
            requestEmailUpdateUseCase = get(),
            checkEmailVerificationUseCase = get()
        )
    }

    viewModel { params ->
        VerifyEmailUpdateViewModel(
            oobCode = params.get(),
            verifyEmailUpdateUseCase = get()
        )
    }

    viewModel {
        UpdatePasswordViewModel(updatePasswordUseCase = get())
    }

    viewModel { params ->
        RequestPasswordResetViewModel(
            email = params.get(),
            requestPasswordResetUseCase = get()
        )
    }

    viewModel { params ->
        ResetPasswordViewModel(
            oobCode = params.get(),
            resetPasswordUseCase = get()
        )
    }

    viewModel {
        DeleteAccountViewModel(deleteAccountUseCase = get())
    }

    viewModel {
        ProfileViewModel(signOutUseCase = get())
    }

}