package com.todoapp.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.todoapp.R
import com.todoapp.databinding.FragmentAddEditTodoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditTodoFragment: Fragment(R.layout.fragment_add_edit_todo) {

    private val viewModel: AddEditTodoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTodoBinding.bind(view)

        binding.apply {
            editTextTodoName.setText(viewModel.todoName)
            editTextCategory.setText(viewModel.todoCategory)
            checkboxPriority.isChecked = viewModel.todoPriority
            checkboxPriority.jumpDrawablesToCurrentState()
            textViewDateCreated.isVisible = viewModel.todo != null
            textViewDateCreated.text = "Created: ${viewModel.todo?.createdTimeFormatted}"

            editTextTodoName.addTextChangedListener {
                viewModel.todoName = it.toString()
            }

            editTextCategory.addTextChangedListener {
                viewModel.todoCategory = it.toString()
            }

            checkboxPriority.setOnCheckedChangeListener { _, isChecked ->
                viewModel.todoPriority = isChecked
            }

            fabSaveTodo.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when(event) {
                    is AddEditTodoViewModel.AddEditTodoEvent.NavigateToHomeScreen ->  {
                        val action = AddEditTodoFragmentDirections.actionAddEditTodoFragmentToTodosFragment2()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }


}

