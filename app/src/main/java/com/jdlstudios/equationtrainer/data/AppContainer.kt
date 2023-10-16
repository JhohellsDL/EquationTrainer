package com.jdlstudios.equationtrainer.data

import android.content.Context
import com.jdlstudios.equationtrainer.data.local.AppDataBase
import com.jdlstudios.equationtrainer.data.repositories.EquationsRepository
import com.jdlstudios.equationtrainer.data.repositories.OfflineEquationRepository
import com.jdlstudios.equationtrainer.data.repositories.OfflineSessionRepository
import com.jdlstudios.equationtrainer.data.repositories.SessionsRepository

interface AppContainer {
    val equationsRepository: EquationsRepository
    val sessionsRepository: SessionsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val equationsRepository: EquationsRepository by lazy {
        OfflineEquationRepository(AppDataBase.getDatabase(context).equationDao())
    }

    override val sessionsRepository: SessionsRepository by lazy {
        OfflineSessionRepository(AppDataBase.getDatabase(context).sessionDao())
    }
}