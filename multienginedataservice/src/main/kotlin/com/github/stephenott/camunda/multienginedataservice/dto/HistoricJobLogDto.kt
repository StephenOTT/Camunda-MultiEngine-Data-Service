package com.github.stephenott.camunda.multienginedataservice.dto

import org.camunda.bpm.engine.impl.persistence.entity.HistoricJobLogEventEntity
import java.util.*

data class HistoricJobLogDto(
    override val _id: String? = null,
    override val id: String? = null,
    val timestamp: Date? = null,
    val removalTime: Date? = null,

    val jobId: String? = null,
    val jobDueDate: Date? = null,
    val jobRetries: Int = 0,
    val jobPriority: Long = 0,
    val jobExceptionMessage: String? = null,

    val jobDefinitionId: String? = null,
    val jobDefinitionType: String? = null,
    val jobDefinitionConfiguration: String? = null,

    val activityId: String? = null,
    val failedActivityId: String? = null,
    val executionId: String? = null,
    val processInstanceId: String? = null,
    val processDefinitionId: String? = null,
    val processDefinitionKey: String? = null,
    val deploymentId: String? = null,
    override val tenantId: String? = null,
    val hostname: String? = null,
    val rootProcessInstanceId: String? = null,

    val creationLog: Boolean = false,
    val failureLog: Boolean = false,
    val successLog: Boolean = false,
    val deletionLog: Boolean = false,

    override val sequenceCounter: Long? = null,

    override val metadata: EngineMetadata? = null


): HistoricData, SequenceCounted {

    override val EVENT_TYPE: String = "historicJobLog"

    constructor(historicJobLog: HistoricJobLogEventEntity, metadata: EngineMetadata? = null, dbId: String? = null) : this(
        id = historicJobLog.id,
        timestamp = historicJobLog.timestamp,
        removalTime = historicJobLog.removalTime,
        jobId = historicJobLog.jobId,
        jobDueDate = historicJobLog.jobDueDate,
        jobRetries = historicJobLog.jobRetries,
        jobPriority = historicJobLog.jobPriority,
        jobExceptionMessage = historicJobLog.jobExceptionMessage,
        jobDefinitionId = historicJobLog.jobDefinitionId,
        jobDefinitionType = historicJobLog.jobDefinitionType,
        jobDefinitionConfiguration = historicJobLog.jobDefinitionConfiguration,
        activityId = historicJobLog.activityId,
        failedActivityId = historicJobLog.failedActivityId,
        executionId = historicJobLog.executionId,
        processInstanceId = historicJobLog.processInstanceId,
        processDefinitionId = historicJobLog.processDefinitionId,
        processDefinitionKey = historicJobLog.processDefinitionKey,
        deploymentId = historicJobLog.deploymentId,
        tenantId = historicJobLog.tenantId,
        hostname = historicJobLog.hostname,
        rootProcessInstanceId = historicJobLog.rootProcessInstanceId,
        creationLog = historicJobLog.isCreationLog,
        failureLog = historicJobLog.isFailureLog,
        successLog = historicJobLog.isSuccessLog,
        deletionLog = historicJobLog.isDeletionLog,
        sequenceCounter = historicJobLog.sequenceCounter,
        metadata = metadata,
        _id = dbId
    )
}

