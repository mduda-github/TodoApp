package com.todoapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.todoapp.data.Todo
import com.todoapp.data.TodoDao
import com.todoapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteTodoViewModel @ViewModelInject constructor(
    private val todoDao: TodoDao,
    @ApplicationScope private val applicationScope: CoroutineScope
): ViewModel() {
    fun onConfirmClick(todo: Todo) = applicationScope.launch {
        todoDao.delete(todo);
    }
}