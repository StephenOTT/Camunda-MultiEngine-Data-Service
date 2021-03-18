package com.github.stephenott.camunda.multienginedataservice

import com.github.stephenott.camunda.multienginedataservice.dto.EngineMetadata
import com.github.stephenott.camunda.multienginedataservice.session.MongoTransactionSession
import com.mongodb.client.ClientSession
import org.bson.types.ObjectId
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.impl.context.Context
import org.camunda.bpm.engine.impl.history.HistoryLevel
import org.camunda.bpm.engine.impl.interceptor.CommandContext
import java.time.Instant

object MongoHistoryEventHandlerUtils {

    fun processEngineFromContext(commandContext: CommandContext = Context.getCommandContext()): ProcessEngine {
        return commandContext.processEngineConfiguration.processEngine
    }

    fun generateIdFromContext(commandContext: CommandContext = Context.getCommandContext()): String {
        return (commandContext.processEngineConfiguration as ProcessEngineConfigurationImpl).idGenerator.nextId
    }

    /**
     * Defaults:
     *  HISTORY_LEVEL_NONE
     *  HISTORY_LEVEL_ACTIVITY
     *  HISTORY_LEVEL_AUDIT
     *  HISTORY_LEVEL_FULL
     *
     *  Audit is the default camunda history level.
     */
    fun getHistoryLevelFromContext(commandContext: CommandContext = Context.getCommandContext()): HistoryLevel {
        return (processEngineFromContext(commandContext).processEngineConfiguration as ProcessEngineConfigurationImpl).historyLevel
    }

    fun getMongoSessionTransactionIdFromContext(commandContext: CommandContext = Context.getCommandContext()): String {
        return commandContext.getSession(MongoTransactionSession::class.java).transactionId
    }

//    fun getProcessEngineName(): String {
//        return processEngine().name
//    }
//
//    fun getProcessEngineHostName(): String {
//        return (processEngine().processEngineConfiguration as ProcessEngineConfigurationImpl).hostname
//    }

    fun getMongoSessionFromContext(commandContext: CommandContext = Context.getCommandContext()): ClientSession {
        return commandContext.getSession(MongoTransactionSession::class.java).session
    }

    fun generateMongoId(): String{
        return ObjectId().toString()
    }

    fun generateMetadata(
        //@TODO *** Review: The processEngine is nullable because on engine startup for first time there are job history jobs created when user op log is set to false for requiring auth to generate the log.  As a result the processEngine is not available when these user op log entries are trying to be created.  Furhter review is required.
        processEngine: ProcessEngine? = Context.getProcessEngineConfiguration().processEngine,
        createdAt: Instant = Instant.now(),
        engineName: String = if (processEngine == null) Context.getProcessEngineConfiguration().processEngineName else processEngine.name,
        hostname: String = if (processEngine == null) Context.getProcessEngineConfiguration().hostname else (processEngine.processEngineConfiguration as ProcessEngineConfigurationImpl).hostname,
        clusterName: String? = MultiEngineDataServiceProcessEnginePlugin.clusterName,
        transactionId: String = getMongoSessionTransactionIdFromContext()
    ): EngineMetadata {
        return EngineMetadata(
            engineName = engineName,
            hostname = hostname,
            clusterName = clusterName,
            transactionId = transactionId,
            createdAt = createdAt
        )
    }
}