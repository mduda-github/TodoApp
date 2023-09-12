package com.todoapp.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.data.Todo
import com.todoapp.data.TodoDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTodoViewModel @ViewModelInject constructor(
    private val todoDao: TodoDao,
    @Assisted private val state: SavedStateHandle
): ViewModel() {
    val todo = state.get<Todo>("todo")

    var todoName = state.get<String>("todoName") ?: todo?.name ?: ""
        set(value) {
            field = value
            state.set("todoName", value)
        }

    var todoCategory = state.get<String>("todoCategory") ?: todo?.category ?: ""
        set(value) {
            field = value
            state.set("todoCategory", value)
        }

    var todoPriority = state.get<Boolean>("todoPriority") ?: todo?.priority ?: false
        set(value) {
            field = value
            state.set("todoPriority", value)
        }

    private val addEditTodoEventChannel = Channel<AddEditTodoEvent>()
    val addEditTaskEvent = addEditTodoEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (todoName.isBlank()) {
            return
        }

        if (todoCategory.isBlank()) {
            return
        }

        if( todo != null ) {
            val updateTodo = todo.copy(name = todoName, category = todoCategory, priority = todoPriority)
            updateTodo(updateTodo)

        } else {
            val newTodo = Todo(todoName, todoCategory, todoPriority)
            createTodo(newTodo)
        }
    }

    private fun createTodo( todo: Todo) = viewModelScope.launch {
        todoDao.insert(todo)
        addEditTodoEventChannel.send(AddEditTodoEvent.NavigateToHomeScreen)
    }

    private fun updateTodo( todo: Todo) = viewModelScope.launch {
        todoDao.update(todo)
        addEditTodoEventChannel.send(AddEditTodoEvent.NavigateToHomeScreen)
    }

    sealed class AddEditTodoEvent {
        object NavigateToHomeScreen : AddEditTodoEvent()
    }
}