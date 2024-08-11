package com.example.rad.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Algorithm::class], version = 1)
abstract class AlgorithmDatabase : RoomDatabase() {
    abstract fun algorithmDao(): AlgorithmDao

    companion object {
        @Volatile
        private var INSTANCE: AlgorithmDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AlgorithmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlgorithmDatabase::class.java,
                    "algorithm_database"
                )
                    .addCallback(SeedDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SeedDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.algorithmDao())
                }
            }
        }

        suspend fun populateDatabase(algorithmDao: AlgorithmDao) {
            val insertionSortCode = """
                def sort(arr):
                    steps = []
                    for i in range(1, len(arr)):
                        j = i - 1
                        key = arr[i]
                        while j >= 0 and key < arr[j]:
                            arr[j+1] = arr[j]
                            steps.append(arr[:])
                            j -= 1
                        arr[j+1] = key
                        steps.append(arr[:])
                    return steps
            """.trimIndent()

            algorithmDao.insertAlgorithm(Algorithm(name = "Insertion Sort", code = insertionSortCode))
        }
    }
}

