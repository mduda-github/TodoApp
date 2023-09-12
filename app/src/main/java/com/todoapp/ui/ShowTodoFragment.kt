package com.todoapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.todoapp.R
import com.todoapp.databinding.FragmentShowTodoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ShowTodoFragment: Fragment(R.layout.fragment_show_todo) {

    private val viewModel: ShowTodoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentShowTodoBinding.bind(view)

        binding.apply {

            showTodoName.text = viewModel.todoName
            showCategory.text = viewModel.todoCategory
            showCheckboxPriority.isChecked = viewModel.todoPrority
            showCheckboxPriority.jumpDrawablesToCurrentState()
            showDateCreated.text = "Created: ${viewModel.todo?.createdTimeFormatted}"


            fabEditTodo.setOnClickListener {
                viewModel.editTodo(
                    viewModel.todo!!.copy(name = viewModel.todoName, category = viewModel.todoCategory, priority = viewModel.todoPrority)
                )
            }
            fabShareTodo.setOnClickListener {
                val context = it.context
                val info = viewModel.todoName + " " + viewModel.todoCategory + " " + viewModel.todo?.createdTimeFormatted
                viewModel.shareTodo(context, info)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.showTodoEvent.collect {
                    event -> when(event) {
                is ShowTodoViewModel.ShowTodoEvent.NavigateToEditTodoScreen -> {
                    val action = ShowTodoFragmentDirections.actionShowTodoFragmentToAddEditTodoFragment(event.todo, "Todo Edit")
                    findNavController().navigate(action)
                }
            }
            }
        }

    }


}

