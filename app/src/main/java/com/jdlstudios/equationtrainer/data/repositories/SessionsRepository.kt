package com.jdlstudios.equationtrainer.data.repositories

import com.jdlstudios.equationtrainer.data.local.entities.SessionEntity
import kotlinx.coroutines.flow.Flow

interface SessionsRepository {
    fun getAllSessionsStream(): Flow<List<SessionEntity>>

    fun getSessionsStream(id: Int): Flow<SessionEntity?>

    suspend fun insertSession(session: SessionEntity)

    suspend fun deleteSession(session: SessionEntity)

    suspend fun updateSession(session: SessionEntity)

    suspend fun deleteAllSessions()
}