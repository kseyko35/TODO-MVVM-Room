package com.kseyko.todomvvmroom.ui.view.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kseyko.todomvvmroom.R
import com.kseyko.todomvvmroom.data.SortOrder
import com.kseyko.todomvvmroom.databinding.FragmentTaskBinding
import com.kseyko.todomvvmroom.ui.adapter.TaskAdapter
//import com.kseyko.todomvvmroom.ui.viewmodel.SortOrder
import com.kseyko.todomvvmroom.ui.viewmodel.TaskViewModel
import com.kseyko.todomvvmroom.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {

    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTaskBinding.bind(view)

        val taskAdapter = TaskAdapter()

        binding.apply {
            recyclerviewTask.apply {
                adapter = taskAdapter
                layoutManager =
                    LinearLayoutManager(requireContext()) //as responsible for how to should layout to screen
                setHasFixedSize(true) // Optimization method of the recyclerview. If doesnt change dimension on the screen use it more efficient

            }
        }
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
        setHasOptionsMenu(true) //Dont forget to set menu
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.preferencesFlow.first().hideCompleted
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                // viewModel.sortOrder.value = SortOrder.BY_NAME
                viewModel.onSortOrderSlected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_created -> {
                viewModel.onSortOrderSlected(SortOrder.BY_DATE)
                //viewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                // viewModel.hideCompleted.value = item.isChecked
                true
            }
            R.id.action_delete_all_completed_tasks -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


