package com.kseyko.todomvvmroom.data


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat


/**     Code with ❤
╔════════════════════════════╗
║   Created by Seyfi ERCAN   ║
╠════════════════════════════╣
║  seyfiercan35@hotmail.com  ║
╠════════════════════════════╣
║      14,February,2021      ║
╚════════════════════════════╝
 */
@Entity(tableName = "taskTable")
@Parcelize
data class Task(
    val name: String,
    val important: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val completed: Boolean = false,

    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
}