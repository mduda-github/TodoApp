package com.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.todoapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Todo::class], version = 3)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao

    class Callback @Inject constructor(
        private val database: Provider<TodoDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().todoDao()

            applicationScope.launch {
                dao.insert(Todo("buy ketchup","groceries"))
                dao.insert(Todo("walk dog","todo", priority = true))
                dao.insert(Todo("find mayonnaise","groceries"))
                dao.insert(Todo("submit PRM","uni", priority = true))
                dao.insert(Todo("pass all classes","uni", completed = true))
                dao.insert(Todo("water plants","todo"))
                dao.insert(Todo("make pancakes","todo", completed = true))
            }

        }
    }
}