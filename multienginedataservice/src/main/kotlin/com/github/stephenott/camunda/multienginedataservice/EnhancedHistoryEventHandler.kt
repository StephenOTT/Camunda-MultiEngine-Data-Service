package com.github.stephenott.camunda.multienginedataservice

import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.generateIdFromContext
import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.getHistoryLevelFromContext
import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.processEngineFromContext
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.impl.history.HistoryLevel
import org.camunda.bpm.engine.impl.history.event.*
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler
import org.camunda.bpm.engine.impl.persistence.entity.HistoricJobLogEventEntity
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity

interface EnhancedHistoryEventHandler : HistoryEventHandler {

    fun generateIdIfNull(): Boolean {
        return true
    }

    fun generateAndSetIdWhenNull(event: HistoryEvent) {
        if (generateIdIfNull() && event.id == null) {
            event.id = generateIdFromContext()
        }
    }

    fun shouldWriteHistoricDetail(historyEvent: HistoricDetailEventEntity): Boolean {
        return when (historyEvent) {
            is HistoricVariableUpdateEventEntity -> {
                (processEngineFromContext().processEngineConfiguration as ProcessEngineConfigurationImpl).historyLevel.isHistoryEventProduced(
                    HistoryEventTypes.VARIABLE_INSTANCE_UPDATE_DETAIL,
                    historyEvent
                )
                        && !historyEvent.isEventOfType(HistoryEventTypes.VARIABLE_INSTANCE_MIGRATE)
            }
            is HistoricFormPropertyEventEntity -> {
                true
            }
            else -> {
                throw IllegalArgumentException("Unexpected HistoricDetail for evaluation of shouldWriteHistoricDetail: ${historyEvent::class.qualifiedName}. Event: $historyEvent")
            }
        }
    }

    override fun handleEvent(historyEvent: HistoryEvent) {
        //@TODO review the if statement.  Should not be required.
        // currently required because of the CreateHistoryCleanupJobs operation log that is generated during engine startup because a Context is created.
//        if (org.camunda.bpm.engine.impl.context.Context.getCommandContext() != null){
            generateAndSetIdWhenNull(historyEvent)

            when (historyEvent) {
                is HistoricActivityInstanceEventEntity -> processHistoricActivityInstanceEventEntity(
                    historyEvent, isInitialEvent(historyEvent)
                )
                is HistoricCaseActivityInstanceEventEntity -> processHistoricCaseActivityInstanceEventEntity(
                    historyEvent, isInitialEvent(historyEvent)
                )
                is HistoricCaseInstanceEventEntity -> processHistoricCaseInstanceEventEntity(
                    historyEvent, isInitialEvent(historyEvent)
                )
                is HistoricDecisionEvaluationEvent -> processHistoricDecisionEvaluationEvent(
                    historyEvent
                )
                is HistoricDetailEventEntity -> processHistoricDetailEventEntity(
                    historyEvent, isInitialEvent(historyEvent), shouldWriteHistoricDetail(historyEvent)
                )
                is HistoricExternalTaskLogEntity -> processHistoricExternalTaskLogEntity(
                    historyEvent, isInitialEvent(historyEvent)
                )
                is HistoricIdentityLinkLogEventEntity -> processHistoricIdentityLinkLogEventEntity(
                    historyEvent, isInitialEvent(historyEvent)
                )
                is HistoricIncidentEventEntity -> processHistoricIncidentEventEntity(
                    historyEvent, isInitialEvent(historyEvent)
                )
                is HistoricJobLogEventEntity -> processHistoricJobLogEventEntity(
                    historyEvent, isInitialEvent(historyEvent)
                )
                is HistoricProcessInstanceEventEntity -> processHistoricProcessInstanceEventEntity(
                    historyEvent, isInitialEvent(historyEvent)
                )
                is HistoricTaskInstanceEventEntity -> processHistoricTaskInstanceEventEntity(
                    historyEvent, isInitialEvent(historyEvent)
                )
                is UserOperationLogEntryEventEntity -> processUserOperationLogEntryEventEntity(
                    historyEvent, isInitialEvent(historyEvent)
                )
                else -> processUnexpectedEventEntity(historyEvent)
            }
//        }

    }

    override fun handleEvents(historyEvents: MutableList<HistoryEvent>) {
        historyEvents.forEach { handleEvent(it) }
    }

    fun isInitialEvent(historyEvent: HistoryEvent): Boolean {
        return (historyEvent.eventType == null
                || historyEvent.isEventOfType(HistoryEventTypes.ACTIVITY_INSTANCE_START)
                || historyEvent.isEventOfType(HistoryEventTypes.PROCESS_INSTANCE_START)
                || historyEvent.isEventOfType(HistoryEventTypes.TASK_INSTANCE_CREATE)
                || historyEvent.isEventOfType(HistoryEventTypes.FORM_PROPERTY_UPDATE)
                || historyEvent.isEventOfType(HistoryEventTypes.INCIDENT_CREATE)
                || historyEvent.isEventOfType(HistoryEventTypes.CASE_INSTANCE_CREATE)
                || historyEvent.isEventOfType(HistoryEventTypes.DMN_DECISION_EVALUATE)
                || historyEvent.isEventOfType(HistoryEventTypes.BATCH_START)
                || historyEvent.isEventOfType(HistoryEventTypes.IDENTITY_LINK_ADD)
                || historyEvent.isEventOfType(HistoryEventTypes.IDENTITY_LINK_DELETE))
    }


