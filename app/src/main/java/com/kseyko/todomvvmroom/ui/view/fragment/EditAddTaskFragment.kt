package com.kseyko.todomvvmroom.ui.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kseyko.todomvvmroom.R
import com.kseyko.todomvvmroom.databinding.FragmentEditAddTaskBinding
import com.kseyko.todomvvmroom.ui.viewmodel.EditAddTaskViewModel
import com.kseyko.todomvvmroom.ui.viewmodel.TaskViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditAddTaskFragment : Fragment(R.layout.fragment_edit_add_task) {

    private val viewModel: EditAddTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEditAddTaskBinding.bind(view)

        binding.apply {
            editTextTask.setText(viewModel.taskName)
            checkBoxImportant.isChecked = viewModel.taskImportance
            checkBoxImportant.jumpDrawablesToCurrentState()
            textViewCreatedDate.isVisible = viewModel.task !=null
            textViewCreatedDate.text = "Created: ${viewModel.task?.createdDateFormatted} "
        }
    }

}