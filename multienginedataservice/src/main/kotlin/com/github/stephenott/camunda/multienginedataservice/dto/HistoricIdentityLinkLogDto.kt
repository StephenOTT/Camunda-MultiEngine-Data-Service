package com.github.stephenott.camunda.multienginedataservice.dto

import org.camunda.bpm.engine.impl.history.event.HistoricIdentityLinkLogEventEntity
import java.util.*


data class HistoricIdentityLinkLogDto(
    override val _id: String? = null,
    override val id: String? = null,
    val time: Date? = null,
    val type: String? = null,
    val userId: String? = null,
    val groupId: String? = null,
    val taskId: String? = null,
    val processDefinitionId: String? = null,
    val processDefinitionKey: String? = null,
    val operationType: String? = null,
    val assignerId: String? = null,
    override val tenantId: String? = null,
    val removalTime: Date? = null,
    val rootProcessInstanceId: String? = null,
    val processInstanceId: String? = null, //@TODO it seems like this is always null and the rootProcessInstanceID field is always used instead.  Need to investigate

    override val sequenceCounter: Long? = null,

    override val metadata: EngineMetadata? = null
): HistoricData, SequenceCounted {

    override val EVENT_TYPE: String = "historicIdentityLinkLog"

    constructor(historicIdentityLinkLogEventEntity: HistoricIdentityLinkLogEventEntity, metadata: EngineMetadata? = null, dbId: String? = null): this(
        id = historicIdentityLinkLogEventEntity.id,
        assignerId = historicIdentityLinkLogEventEntity.assignerId,
        groupId = historicIdentityLinkLogEventEntity.groupId,
        operationType = historicIdentityLinkLogEventEntity.operationType,
        taskId = historicIdentityLinkLogEventEntity.taskId,
        time = historicIdentityLinkLogEventEntity.time,
        type = historicIdentityLinkLogEventEntity.type,
        processDefinitionId = historicIdentityLinkLogEventEntity.processDefinitionId,
        processDefinitionKey = historicIdentityLinkLogEventEntity.processDefinitionKey,
        userId = historicIdentityLinkLogEventEntity.userId,
        tenantId = historicIdentityLinkLogEventEntity.tenantId,
        removalTime = historicIdentityLinkLogEventEntity.removalTime,
        rootProcessInstanceId = historicIdentityLinkLogEventEntity.rootProcessInstanceId,
        processInstanceId = historicIdentityLinkLogEventEntity.processInstanceId,

        sequenceCounter = historicIdentityLinkLogEventEntity.sequenceCounter,
        metadata = metadata,
        _id = dbId
    )
}


