package com.ataglance.walletglance.di

import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.data.repository.AuthRepositoryImpl
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.domain.usecase.auth.CheckEmailVerificationUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.CheckEmailVerificationUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.CheckTokenValidityUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.CheckTokenValidityUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.DeleteAccountUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.DeleteAccountUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.DeleteAuthTokenFromSecureStorageUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.DeleteAuthTokenFromSecureStorageUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.FinishSignUpUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.FinishSignUpUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.RequestEmailUpdateUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.RequestEmailUpdateUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.RequestPasswordResetUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.RequestPasswordResetUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.ResetPasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.ResetPasswordUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.SignInWithEmailAndPasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.SignInWithEmailAndPasswordUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.SignOutUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.SignOutUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.SignUpUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.SignUpUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.UpdatePasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.UpdatePasswordUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.auth.VerifyEmailUpdateUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.VerifyEmailUpdateUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.authToken.GetAuthTokenFromSecureStorageUseCase
import com.ataglance.walletglance.auth.domain.usecase.authToken.GetAuthTokenFromSecureStorageUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.authToken.SaveAuthTokenToSecureStorageUseCase
import com.ataglance.walletglance.auth.domain.usecase.authToken.SaveAuthTokenToSecureStorageUseCaseImpl
import com.ataglance.walletglance.auth.presentation.viewmodel.DeleteAccountViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.EmailUpdateVerifyViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.EmailUpdateViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.PasswordResetRequestViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.PasswordResetViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.PasswordUpdateViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.ProfileViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.SignInViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.SignUpFinishViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.SignUpViewModel
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
        AuthRepositoryImpl(
            userContext = get(),
            httpClient = get()
        )
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

    /* ---------- ViewModels ---------- */

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
        SignUpFinishViewModel(
            oobCode = params.get(),
            finishSignUpUseCase = get()
        )
    }

    viewModel {
        EmailUpdateViewModel(
            requestEmailUpdateUseCase = get(),
            checkEmailVerificationUseCase = get()
        )
    }

    viewModel { params ->
        EmailUpdateVerifyViewModel(
            oobCode = params.get(),
            verifyEmailUpdateUseCase = get()
        )
    }

    viewModel {
        PasswordUpdateViewModel(updatePasswordUseCase = get())
    }

    viewModel { params ->
        PasswordResetRequestViewModel(
            email = params.get(),
            requestPasswordResetUseCase = get()
        )
    }

    viewModel { params ->
        PasswordResetViewModel(
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