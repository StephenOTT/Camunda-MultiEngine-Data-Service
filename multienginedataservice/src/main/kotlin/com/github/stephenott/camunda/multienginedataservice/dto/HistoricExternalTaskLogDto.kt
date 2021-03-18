package com.github.stephenott.camunda.multienginedataservice.dto

import org.camunda.bpm.engine.impl.history.event.HistoricExternalTaskLogEntity
import java.util.*

data class HistoricExternalTaskLogDto(
    override val _id: String? = null,
    override val id: String? = null,
    val timestamp: Date? = null,
    val removalTime: Date? = null,

    val externalTaskId: String? = null,
    val topicName: String? = null,
    val workerId: String? = null,
    val priority: Long = 0,
    val retries: Int? = null,
    val errorMessage: String? = null,

    val activityId: String? = null,
    val activityInstanceId: String? = null,
    val executionId: String? = null,

    val processInstanceId: String? = null,
    val processDefinitionId: String? = null,
    val processDefinitionKey: String? = null,
    override val tenantId: String? = null,
    val rootProcessInstanceId: String? = null,

    val creationLog: Boolean = false,
    val failureLog: Boolean = false,
    val successLog: Boolean = false,
    val deletionLog: Boolean = false,

    override val sequenceCounter: Long? = null,

    override val metadata: EngineMetadata? = null
): HistoricData, SequenceCounted {

    override val EVENT_TYPE: String = "historicExternalTaskLog"

    constructor(historicExternalTaskLog: HistoricExternalTaskLogEntity, metadata: EngineMetadata? = null, dbId: String? = null) : this(
        id = historicExternalTaskLog.id,
        timestamp = historicExternalTaskLog.timestamp,
        removalTime = historicExternalTaskLog.removalTime,
        externalTaskId = historicExternalTaskLog.externalTaskId,
        topicName = historicExternalTaskLog.topicName,
        workerId = historicExternalTaskLog.workerId,
        priority = historicExternalTaskLog.priority,
        retries = historicExternalTaskLog.retries,
        errorMessage = historicExternalTaskLog.errorMessage,
        activityId = historicExternalTaskLog.activityId,
        activityInstanceId = historicExternalTaskLog.activityInstanceId,
        executionId = historicExternalTaskLog.executionId,
        processInstanceId = historicExternalTaskLog.processInstanceId,
        processDefinitionId = historicExternalTaskLog.processDefinitionId,
        processDefinitionKey = historicExternalTaskLog.processDefinitionKey,
        tenantId = historicExternalTaskLog.tenantId,
        rootProcessInstanceId = historicExternalTaskLog.rootProcessInstanceId,
        creationLog = historicExternalTaskLog.isCreationLog,
        failureLog = historicExternalTaskLog.isFailureLog,
        successLog = historicExternalTaskLog.isSuccessLog,
        deletionLog = historicExternalTaskLog.isDeletionLog,
        sequenceCounter = historicExternalTaskLog.sequenceCounter,
        metadata = metadata,
        _id = dbId
    )

}

