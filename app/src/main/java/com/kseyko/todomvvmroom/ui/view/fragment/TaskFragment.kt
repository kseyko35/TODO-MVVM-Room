package com.kseyko.todomvvmroom.ui.view.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kseyko.todomvvmroom.R
import com.kseyko.todomvvmroom.data.SortOrder
import com.kseyko.todomvvmroom.data.model.Task
import com.kseyko.todomvvmroom.databinding.FragmentTaskBinding
import com.kseyko.todomvvmroom.ui.adapter.TaskAdapter
//import com.kseyko.todomvvmroom.ui.viewmodel.SortOrder
import com.kseyko.todomvvmroom.ui.viewmodel.TaskViewModel
import com.kseyko.todomvvmroom.util.exhaustive
import com.kseyko.todomvvmroom.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task), TaskAdapter.onItemClickListener {

    private val viewModel: TaskViewModel by viewModels()

    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTaskBinding.bind(view)

        val taskAdapter = TaskAdapter(this)

        binding.apply {
            recyclerviewTask.apply {
                adapter = taskAdapter
                layoutManager =
                    LinearLayoutManager(requireContext()) //as responsible for how to should layout to screen
                setHasFixedSize(true) // Optimization method of the recyclerview. If doesnt change dimension on the screen use it more efficient

            }
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false // I dont care move method because it is working up and down drag and drop function
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(recyclerviewTask)//we have to attach to recyclerview to work together

            fabAddTask.setOnClickListener {
                 viewModel.onAddNewTaskClick()
            }
        }

        setFragmentResultListener("add_edit_request"){_,bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event->
                when(event){
                    is TaskViewModel.TasksEvent.ShowUndoDeletedTaskMessage->{
                        Snackbar.make(requireView(),"Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO"){
                                viewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                    TaskViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                        val action= TaskFragmentDirections.actionTaskFragmentToEditAddTaskFragment(null,"New Task")
                        findNavController().navigate(action)
                    }
                    is TaskViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        val action= TaskFragmentDirections.actionTaskFragmentToEditAddTaskFragment(event.task, "Edit Task")
                        findNavController().navigate(action)
                    }
                    is TaskViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_SHORT).show()
                    }
                    TaskViewModel.TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                        val action = TaskFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true) //Dont forget to set menu
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery= viewModel.searchQuery.value
        if(pendingQuery !=null && pendingQuery.isNotEmpty()){ // Solve the rotation bug.
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

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
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_created -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
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
                viewModel.onDeleteAllCompletedClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task,isChecked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}