    fun processHistoricDecisionEvaluationEvent(event: HistoricDecisionEvaluationEvent) {
        // @TODO the `true` change to check if History is enabled
        if (getHistoryLevelFromContext() != HistoryLevel.HISTORY_LEVEL_NONE) {
            val rootHistoricDecisionInstance = event.rootHistoricDecisionInstance

            generateAndSetIdWhenNull(rootHistoricDecisionInstance)

            processHistoricDecisionInstanceEntity(rootHistoricDecisionInstance, isInitialEvent(event))

            for (requiredHistoricDecisionInstance in event.requiredHistoricDecisionInstances) {
                generateAndSetIdWhenNull(requiredHistoricDecisionInstance)

                requiredHistoricDecisionInstance.rootDecisionInstanceId = rootHistoricDecisionInstance.id

                processHistoricDecisionInstanceEntity(requiredHistoricDecisionInstance, isInitialEvent(event))
            }
        }
    }

    fun processHistoricActivityInstanceEventEntity(event: HistoricActivityInstanceEventEntity, isInitial: Boolean)

    fun processHistoricCaseActivityInstanceEventEntity(
        event: HistoricCaseActivityInstanceEventEntity,
        isInitial: Boolean
    )

    fun processHistoricCaseInstanceEventEntity(event: HistoricCaseInstanceEventEntity, isInitial: Boolean)

    fun processHistoricDecisionInstanceEntity(event: HistoricDecisionInstanceEntity, isInitial: Boolean)

    /**
     * May be one of two types:
     *
     * `HistoricDetailVariableInstanceUpdateEntity` or `HistoricFormPropertyEventEntity`
     *
     */
    fun processHistoricDetailEventEntity(
        event: HistoricDetailEventEntity,
        isInitial: Boolean,
        shouldWriteHistoricDetail: Boolean
    ) {

        when (event) {
            is HistoricVariableUpdateEventEntity -> processHistoricDetailVariableInstanceUpdateEntity(
                event,
                isInitial,
                shouldWriteHistoricDetail
            )
            is HistoricFormPropertyEventEntity -> processHistoricDetailFormPropertyEventEntity(
                event,
                isInitial,
                shouldWriteHistoricDetail
            )
        }
    }

    fun convertVariableUpdateToVariableInstance(event: HistoricVariableUpdateEventEntity): HistoricVariableInstanceEntity{
        return HistoricVariableInstanceEntity(event)
    }

    fun processHistoricDetailVariableInstanceUpdateEntity(
        historyEvent: HistoricVariableUpdateEventEntity, isInitial: Boolean,
        shouldWriteHistoricDetail: Boolean
    ) {
        when {
            historyEvent.isEventOfType(HistoryEventTypes.VARIABLE_INSTANCE_CREATE) -> {
                processHistoricProcessVariableInstanceCreate(historyEvent, shouldWriteHistoricDetail)
            }
            historyEvent.isEventOfType(HistoryEventTypes.VARIABLE_INSTANCE_UPDATE) -> {
                processHistoricProcessVariableInstanceUpdate(historyEvent, shouldWriteHistoricDetail)
            }
            historyEvent.isEventOfType(HistoryEventTypes.VARIABLE_INSTANCE_MIGRATE) -> {
                processHistoricProcessVariableInstanceMigrate(historyEvent, shouldWriteHistoricDetail)
            }
            historyEvent.isEventOfType(HistoryEventTypes.VARIABLE_INSTANCE_DELETE) -> {
                processHistoricProcessVariableInstanceDelete(historyEvent, shouldWriteHistoricDetail)
            }
        }
    }

    fun processHistoricDetailFormPropertyEventEntity(
        event: HistoricFormPropertyEventEntity, isInitial: Boolean,
        shouldWriteHistoricDetail: Boolean
    )

    fun processHistoricExternalTaskLogEntity(event: HistoricExternalTaskLogEntity, isInitial: Boolean)

    fun processHistoricIdentityLinkLogEventEntity(event: HistoricIdentityLinkLogEventEntity, isInitial: Boolean)

    fun processHistoricIncidentEventEntity(event: HistoricIncidentEventEntity, isInitial: Boolean)

    fun processHistoricJobLogEventEntity(event: HistoricJobLogEventEntity, isInitial: Boolean)

    fun processHistoricProcessInstanceEventEntity(event: HistoricProcessInstanceEventEntity, isInitial: Boolean)

    fun processHistoricTaskInstanceEventEntity(event: HistoricTaskInstanceEventEntity, isInitial: Boolean)

    fun processUserOperationLogEntryEventEntity(event: UserOperationLogEntryEventEntity, isInitial: Boolean)

    fun processUnexpectedEventEntity(event: HistoryEvent) {
        throw IllegalStateException("Unexpected History Event: ${event::class.qualifiedName}. Event: $event")
    }

    fun processHistoricProcessVariableInstanceCreate(event: HistoricVariableUpdateEventEntity, shouldWriteHistoricDetail: Boolean)

    fun processHistoricProcessVariableInstanceUpdate(event: HistoricVariableUpdateEventEntity, shouldWriteHistoricDetail: Boolean)

    fun processHistoricProcessVariableInstanceMigrate(event: HistoricVariableUpdateEventEntity, shouldWriteHistoricDetail: Boolean) {
        processHistoricProcessVariableInstanceUpdate(event, shouldWriteHistoricDetail)
    }

    fun processHistoricProcessVariableInstanceDelete(event: HistoricVariableUpdateEventEntity, shouldWriteHistoricDetail: Boolean)

}