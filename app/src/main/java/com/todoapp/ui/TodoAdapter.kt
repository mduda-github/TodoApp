package com.todoapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.todoapp.data.Todo
import com.todoapp.databinding.ItemTodoBinding

class TodoAdapter(private val listener: OnItemClickListener): ListAdapter<Todo, TodoAdapter.TodoViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TodoViewHolder(private val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }
                root.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemLongClick(task)
                    }
                    true
                }
                checkboxDone.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckboxClick(task, checkboxDone.isChecked)
                    }
                }
            }
        }
        fun bind(todo: Todo) {
            binding.apply {
                checkboxDone.isChecked = todo.completed
                todoName.text = todo.name
                todoName.paint.isStrikeThruText = todo.completed
                todoCategory.text = todo.category
                importantMark.isVisible = todo.priority
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(todo: Todo)
        fun onItemLongClick(todo: Todo)
        fun onCheckboxClick(todo: Todo, isChecked: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }

    }
}