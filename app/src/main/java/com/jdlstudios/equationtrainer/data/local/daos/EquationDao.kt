package com.jdlstudios.equationtrainer.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jdlstudios.equationtrainer.data.local.entities.EquationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EquationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(equation: EquationEntity)

    @Update
    suspend fun update(equation: EquationEntity)

    @Delete
    suspend fun delete(equation: EquationEntity)

    @Query("SELECT * FROM equations WHERE id = :id")
    fun getEquation(id: Int): Flow<EquationEntity>

    @Query("SELECT * FROM equations")
    fun getAllEquations(): Flow<List<EquationEntity>>
}