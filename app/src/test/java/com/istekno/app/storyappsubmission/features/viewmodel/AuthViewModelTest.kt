package com.istekno.app.storyappsubmission.features.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.istekno.app.storyappsubmission.db.network.auth.AuthService
import com.istekno.app.storyappsubmission.features.auth.AuthViewModel
import com.istekno.app.storyappsubmission.features.utils.CoroutinesTestRule
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyLoginRequest
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyLoginResponse
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyLoginResponseError
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyRegisterRequest
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyRegisterResponse
import com.istekno.app.storyappsubmission.features.utils.DataDummy.generateDummyRegisterResponseError
import com.istekno.app.storyappsubmission.features.utils.ReflectUtils
import com.istekno.app.storyappsubmission.utils.Resource
import com.istekno.app.storyappsubmission.utils.Status
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var authViewModel: AuthViewModel

    @Mock
    private lateinit var authService: AuthService

    @Before
    fun setup() {
        authViewModel = AuthViewModel()
    }

    @Test
    fun `register success - Resource Success`() = runTest {
        ReflectUtils.setField(authViewModel, authViewModel.javaClass.getDeclaredField("client"), authService)
        val expected = Resource.success(generateDummyRegisterResponse())

        `when`(
            authService.registerUserAsync(generateDummyRegisterRequest())
        ).thenReturn(expected.data)
        authViewModel.userRegister(generateDummyRegisterRequest())

        assertNotNull(authViewModel.registerData.value?.data)
        assertEquals(Status.SUCCESS, authViewModel.registerData.value?.status)
    }

    @Test
    fun `register failed - Resource Error`() = runTest {
        ReflectUtils.setField(authViewModel, authViewModel.javaClass.getDeclaredField("client"), authService)
        val expected = Resource.error("failed", generateDummyRegisterResponseError())

        `when`(
            authService.registerUserAsync(generateDummyRegisterRequest())
        ).thenReturn(expected.data)
        authViewModel.userRegister(generateDummyRegisterRequest())

        assertNull(authViewModel.registerData.value?.data)
        assertEquals(Status.ERROR, authViewModel.registerData.value?.status)
    }

    @Test
    fun `login success - Resource Success`() = runTest {
        ReflectUtils.setField(authViewModel, authViewModel.javaClass.getDeclaredField("client"), authService)
        val expected = Resource.success(generateDummyLoginResponse())

        `when`(
            authService.loginUserAsync(generateDummyLoginRequest())
        ).thenReturn(expected.data)
        authViewModel.userLogin(generateDummyLoginRequest())

        assertNotNull(authViewModel.loginData.value?.data)
        assertEquals(Status.SUCCESS, authViewModel.loginData.value?.status)
    }

    @Test
    fun `login failed - Resource Error`() = runTest {
        ReflectUtils.setField(authViewModel, authViewModel.javaClass.getDeclaredField("client"), authService)
        val expected = Resource.error("failed", generateDummyLoginResponseError())

        `when`(
            authService.loginUserAsync(generateDummyLoginRequest())
        ).thenReturn(expected.data)
        authViewModel.userLogin(generateDummyLoginRequest())

        assertNull(authViewModel.loginData.value?.data)
        assertEquals(Status.ERROR, authViewModel.loginData.value?.status)
    }
}