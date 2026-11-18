package com.example.studentapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String
)
@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<StudentEntity>>
}

@Database(entities = [StudentEntity::class], version = 1, exportSchema = false)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}
class StudentViewModel(
    private val studentDao: StudentDao
) : ViewModel() {
    val students: StateFlow<List<StudentEntity>> =
        studentDao.getAllStudents()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}
@Composable
fun StudentScreen(viewModel: StudentViewModel) {
    val students by viewModel.students.collectAsStateWithLifecycle()
    LazyColumn {
        items(students) { student ->
            Text(
                text = "${student.id} - ${student.name} - ${student.email}",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
