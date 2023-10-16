package com.jdlstudios.equationtrainer.data.repositories

import com.jdlstudios.equationtrainer.data.local.entities.EquationEntity
import kotlinx.coroutines.flow.Flow

interface EquationsRepository {

    fun getAllEquationsStream(): Flow<List<EquationEntity>>

    fun getEquationStream(id: Int): Flow<EquationEntity?>

    suspend fun insertEquation(equation: EquationEntity)

    suspend fun deleteEquation(equation: EquationEntity)

    suspend fun updateEquation(equation: EquationEntity)
}
