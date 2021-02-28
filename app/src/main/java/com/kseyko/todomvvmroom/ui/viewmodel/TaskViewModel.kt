package com.kseyko.todomvvmroom.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kseyko.todomvvmroom.data.local.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject


/**     Code with ❤
╔════════════════════════════╗
║   Created by Seyfi ERCAN   ║
╠════════════════════════════╣
║  seyfiercan35@hotmail.com  ║
╠════════════════════════════╣
║      16,February,2021      ║
╚════════════════════════════╝
 */
@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel(){

    val searchQuery = MutableStateFlow("")

    val sortOrder = MutableStateFlow( SortOrder.BY_DATE)

    val hideCompleted = MutableStateFlow( false)

    private val tasksFlow = combine(searchQuery,sortOrder,hideCompleted){
        query,sortOrder,hideCompleted ->
        Triple(query,sortOrder,hideCompleted)
    }.flatMapLatest {//(query,sortOrder,hideCompleted)->
        taskDao.getTasks(it.first,it.second,it.third)
    }




    val tasks = tasksFlow.asLiveData()
}
enum class SortOrder{ BY_NAME, BY_DATE }