package com.kseyko.todomvvmroom.ui.adapter

import android.content.ReceiverCallNotAllowedException
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kseyko.todomvvmroom.data.model.Task
import com.kseyko.todomvvmroom.databinding.ItemTaskBinding


/**     Code with ❤
╔════════════════════════════╗
║   Created by Seyfi ERCAN   ║
╠════════════════════════════╣
║  seyfiercan35@hotmail.com  ║
╠════════════════════════════╣
║      17,February,2021      ║
╚════════════════════════════╝
 */
class TaskAdapter(private val listener: onItemClickListener ) : ListAdapter<Task, TaskAdapter.TasksViewHolder>(DiffCallBack()) {


    inner class TasksViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
             binding.apply {
                 root.setOnClickListener{
                     val position = adapterPosition
                     if(position!=RecyclerView.NO_POSITION){
                         val task= getItem(position)
                         listener.onItemClick(task)
                     }
                 }
                 checkBoxCompleted.setOnClickListener{
                     val position = adapterPosition
                     if(position!= RecyclerView.NO_POSITION){
                         val task= getItem(position)
                         listener.onCheckBoxClick(task,checkBoxCompleted.isChecked)
                     }
                 }
             }
        }
        fun bind(task: Task) = with(binding) {

                checkBoxCompleted.isChecked = task.completed
                textViewTask.text = task.name
                textViewTask.paint.isStrikeThruText = task.completed
                imageViewPriority.isVisible = task.important
        }
    }

    interface onItemClickListener{
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task,isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {

        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }


    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallBack :
        DiffUtil.ItemCallback<Task>() { // We dont wanna change all list. we just will change data which needs changing
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
        // Is used instead of notifyDataSetChanged. We dont have to change all item if there is no changing
    }

}
