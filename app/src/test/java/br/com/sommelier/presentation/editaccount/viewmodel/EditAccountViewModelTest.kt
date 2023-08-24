package br.com.sommelier.presentation.editaccount.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.sommelier.domain.usecase.GetUserDocumentUseCase
import br.com.sommelier.domain.usecase.UpdateUserDocumentUseCase
import br.com.sommelier.testrule.CoroutineTestRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

class EditAccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val getUserDocumentUseCase = mockk<GetUserDocumentUseCase>()

    private val updateUserDocumentUseCase = mockk<UpdateUserDocumentUseCase>()

    private lateinit var viewModel: EditAccountViewModel

    @Before
    fun setUp() {
        viewModel = EditAccountViewModel(
            getUserDocumentUseCase,
            updateUserDocumentUseCase
        )
    }
}