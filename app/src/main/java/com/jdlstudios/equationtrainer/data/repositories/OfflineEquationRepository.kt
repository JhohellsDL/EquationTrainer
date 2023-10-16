package com.jdlstudios.equationtrainer.data.repositories

import com.jdlstudios.equationtrainer.data.local.daos.EquationDao
import com.jdlstudios.equationtrainer.data.local.entities.EquationEntity
import kotlinx.coroutines.flow.Flow

class OfflineEquationRepository(private val equationDao: EquationDao): EquationsRepository {
    override fun getAllEquationsStream(): Flow<List<EquationEntity>> = equationDao.getAllEquations()

    override fun getEquationStream(id: Int): Flow<EquationEntity?> = equationDao.getEquation(id)

    override suspend fun insertEquation(equation: EquationEntity) = equationDao.insert(equation)

    override suspend fun deleteEquation(equation: EquationEntity) = equationDao.delete(equation)

    override suspend fun updateEquation(equation: EquationEntity) = equationDao.update(equation)
}