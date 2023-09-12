package com.todoapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "todo_table")
@Parcelize
data class Todo (
    val name: String,
    val category: String,
    val priority: Boolean = false,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
        ) : Parcelable {
    val createdTimeFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
}