package com.github.stephenott.camunda.multienginedataservice

import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.generateMetadata
import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.generateMongoId
import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.getMongoSessionFromContext
import com.github.stephenott.camunda.multienginedataservice.dto.*
import com.mongodb.client.MongoCollection
import org.camunda.bpm.engine.impl.history.event.*
import org.camunda.bpm.engine.impl.persistence.entity.HistoricJobLogEventEntity
import org.litote.kmongo.*

class MongoEnhancedHistoryEventHandler(
    private val collections: HistoryEventMongoCollections,
    private val config: HistoricEventHandlerConfig
) : EnhancedHistoryEventHandler {

    override fun processHistoricActivityInstanceEventEntity(
        event: HistoricActivityInstanceEventEntity,
        isInitial: Boolean
    ) {
        genericInsertUpdate(event, isInitial)
    }

    override fun processHistoricCaseActivityInstanceEventEntity(
        event: HistoricCaseActivityInstanceEventEntity,
        isInitial: Boolean
    ) {
        genericInsertUpdate(event, isInitial)
    }

    override fun processHistoricCaseInstanceEventEntity(event: HistoricCaseInstanceEventEntity, isInitial: Boolean) {
        genericInsertUpdate(event, isInitial)
    }

    override fun processHistoricDecisionInstanceEntity(event: HistoricDecisionInstanceEntity, isInitial: Boolean) {
        collections.historicDecisionInstanceColl.insertOne(
            getMongoSessionFromContext(),
            event.toHistoricData(generateMetadata(), generateMongoId())
        )
    }

    override fun processHistoricDetailFormPropertyEventEntity(
        event: HistoricFormPropertyEventEntity,
        isInitial: Boolean,
        shouldWriteHistoricDetail: Boolean
    ) {
        if (shouldWriteHistoricDetail) {
            genericInsertUpdate(event, isInitial)
        }
    }

    override fun processHistoricExternalTaskLogEntity(event: HistoricExternalTaskLogEntity, isInitial: Boolean) {
        genericInsertUpdate(event, isInitial)
    }

    override fun processHistoricIdentityLinkLogEventEntity(
        event: HistoricIdentityLinkLogEventEntity,
        isInitial: Boolean
    ) {
        genericInsertUpdate(event, isInitial)

        //@TODO review!
        if (config.generateIdentityLinkInstances) {
            if (event.operationType == "add") {
                collections.historicIdentityLinkInstanceColl.insertOne(
                    getMongoSessionFromContext(),
                    HistoricIdentityLinkInstanceDto(event, generateMetadata(), generateMongoId())
                )

            } else if (event.operationType == "delete") {
                //@TODO Review because there is no ID for a identity link
                val result = collections.historicIdentityLinkInstanceColl.updateOne(
                    getMongoSessionFromContext(),
                    and(
                        //@TODO Review usage
                        HistoricData::metadata / EngineMetadata::engineName eq MongoHistoryEventHandlerUtils.processEngineFromContext().name,
                        HistoricData::metadata / EngineMetadata::clusterName eq config.clusterName,
                        //@TODO review (because there is no "ID" field for each identity link based on the logs... https://jira.camunda.com/browse/CAM-13028
                        HistoricIdentityLinkInstanceDto::taskId eq event.taskId,
                        HistoricIdentityLinkInstanceDto::rootProcessInstanceId eq event.rootProcessInstanceId,
                        HistoricIdentityLinkInstanceDto::processInstanceId eq event.processInstanceId,
                        HistoricIdentityLinkInstanceDto::processDefinitionId eq event.processDefinitionId,
                        HistoricIdentityLinkInstanceDto::state eq "CREATED",
                        HistoricIdentityLinkInstanceDto::userId eq event.userId,
                        HistoricIdentityLinkInstanceDto::groupId eq event.groupId,
                        HistoricIdentityLinkInstanceDto::tenantId eq event.tenantId,
                        HistoricIdentityLinkInstanceDto::type eq event.type
                    ),
                    listOf(
                        setValue(
                            HistoricIdentityLinkInstanceDto::state,
                            "DELETED"
                        )
                    )
                )
                require(result.matchedCount == 1L && result.modifiedCount == 1L) {
                    "Unable to find Identity link to perform deletion in identity link instances."
                }
            }
        }
    }

    override fun processHistoricIncidentEventEntity(event: HistoricIncidentEventEntity, isInitial: Boolean) {
        genericInsertUpdate(event, isInitial)
    }

    override fun processHistoricJobLogEventEntity(event: HistoricJobLogEventEntity, isInitial: Boolean) {
        genericInsertUpdate(event, isInitial)
    }

    override fun processHistoricProcessInstanceEventEntity(
        event: HistoricProcessInstanceEventEntity,
        isInitial: Boolean
    ) {
        genericInsertUpdate(event, isInitial)
    }

    override fun processHistoricTaskInstanceEventEntity(event: HistoricTaskInstanceEventEntity, isInitial: Boolean) {
        genericInsertUpdate(event, isInitial)
    }

    override fun processUserOperationLogEntryEventEntity(event: UserOperationLogEntryEventEntity, isInitial: Boolean) {
        genericInsertUpdate(event, isInitial)
    }

    override fun processHistoricProcessVariableInstanceCreate(
        event: HistoricVariableUpdateEventEntity,
        shouldWriteHistoricDetail: Boolean
    ) {
        if (shouldWriteHistoricDetail) {
            genericInsertUpdate(event, true)
        }
        collections.historicVariableInstanceColl.insertOne(
            getMongoSessionFromContext(),
            convertVariableUpdateToVariableInstance(event).toHistoricData(generateMetadata(), generateMongoId())
        )
    }

    override fun processHistoricProcessVariableInstanceUpdate(
        event: HistoricVariableUpdateEventEntity,
        shouldWriteHistoricDetail: Boolean
    ) {
        if (shouldWriteHistoricDetail) {
            genericInsertUpdate(event, true)
        }
        collections.historicVariableInstanceColl.replaceOneWithFilter(
            getMongoSessionFromContext(),
            and(
                HistoricData::metadata / EngineMetadata::engineName eq MongoHistoryEventHandlerUtils.processEngineFromContext().name,
                HistoricData::metadata / EngineMetadata::clusterName eq config.clusterName,
                HistoricData::id eq event.variableInstanceId
            ),
            convertVariableUpdateToVariableInstance(event).toHistoricData(generateMetadata())
        )
    }

    override fun processHistoricProcessVariableInstanceDelete(
        event: HistoricVariableUpdateEventEntity,
        shouldWriteHistoricDetail: Boolean
    ) {
        if (shouldWriteHistoricDetail) {
            genericInsertUpdate(event, true)
        }
        val result = collections.historicVariableInstanceColl.updateOne(
            getMongoSessionFromContext(),
            and(
                HistoricData::metadata / EngineMetadata::engineName eq MongoHistoryEventHandlerUtils.processEngineFromContext().name,
                HistoricData::metadata / EngineMetadata::clusterName eq config.clusterName,
                HistoricData::id eq event.variableInstanceId
            ),
            listOf(
                setValue(
                    HistoricVariableInstanceDto::state,
                    "DELETED"
                )
            )
        )
        require(result.matchedCount == 1L && result.modifiedCount == 1L) {
            "Unable to update variable instance with deletion"
        }
    }

    private fun getMongoCollection(historyEvent: HistoryEvent): MongoCollection<HistoricData> {
        return when (historyEvent) {
            is HistoricActivityInstanceEventEntity -> collections.historicActivityInstanceColl
            is HistoricCaseActivityInstanceEventEntity -> collections.historicCaseActivityInstanceColl
            is HistoricCaseInstanceEventEntity -> collections.historicCaseInstanceColl
            is HistoricDecisionInstanceEntity -> collections.historicDecisionInstanceColl
            is HistoricDetailEventEntity -> collections.historicDetailColl
            is HistoricExternalTaskLogEntity -> collections.historicExternalTaskLogColl
            is HistoricIdentityLinkLogEventEntity -> collections.historicIdentityLinkLogColl
            is HistoricIncidentEventEntity -> collections.historicIncidentColl
            is HistoricJobLogEventEntity -> collections.historicJobLogColl
            is HistoricProcessInstanceEventEntity -> collections.historicProcessInstanceColl
            is HistoricTaskInstanceEventEntity -> collections.historicTaskInstanceColl
            is UserOperationLogEntryEventEntity -> collections.historicUserOperationLogColl
            else -> throw IllegalArgumentException("Unexpected HistoryEvent Type ${historyEvent::class.qualifiedName}")
        }
    }

    private fun genericInsertUpdate(historyEvent: HistoryEvent, isInitial: Boolean) {

        if (isInitial) {
            // INSERT
            getMongoCollection(historyEvent).insertOne(
                getMongoSessionFromContext(),
                historyEvent.toHistoricData(generateMetadata(), generateMongoId())
            )

        } else {
            // UPDATE
            val coll = getMongoCollection(historyEvent)
            val existingEvent: HistoricData? = coll.findOne(
                getMongoSessionFromContext(),
                and(
                    HistoricData::metadata / EngineMetadata::engineName eq MongoHistoryEventHandlerUtils.processEngineFromContext().name,
                    HistoricData::metadata / EngineMetadata::clusterName eq config.clusterName,
                    HistoricData::id eq historyEvent.id
                )
            )

            checkNotNull(existingEvent) {
                "Unable to find expected existing history event for $historyEvent"
            }

            if (historyEvent is HistoricScopeInstanceEvent) {
                historyEvent.startTime = (existingEvent as DurationTracked).startTime
            }

            //@TODO Review:
            val result = getMongoCollection(historyEvent).replaceOneWithFilter(
                getMongoSessionFromContext(),
                HistoricData::_id eq existingEvent._id,
                historyEvent.toHistoricData(generateMetadata())
            )

            require(result.matchedCount == 1L && result.modifiedCount == 1L) {
                "Unable to update event"
            }
        }
    }
}