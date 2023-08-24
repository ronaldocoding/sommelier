package br.com.sommelier.presentation.editaccount.viewmodel

import androidx.lifecycle.ViewModel
import br.com.sommelier.domain.usecase.GetUserDocumentUseCase
import br.com.sommelier.domain.usecase.UpdateUserDocumentUseCase
import br.com.sommelier.presentation.editaccount.action.EditAccountAction

class EditAccountViewModel(
    private val getUserDocumentUseCase: GetUserDocumentUseCase,
    private val updateUserDocumentUseCase: UpdateUserDocumentUseCase
) : ViewModel(), EditAccountAction {

    override fun sendAction(action: EditAccountAction.Action) {
         when (action) {
             else -> {}
         }
    }
}