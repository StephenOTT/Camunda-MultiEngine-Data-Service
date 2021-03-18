package com.github.stephenott.camunda.multienginedataservice

import com.github.stephenott.camunda.multienginedataservice.dto.*
import org.camunda.bpm.engine.impl.db.HistoricEntity
import org.camunda.bpm.engine.impl.history.event.*
import org.camunda.bpm.engine.impl.persistence.entity.HistoricJobLogEventEntity
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity

fun HistoricEntity.toHistoricData(metadata: EngineMetadata? = null, dbId: String? = null): HistoricData {
    return when (this) {
        is HistoricActivityInstanceEventEntity -> HistoricActivityInstanceDto(this, metadata, dbId)
        is HistoricDecisionInstanceEntity -> HistoricDecisionInstanceDto(this, metadata, dbId)
        is HistoricFormPropertyEventEntity -> HistoricDetailFormFieldDto(this, metadata, dbId)
        is HistoricVariableUpdateEventEntity -> HistoricDetailVariableUpdateDto(this, metadata, dbId)
        is HistoricExternalTaskLogEntity -> HistoricExternalTaskLogDto(this, metadata, dbId)
        is HistoricIdentityLinkLogEventEntity -> HistoricIdentityLinkLogDto(this, metadata, dbId)
        is HistoricIncidentEventEntity -> HistoricIncidentDto(this, metadata, dbId)
        is HistoricJobLogEventEntity -> HistoricJobLogDto(this, metadata, dbId)
        is HistoricProcessInstanceEventEntity -> HistoricProcessInstanceDto(this, metadata, dbId)
        is HistoricTaskInstanceEventEntity -> HistoricTaskInstanceDto(this, metadata, dbId)
        is UserOperationLogEntryEventEntity -> HistoricUserOperationLogEntryDto(this, metadata, dbId)
        is HistoricVariableInstanceEntity -> HistoricVariableInstanceDto(this, metadata, dbId)
        else -> throw IllegalArgumentException("Unexpected History Event Type: ${this::class.qualifiedName}")
    }
}