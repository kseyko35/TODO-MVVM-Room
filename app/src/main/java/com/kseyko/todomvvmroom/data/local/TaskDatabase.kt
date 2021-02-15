package com.kseyko.todomvvmroom.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kseyko.todomvvmroom.data.model.Task
import com.kseyko.todomvvmroom.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


/**     Code with ❤
╔════════════════════════════╗
║   Created by Seyfi ERCAN   ║
╠════════════════════════════╣
║  seyfiercan35@hotmail.com  ║
╠════════════════════════════╣
║      15,February,2021      ║
╚════════════════════════════╝
 */
@Database(
    entities = [Task::class],
    version = 1
)//This is how we can update database later if we change schema of it
abstract class TaskDatabase : RoomDatabase() {


    abstract fun taskDao(): TaskDao // fun is abstract because we dont wanna define it here. We just wanna declare function

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val appScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            appScope.launch {
                dao.insert(Task("Wash the dishes"))
                dao.insert(Task("Do the laundry"))
                dao.insert(Task("Buy groceries", important = true))
                dao.insert(Task("Prepare food", completed = true))
                dao.insert(Task("Call mom"))
                dao.insert(Task("Visit grandma", completed = true))
                dao.insert(Task("Repair my bike"))
                dao.insert(Task("Call Elon Musk"))
            }

            // GlobalScope  //It is belong to coroutines library.it runs as long as app is running until coroutine is finished
            //This is bad case

        }

    }
}