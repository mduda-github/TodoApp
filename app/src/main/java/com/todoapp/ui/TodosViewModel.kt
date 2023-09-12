package com.todoapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.todoapp.data.Todo
import com.todoapp.data.TodoDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TodosViewModel @ViewModelInject constructor(
    private val todoDao: TodoDao
): ViewModel() {

    private val todosEventChannel = Channel<TodosEvent>()
    val todosEvent = todosEventChannel.receiveAsFlow()

    private val todosFlow = todoDao.getTodosSortedByName()

    val todos = todosFlow.asLiveData()

    fun showTodosTotal(): Int? {
        return todos?.value?.size
    }

    fun onTodoSelected(todo: Todo) = viewModelScope.launch {
        todosEventChannel.send(TodosEvent.NavigateToShowTodoScreen(todo))
    }

    fun onTodoCheckedChanged(todo: Todo, isChecked: Boolean) = viewModelScope.launch {
        todoDao.update(todo.copy(completed = isChecked))
    }

    fun onTodoLongClick(todo: Todo) = viewModelScope.launch {
        todosEventChannel.send(TodosEvent.NavigateToDeleteTodoScreen(todo))
    }

    fun onAddNewTodoClick() = viewModelScope.launch {
        todosEventChannel.send(TodosEvent.NavigateToAddTodoScreen)
    }

    sealed class TodosEvent {
        object NavigateToAddTodoScreen : TodosEvent()
        data class NavigateToShowTodoScreen(val todo: Todo) : TodosEvent()
        data class NavigateToDeleteTodoScreen(val todo: Todo) : TodosEvent()
    }
}