package com.todoapp.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.todoapp.data.Todo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteTodoDialogFragment: DialogFragment() {

    private val viewModel: DeleteTodoViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val todo = arguments?.get("todo")
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Are you sure to delete this todo?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") { _, _ ->
                viewModel.onConfirmClick(todo as Todo)
            }
            .create()

    }

}