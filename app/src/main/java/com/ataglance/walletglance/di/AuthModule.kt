package com.ataglance.walletglance.di

import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.data.repository.AuthRepositoryImpl
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.auth.domain.model.UserContext
import com.ataglance.walletglance.auth.domain.usecase.ApplyOobCodeUseCase
import com.ataglance.walletglance.auth.domain.usecase.ApplyOobCodeUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.CheckEmailVerificationUseCase
import com.ataglance.walletglance.auth.domain.usecase.CheckEmailVerificationUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.CreateNewUserUseCase
import com.ataglance.walletglance.auth.domain.usecase.CreateNewUserUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.DeleteUserUseCase
import com.ataglance.walletglance.auth.domain.usecase.DeleteUserUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.FinishSignUpUseCase
import com.ataglance.walletglance.auth.domain.usecase.FinishSignUpUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.GetAuthTokenFromSecureStorageUseCase
import com.ataglance.walletglance.auth.domain.usecase.GetAuthTokenFromSecureStorageUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.GetUserDataUseCase
import com.ataglance.walletglance.auth.domain.usecase.GetUserDataUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.GetUserEmailUseCase
import com.ataglance.walletglance.auth.domain.usecase.GetUserEmailUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.ReauthenticateUseCase
import com.ataglance.walletglance.auth.domain.usecase.ReauthenticateUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.RequestEmailUpdateUseCase
import com.ataglance.walletglance.auth.domain.usecase.RequestEmailUpdateUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.RequestPasswordResetUseCase
import com.ataglance.walletglance.auth.domain.usecase.RequestPasswordResetUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.SaveAuthTokenToSecureStorageUseCase
import com.ataglance.walletglance.auth.domain.usecase.SaveAuthTokenToSecureStorageUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.SendEmailVerificationEmailUseCase
import com.ataglance.walletglance.auth.domain.usecase.SendEmailVerificationEmailUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.SetNewPasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.SetNewPasswordUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.SignInUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignInUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignInWithEmailAndPasswordUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.SignOutUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignOutUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.SignUpUseCase
import com.ataglance.walletglance.auth.domain.usecase.SignUpUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.UpdatePasswordUseCase
import com.ataglance.walletglance.auth.domain.usecase.UpdatePasswordUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.UserEmailIsVerifiedUseCase
import com.ataglance.walletglance.auth.domain.usecase.UserEmailIsVerifiedUseCaseImpl
import com.ataglance.walletglance.auth.presentation.viewmodel.FinishSignUpViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.SignInViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    /* ---------- Other ---------- */

    single {
        UserContext(
            getAuthTokenFromSecureStorageUseCase = get(),
            saveAuthTokenToSecureStorageUseCase = get()
        )
    }

    single {
        AuthController(
            userContext = get(),

            getUserEmailUseCase = get(),
            applyOobCodeUseCase = get(),
            createNewUserUseCase = get(),
            signInUseCase = get(),
            getUserDataUseCase = get(),
            sendEmailVerificationEmailUseCase = get(),
            requestEmailUpdateUseCase = get(),
            updatePasswordUseCase = get(),
            requestPasswordResetUseCase = get(),
            setNewPasswordUseCase = get(),
            deleteUserUseCase = get(),
            signOutUseCase = get(),

            saveUserIdPreferenceUseCase = get(),
            getUserIdPreferenceUseCase = get(),
            applyLanguageToSystemUseCase = get(),
            saveLanguagePreferenceUseCase = get(),
            deleteAllDataLocallyUseCase = get()
        )
    }

    /* ---------- Repositories ---------- */

    single<AuthRepository> {
        AuthRepositoryImpl(userContext = get())
    }

    /* ---------- Use Cases ---------- */

    single<GetAuthTokenFromSecureStorageUseCase> {
        GetAuthTokenFromSecureStorageUseCaseImpl(secureStorage = get())
    }
    single<SaveAuthTokenToSecureStorageUseCase> {
        SaveAuthTokenToSecureStorageUseCaseImpl(secureStorage = get())
    }
    single<SignInWithEmailAndPasswordUseCase> {
        SignInWithEmailAndPasswordUseCaseImpl(
            authRepository = get(),
            userContext = get()
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
    single<GetUserEmailUseCase> {
        GetUserEmailUseCaseImpl(auth = get())
    }
    single<UserEmailIsVerifiedUseCase> {
        UserEmailIsVerifiedUseCaseImpl(auth = get())
    }
    single<ApplyOobCodeUseCase> {
        ApplyOobCodeUseCaseImpl(auth = get())
    }
    single<CreateNewUserUseCase> {
        CreateNewUserUseCaseImpl(auth = get(), userRepository = get())
    }
    single<SignInUseCase> {
        SignInUseCaseImpl(auth = get())
    }
    single<ReauthenticateUseCase> {
        ReauthenticateUseCaseImpl(auth = get())
    }
    single<GetUserDataUseCase> {
        GetUserDataUseCaseImpl(userRepository = get())
    }
    single<SendEmailVerificationEmailUseCase> {
        SendEmailVerificationEmailUseCaseImpl(auth = get())
    }
    single<RequestEmailUpdateUseCase> {
        RequestEmailUpdateUseCaseImpl(auth = get())
    }
    single<UpdatePasswordUseCase> {
        UpdatePasswordUseCaseImpl(reauthenticateUseCase = get())
    }
    single<RequestPasswordResetUseCase> {
        RequestPasswordResetUseCaseImpl(auth = get())
    }
    single<SetNewPasswordUseCase> {
        SetNewPasswordUseCaseImpl(auth = get())
    }
    single<DeleteUserUseCase> {
        DeleteUserUseCaseImpl(userRepository = get(), reauthenticateUseCase = get())
    }
    single<SignOutUseCase> {
        SignOutUseCaseImpl(auth = get())
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

}