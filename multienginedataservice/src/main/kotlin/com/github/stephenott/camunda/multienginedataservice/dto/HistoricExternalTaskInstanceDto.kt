package com.github.stephenott.camunda.multienginedataservice.dto

import org.camunda.bpm.engine.externaltask.ExternalTask
import java.util.*

data class HistoricExternalTaskInstanceDto(
    override val _id: String? = null,
    override val id: String? = null,
    var activityId: String? = null,
    val activityInstanceId: String? = null,
    val errorMessage: String? = null,
    val executionId: String? = null,
    val lockExpirationTime: Date? = null,
    val processDefinitionId: String? = null,
    val processDefinitionKey: String? = null,
    val processDefinitionVersionTag: String? = null,
    val processInstanceId: String? = null,
    val retries: Int? = null,
    val suspended: Boolean? = null,
    val workerId: String? = null,
    val topicName: String? = null,
    override val tenantId: String? = null,
    val priority: Long? = null,
    val businessKey: String? = null,

    override val metadata: EngineMetadata? = null
): HistoricData{
    override val EVENT_TYPE: String = "historicExternalTaskInstance"

    constructor(externalTask: ExternalTask, metadata: EngineMetadata? = null, dbId: String? = null): this(
        id = externalTask.id,
        activityId = externalTask.activityId,
        activityInstanceId = externalTask.activityInstanceId,
        errorMessage = externalTask.errorMessage,
        executionId = externalTask.executionId,
        lockExpirationTime = externalTask.lockExpirationTime,
        processDefinitionId = externalTask.processDefinitionId,
        processDefinitionKey = externalTask.processDefinitionKey,
        processDefinitionVersionTag = externalTask.processDefinitionVersionTag,
        processInstanceId = externalTask.processInstanceId,
        retries = externalTask.retries,
        suspended = externalTask.isSuspended,
        workerId = externalTask.workerId,
        topicName = externalTask.topicName,
        tenantId = externalTask.tenantId,
        priority = externalTask.priority,
        businessKey = externalTask.businessKey,
        metadata = metadata,
        _id = dbId
    )
}
