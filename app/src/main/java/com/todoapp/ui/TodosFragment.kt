package com.todoapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.todoapp.R
import com.todoapp.data.Todo
import com.todoapp.databinding.FragmentTodosBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_todos.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TodosFragment : Fragment(R.layout.fragment_todos), TodoAdapter.OnItemClickListener {
    private val viewModel: TodosViewModel by viewModels();

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTodosBinding.bind(view)
        val todoAdapter = TodoAdapter(this)

        binding.apply {
            recyclerViewTodos.apply {
                adapter = todoAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            fabAddTodo.setOnClickListener {
                viewModel.onAddNewTodoClick()
            }
        }

        viewModel.todos.observe(viewLifecycleOwner) {
            todoAdapter.submitList(it)
            total.text = "Remaining todos: " + viewModel.showTodosTotal().toString()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.todosEvent.collect {
                event -> when(event) {
                    is TodosViewModel.TodosEvent.NavigateToAddTodoScreen -> {
                        val action = TodosFragmentDirections.actionTodosFragment2ToAddEditTodoFragment(null, "New Todo")
                        findNavController().navigate(action)
                    }
                    is TodosViewModel.TodosEvent.NavigateToShowTodoScreen -> {
                        val action = TodosFragmentDirections.actionTodosFragment2ToShowTodoFragment(event.todo, "Todo Details")
                        findNavController().navigate(action)
                    }
                    is TodosViewModel.TodosEvent.NavigateToDeleteTodoScreen -> {
                        val action = TodosFragmentDirections.actionGlobalDeleteTodoDialogFragment(event.todo)
                        findNavController().navigate(action)
                    }
                }
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onItemClick(todo: Todo) {
        viewModel.onTodoSelected(todo)
    }

    override fun onItemLongClick(todo: Todo) {
        viewModel.onTodoLongClick(todo)
    }

    override fun onCheckboxClick(todo: Todo, isChecked: Boolean) {
        viewModel.onTodoCheckedChanged(todo, isChecked)
    }
}