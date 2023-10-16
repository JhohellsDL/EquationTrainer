package com.jdlstudios.equationtrainer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jdlstudios.equationtrainer.data.local.daos.EquationDao
import com.jdlstudios.equationtrainer.data.local.daos.SessionDao
import com.jdlstudios.equationtrainer.data.local.entities.EquationEntity
import com.jdlstudios.equationtrainer.data.local.entities.SessionEntity

@Database(entities = [EquationEntity::class, SessionEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun equationDao(): EquationDao
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var Instance: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(
                    context,
                    AppDataBase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        Instance = it
                    }

            }
        }
    }
}