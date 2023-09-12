package com.todoapp.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.data.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowTodoViewModel @ViewModelInject constructor(
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

    var todoPrority = state.get<Boolean>("todoPriority") ?: todo?.priority ?: false
        set(value) {
            field = value
            state.set("todoPriority", value)
        }

    private val showTodoEventChannel = Channel<ShowTodoEvent>()
    val showTodoEvent = showTodoEventChannel.receiveAsFlow()

    fun editTodo( todo: Todo) = viewModelScope.launch {
        showTodoEventChannel.send(ShowTodoEvent.NavigateToEditTodoScreen(todo))
    }

    fun shareTodo(context: Context, info: String) = viewModelScope.launch {
        withContext(Dispatchers.Main) {
            val intent = Intent(ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(EXTRA_TEXT, info)
            context.startActivity(createChooser(intent, "Share todo"))
        }
    }

    sealed class ShowTodoEvent {
        data class NavigateToEditTodoScreen(val todo: Todo) : ShowTodoEvent()
    }
}