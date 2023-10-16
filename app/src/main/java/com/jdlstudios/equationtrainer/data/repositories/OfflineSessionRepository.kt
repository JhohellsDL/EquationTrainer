package com.jdlstudios.equationtrainer.data.repositories

import com.jdlstudios.equationtrainer.data.local.daos.SessionDao
import com.jdlstudios.equationtrainer.data.local.entities.SessionEntity
import kotlinx.coroutines.flow.Flow

class OfflineSessionRepository(private val sessionDao: SessionDao): SessionsRepository {
    override fun getAllSessionsStream(): Flow<List<SessionEntity>> = sessionDao.getAllSessions()

    override fun getSessionsStream(id: Int): Flow<SessionEntity?> = sessionDao.getSession(id)

    override suspend fun insertSession(session: SessionEntity) = sessionDao.insert(session)

    override suspend fun deleteSession(session: SessionEntity) = sessionDao.delete(session)

    override suspend fun updateSession(session: SessionEntity) = sessionDao.update(session)

    override suspend fun deleteAllSessions() = sessionDao.deleteAllSessions()

}