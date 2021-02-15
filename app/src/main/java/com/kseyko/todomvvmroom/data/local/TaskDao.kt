package com.kseyko.todomvvmroom.data.local

import androidx.room.*
import com.kseyko.todomvvmroom.data.model.Task
import kotlinx.coroutines.flow.Flow


/**     Code with ❤
╔════════════════════════════╗
║   Created by Seyfi ERCAN   ║
╠════════════════════════════╣
║  seyfiercan35@hotmail.com  ║
╠════════════════════════════╣
║      15,February,2021      ║
╚════════════════════════════╝
 */
@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)//suspend function belongs to the coroutine architecture

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM taskTable")
    fun getTask(): Flow<List<Task>>//with flow, whenever a task in the database is updated, then the entire list of task is emitted again.
    //Flow represent is stream of data. If database table changes room will put automatically list of task into this flow.


}