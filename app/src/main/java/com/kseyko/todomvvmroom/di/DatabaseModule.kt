package com.kseyko.todomvvmroom.di

import android.content.Context
import androidx.room.Room
import com.kseyko.todomvvmroom.data.local.TaskDao
import com.kseyko.todomvvmroom.data.local.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


/**     Code with ❤
╔════════════════════════════╗
║   Created by Seyfi ERCAN   ║
╠════════════════════════════╣
║  seyfiercan35@hotmail.com  ║
╠════════════════════════════╣
║      15,February,2021      ║
╚════════════════════════════╝
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun taskDatabase(@ApplicationContext context: Context, callback:TaskDatabase.Callback): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "taskDatabase.db")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    @Singleton
    fun taskDao(taskDatabase: TaskDatabase): TaskDao = taskDatabase.taskDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideAppScope()= CoroutineScope(SupervisorJob())
}
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
