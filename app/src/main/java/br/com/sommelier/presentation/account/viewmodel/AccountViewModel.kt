package br.com.sommelier.presentation.account.viewmodel

import androidx.lifecycle.ViewModel
import br.com.sommelier.domain.usecase.DeleteUserUseCase
import br.com.sommelier.domain.usecase.GetUserDocumentUseCase
import br.com.sommelier.domain.usecase.SignOutUserUseCase
import br.com.sommelier.presentation.account.action.AccountAction

class AccountViewModel(
    private val getUserDocumentUseCase: GetUserDocumentUseCase,
    private val logOutUserUseCase: SignOutUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel(), AccountAction {

    override fun sendAction(action: AccountAction.Action) {
        TODO("Not yet implemented")
    }
}