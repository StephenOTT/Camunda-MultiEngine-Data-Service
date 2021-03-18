package com.github.stephenott.camunda.multienginedataservice.dto

import org.camunda.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity
import java.util.*

data class HistoricProcessInstanceDto(
    override val _id: String? = null,
    override val id: String? = null,
    val businessKey: String? = null,
    val processDefinitionId: String? = null,
    val processDefinitionKey: String? = null,
    val processDefinitionName: String? = null,
    val processDefinitionVersion: Int? = null,
    override val startTime: Date? = null,
    override val endTime: Date? = null,
    val removalTime: Date? = null,
    override val durationInMillis: Long? = null,
    val startUserId: String? = null,
    val startActivityId: String? = null,
    val deleteReason: String? = null,
    val rootProcessInstanceId: String? = null,
    val superProcessInstanceId: String? = null,
    val superCaseInstanceId: String? = null,
    val caseInstanceId: String? = null,
    override val tenantId: String? = null,
    val state: String? = null,
    override val sequenceCounter: Long? = null,

    override val relations: Relations? = null,

    override val metadata: EngineMetadata? = null

) : HistoricData, DurationTracked, SequenceCounted, Relatable {

    override val EVENT_TYPE: String = "historicProcessInstance"

    constructor(instance: HistoricProcessInstanceEventEntity, metadata: EngineMetadata? = null, dbId: String? = null) : this(
        id = instance.id,
        businessKey = instance.businessKey,
        processDefinitionId = instance.processDefinitionId,
        processDefinitionKey = instance.processDefinitionKey,
        processDefinitionName = instance.processDefinitionName,
        processDefinitionVersion = instance.processDefinitionVersion,
        startTime = instance.startTime,
        endTime = instance.endTime,
        removalTime = instance.removalTime,
        durationInMillis = instance.durationInMillis,
        startUserId = instance.startUserId,
        startActivityId = instance.startActivityId,
        deleteReason = instance.deleteReason,
        rootProcessInstanceId = instance.rootProcessInstanceId,
        superProcessInstanceId = instance.superProcessInstanceId,
        superCaseInstanceId = instance.superCaseInstanceId,
        caseInstanceId = instance.caseInstanceId,
        tenantId = instance.tenantId,
        state = instance.state,
        sequenceCounter = instance.sequenceCounter,
        metadata = metadata,
        _id = dbId
    )
}


