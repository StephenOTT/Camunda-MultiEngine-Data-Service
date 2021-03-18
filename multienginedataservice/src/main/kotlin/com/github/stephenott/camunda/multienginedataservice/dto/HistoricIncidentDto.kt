package com.github.stephenott.camunda.multienginedataservice.dto

import org.camunda.bpm.engine.impl.history.event.HistoricIncidentEventEntity
import java.util.*

data class HistoricIncidentDto(
    override val _id: String? = null,
    override val id: String? = null,
    val processDefinitionKey: String? = null,
    val processDefinitionId: String? = null,
    val processInstanceId: String? = null,
    val executionId: String? = null,
    val rootProcessInstanceId: String? = null,
    val createTime: Date? = null,
    val endTime: Date? = null,
    val removalTime: Date? = null,
    val incidentType: String? = null,
    val activityId: String? = null,
    val failedActivityId: String? = null,
    val causeIncidentId: String? = null,
    val rootCauseIncidentId: String? = null,
    val configuration: String? = null,
    val historyConfiguration: String? = null,
    val incidentMessage: String? = null,
    override val tenantId: String? = null,
    val jobDefinitionId: String? = null,
    val open: Boolean? = null,
    val deleted: Boolean? = null,
    val resolved: Boolean? = null,

    override val sequenceCounter: Long? = null,

    override val metadata: EngineMetadata? = null
): HistoricData, SequenceCounted {

    override val EVENT_TYPE: String = "historicIncident"

    constructor(historicIncidentEventEntity: HistoricIncidentEventEntity, metadata: EngineMetadata? = null, dbId: String? = null) : this(
        id = historicIncidentEventEntity.id,
        processDefinitionKey = historicIncidentEventEntity.processDefinitionKey,
        processDefinitionId = historicIncidentEventEntity.processDefinitionId,
        processInstanceId = historicIncidentEventEntity.processInstanceId,
        executionId = historicIncidentEventEntity.executionId,
        createTime = historicIncidentEventEntity.createTime,
        endTime = historicIncidentEventEntity.endTime,
        incidentType = historicIncidentEventEntity.incidentType,
        failedActivityId = historicIncidentEventEntity.failedActivityId,
        activityId = historicIncidentEventEntity.activityId,
        causeIncidentId = historicIncidentEventEntity.causeIncidentId,
        rootCauseIncidentId = historicIncidentEventEntity.rootCauseIncidentId,
        configuration = historicIncidentEventEntity.configuration,
        historyConfiguration = historicIncidentEventEntity.historyConfiguration,
        incidentMessage = historicIncidentEventEntity.incidentMessage,
        open = historicIncidentEventEntity.isOpen,
        deleted = historicIncidentEventEntity.isDeleted,
        resolved = historicIncidentEventEntity.isResolved,
        tenantId = historicIncidentEventEntity.tenantId,
        jobDefinitionId = historicIncidentEventEntity.jobDefinitionId,
        removalTime = historicIncidentEventEntity.removalTime,
        rootProcessInstanceId = historicIncidentEventEntity.rootProcessInstanceId,
        sequenceCounter = historicIncidentEventEntity.sequenceCounter,
        metadata = metadata,
        _id = dbId
    )

}

