package com.kseyko.todomvvmroom.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.kseyko.todomvvmroom.data.local.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
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
class TaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel()