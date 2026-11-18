package com.example.studentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

// 5. MainActivity
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            StudentDatabase::class.java,
            "students.db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO students (id, name, email) VALUES (1, 'Aarav Sharma', 'aarav@college.edu')")
                db.execSQL("INSERT INTO students (id, name, email) VALUES (2, 'Priya Verma', 'priya@college.edu')")
                db.execSQL("INSERT INTO students (id, name, email) VALUES (3, 'Rohan Gupta', 'rohan@college.edu')")
            }
        }).build()

        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StudentViewModel(db.studentDao()) as T
            }
        }

        setContent {
            val viewModel: StudentViewModel = viewModel(factory = factory)

            Box(modifier = Modifier.statusBarsPadding()) {
                StudentScreen(viewModel)
            }
        }
    }
}
