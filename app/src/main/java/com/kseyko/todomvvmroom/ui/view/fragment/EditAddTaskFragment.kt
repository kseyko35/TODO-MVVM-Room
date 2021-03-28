package com.kseyko.todomvvmroom.ui.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kseyko.todomvvmroom.R
import com.kseyko.todomvvmroom.databinding.FragmentEditAddTaskBinding
import com.kseyko.todomvvmroom.ui.viewmodel.EditAddTaskViewModel
import com.kseyko.todomvvmroom.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


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
            textViewCreatedDate.isVisible = viewModel.task != null
            textViewCreatedDate.text = "Created: ${viewModel.task?.createdDateFormatted} "

            editTextTask.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            checkBoxImportant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }

            fabSaveTask.setOnClickListener {
                viewModel.onSaveClick()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect{ event->
                when(event){
                    is EditAddTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage->{
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_LONG).show()
                    }
                    is EditAddTaskViewModel.AddEditTaskEvent.NavigateBackWithResult->{
                        binding.editTextTask.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }

}