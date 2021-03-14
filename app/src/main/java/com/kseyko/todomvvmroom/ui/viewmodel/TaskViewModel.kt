package com.kseyko.todomvvmroom.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kseyko.todomvvmroom.data.PreferencesManager
import com.kseyko.todomvvmroom.data.SortOrder
import com.kseyko.todomvvmroom.data.local.TaskDao
import com.kseyko.todomvvmroom.data.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    /*val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)
    private val tasksFlow =
        combine(searchQuery, sortOrder, hideCompleted) { query, sortOrder, hideCompleted ->
            Triple(query, sortOrder, hideCompleted)
        }.flatMapLatest {//(query,sortOrder,hideCompleted)->
            taskDao.getTasks(it.first, it.second, it.third)
        }*/
    private val tasksFlow = combine(searchQuery, preferencesFlow) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasks(
            query,
            filterPreferences.sortOrder,
            filterPreferences.hideCompleted
        )
    }

    val tasks = tasksFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder)= viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }
    fun onHideCompletedClick(hideCompleted:Boolean)= viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }
    fun onTaskSelected(task:Task){

    }
    fun onTaskCheckedChanged(task:Task, isChecked:Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(completed = isChecked ))
    }
    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeletedTaskMessage(task))

    }
    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    sealed class TasksEvent{
        data class ShowUndoDeletedTaskMessage(val task:Task) : TasksEvent()
    }



}
