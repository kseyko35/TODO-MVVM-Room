package com.kseyko.todomvvmroom.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.kseyko.todomvvmroom.data.local.TaskDao
import com.kseyko.todomvvmroom.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


/**     Code with ❤
╔════════════════════════════╗
║   Created by Seyfi ERCAN   ║
╠════════════════════════════╣
║  seyfiercan35@hotmail.com  ║
╠════════════════════════════╣
║      28,March,2021      ║
╚════════════════════════════╝
 */
@HiltViewModel
class DeleteAllCompletedDialogViewModel @Inject constructor(
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel(){

    fun onConfirmClick() = applicationScope.launch {
        taskDao.deleteCompletedTasks()
    }
}