package com.github.stephenott.camunda.multienginedataservice.query

import com.github.stephenott.camunda.multienginedataservice.HistoryEventMongoCollections
import com.github.stephenott.camunda.multienginedataservice.deployer.HistoricProcessDefinitionDto
import com.github.stephenott.camunda.multienginedataservice.dto.*
import com.github.stephenott.camunda.multienginedataservice.security.SecurityFilterFactory
import com.mongodb.client.AggregateIterable
import org.bson.conversions.Bson
import org.litote.kmongo.*


class HistoryService(
    val collections: HistoryEventMongoCollections,
    val securityFilterFactory: SecurityFilterFactory = SecurityFilterFactory()
) {

    fun processInstanceQuery(
        clusterNames: List<String?>? = null,
        engineNames: List<String?>? = null,
        queryConfig: ProcessInstanceQueryConfig = ProcessInstanceQueryConfig()
    ): AggregateIterable<HistoricProcessInstanceDto> {

        val pipeline: MutableList<Bson> = mutableListOf()

        val mainMatch: MutableList<Bson> = mutableListOf()

        clusterNames?.let {
            mainMatch.add(HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName `in` clusterNames)
        }

        engineNames?.let {
            mainMatch.add(HistoricProcessInstanceDto::metadata / EngineMetadata::engineName `in` engineNames)
        }

        queryConfig.isRootInstance?.let {
            if (it) {
                mainMatch.add(
                    expr(
                        "eq".projection from listOf(
                            HistoricProcessInstanceDto::id,
                            HistoricProcessInstanceDto::rootProcessInstanceId
                        )
                    )
                )
            } else {
                mainMatch.add(HistoricProcessInstanceDto::superProcessInstanceId ne null)
            }
        }

        queryConfig.processInstanceFilter?.let {
            mainMatch.add(match(it))
        }

        pipeline.add(match(and(mainMatch)))

        queryConfig.processInstanceProjection?.let {
            pipeline.add(project(it))
        }

        queryConfig.preRelationsSecurityFilter?.let {
            pipeline.add(match(it.matchContent))
        }

        queryConfig.limit?.let {
            pipeline.add(limit(it))
        }

        queryConfig.skip?.let {
            pipeline.add(skip(it))
        }


        if (queryConfig.includeProcessVariables) {

            val lookupPipeline: MutableList<Bson> = mutableListOf()

            lookupPipeline.add(
                match(
                    and(
                        expr(
                            "eq".projection from listOf(
                                (HistoricVariableInstanceDto::metadata / EngineMetadata::clusterName).projection,
                                "\$\$clusterName"
                            )
                        ),
                        expr(
                            "eq".projection from listOf(
                                (HistoricVariableInstanceDto::metadata / EngineMetadata::engineName).projection,
                                "\$\$engineName"
                            )
                        ),
                        expr(
                            "eq".projection from listOf(
                                HistoricVariableInstanceDto::processInstanceId.projection,
                                HistoricProcessInstanceDto::id.variable
                            )
                        ),
                        expr(
                            "eq".projection from listOf(
                                HistoricVariableInstanceDto::isLocalVariable.projection,
                                false
                            )
                        ),
                    )
                )
            )

            //@TODO add query support for add or not inlcude local variables

            queryConfig.processVariableFilter?.let {
                lookupPipeline.add(match(it))
            }

            queryConfig.processVariableSecurityFilter?.let {
                lookupPipeline.add(match(it.matchContent))
            }

            queryConfig.processVariablesProjection?.let {
                lookupPipeline.add(project(it))
            }

            if (queryConfig.includeProcessVariableDetails) {
                val detailsLookupPipeline: MutableList<Bson> = mutableListOf()

                detailsLookupPipeline.add(
                    match(
                        and(
                            expr(
                                "eq".projection from listOf(
                                    (HistoricDetailVariableUpdateDto::metadata / EngineMetadata::clusterName).projection,
                                    "\$\$clusterName"
                                )
                            ),
                            expr(
                                "eq".projection from listOf(
                                    (HistoricDetailVariableUpdateDto::metadata / EngineMetadata::engineName).projection,
                                    "\$\$engineName"
                                )
                            ),
                            expr(
                                "eq".projection from listOf(
                                    HistoricDetailVariableUpdateDto::EVENT_TYPE.projection,
                                    "historicDetailVariableUpdate" //@TODO move these values into static consts so they can be referenced. (this eq is required because there are multiple history typed in the same collection)
                                )
                            ),
                            expr(
                                "eq".projection from listOf(
                                    HistoricDetailVariableUpdateDto::variableInstanceId.projection,
                                    HistoricVariableInstanceDto::id.variable
                                )
                            )
                        )
                    )
                )

                queryConfig.processVariableDetailsFilter?.let {
                    detailsLookupPipeline.add(match(it))
                }

                queryConfig.processVariablesDetailsProjection?.let {
                    detailsLookupPipeline.add(project(it))
                }

                lookupPipeline.add(
                    lookup(
                        collections.historicDetailColl.namespace.collectionName,
                        listOf(
                            HistoricVariableInstanceDto::id.variableDefinition(),
                            (HistoricVariableInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                            (HistoricVariableInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                        ),
                        HistoricVariableInstanceDto::relations / Relations::historicDetails,
                        *detailsLookupPipeline.toTypedArray()
                    )
                )
            }

            pipeline.add(
                lookup(
                    collections.historicVariableInstanceColl.namespace.collectionName,
                    listOf(
                        HistoricProcessInstanceDto::id.variableDefinition(),
                        (HistoricVariableInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                        (HistoricVariableInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                    ),
                    HistoricProcessInstanceDto::relations / Relations::historicVariableInstances,
                    *lookupPipeline.toTypedArray()
                )
            )
        }

        if (queryConfig.includeActivityInstances) {

            val lookupPipeline: MutableList<Bson> = mutableListOf()

            lookupPipeline.add(
                match(
                    expr(
                        "eq".projection from listOf(
                            (HistoricActivityInstanceDto::metadata / EngineMetadata::clusterName).projection,
                            "\$\$clusterName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            (HistoricActivityInstanceDto::metadata / EngineMetadata::engineName).projection,
                            "\$\$engineName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            HistoricActivityInstanceDto::processInstanceId.projection,
                            HistoricProcessInstanceDto::id.variable
                        )
                    )
                )
            )

            queryConfig.activityInstancesFilter?.let {
                lookupPipeline.add(match(it))
            }

            queryConfig.activityInstancesProjection?.let {
                lookupPipeline.add(project(it))
            }

            pipeline.add(
                lookup(
                    collections.historicActivityInstanceColl.namespace.collectionName,
                    listOf(
                        HistoricProcessInstanceDto::id.variableDefinition(),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                    ),
                    HistoricProcessInstanceDto::relations / Relations::historicActivityInstances,
                    *lookupPipeline.toTypedArray()
                )
            )
        }

        if (queryConfig.includeIncidents) {

            val lookupPipeline: MutableList<Bson> = mutableListOf()

            lookupPipeline.add(
                match(
                    expr(
                        "eq".projection from listOf(
                            (HistoricIncidentDto::metadata / EngineMetadata::clusterName).projection,
                            "\$\$clusterName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            (HistoricIncidentDto::metadata / EngineMetadata::engineName).projection,
                            "\$\$engineName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            HistoricIncidentDto::processInstanceId.projection,
                            HistoricProcessInstanceDto::id.variable
                        )
                    )
                )
            )

            queryConfig.incidentsFilter?.let {
                lookupPipeline.add(match(it))
            }

            queryConfig.incidentsProjection?.let {
                lookupPipeline.add(project(it))
            }

            pipeline.add(
                lookup(
                    collections.historicIncidentColl.namespace.collectionName,
                    listOf(
                        HistoricProcessInstanceDto::id.variableDefinition(),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                    ),
                    HistoricProcessInstanceDto::relations / Relations::historicIncidents,
                    *lookupPipeline.toTypedArray()
                )
            )
        }

        if (queryConfig.includeJobLogs) {

            val lookupPipeline: MutableList<Bson> = mutableListOf()

            lookupPipeline.add(
                match(
                    expr(
                        "eq".projection from listOf(
                            (HistoricJobLogDto::metadata / EngineMetadata::clusterName).projection,
                            "\$\$clusterName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            (HistoricJobLogDto::metadata / EngineMetadata::engineName).projection,
                            "\$\$engineName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            HistoricJobLogDto::processInstanceId.projection,
                            HistoricProcessInstanceDto::id.variable
                        )
                    )
                )
            )

            queryConfig.jobLogsFilter?.let {
                lookupPipeline.add(match(it))
            }

            queryConfig.jobLogsProjection?.let {
                lookupPipeline.add(project(it))
            }

            pipeline.add(
                lookup(
                    collections.historicJobLogColl.namespace.collectionName,
                    listOf(
                        HistoricProcessInstanceDto::id.variableDefinition(),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                    ),
                    HistoricProcessInstanceDto::relations / Relations::historicJobLogs,
                    *lookupPipeline.toTypedArray()
                )
            )
        }

        if (queryConfig.includeTasks) {

            val lookupPipeline: MutableList<Bson> = mutableListOf()

            lookupPipeline.add(
                match(
                    expr(
                        "eq".projection from listOf(
                            (HistoricTaskInstanceDto::metadata / EngineMetadata::clusterName).projection,
                            "\$\$clusterName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            (HistoricTaskInstanceDto::metadata / EngineMetadata::engineName).projection,
                            "\$\$engineName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            HistoricTaskInstanceDto::processInstanceId.projection,
                            HistoricProcessInstanceDto::id.variable
                        )
                    )
                )
            )

            queryConfig.tasksFilter?.let {
                lookupPipeline.add(match(it))
            }

            queryConfig.tasksProjection?.let {
                lookupPipeline.add(project(it))
            }


            if (queryConfig.includeTasksIdentityLinkLogs) {
                val tasksIdentityLinksLogLookupPipeline: MutableList<Bson> = mutableListOf()

                tasksIdentityLinksLogLookupPipeline.add(
                    match(
                        and(
                            expr(
                                "eq".projection from listOf(
                                    (HistoricDetailVariableUpdateDto::metadata / EngineMetadata::clusterName).projection,
                                    "\$\$clusterName"
                                )
                            ),
                            expr(
                                "eq".projection from listOf(
                                    (HistoricDetailVariableUpdateDto::metadata / EngineMetadata::engineName).projection,
                                    "\$\$engineName"
                                )
                            ),
                            expr(
                                "eq".projection from listOf(
                                    HistoricIdentityLinkLogDto::rootProcessInstanceId.projection,
                                    HistoricTaskInstanceDto::processInstanceId.variable
                                )
                            ),
                            expr(
                                "eq".projection from listOf(
                                    HistoricIdentityLinkLogDto::taskId.projection,
                                    HistoricTaskInstanceDto::id.variable
                                )
                            )
                        )
                    )
                )

                queryConfig.tasksIdentityLinkLogsFilter?.let {
                    tasksIdentityLinksLogLookupPipeline.add(match(it))
                }

                queryConfig.tasksIdentityLinkLogsProjection?.let {
                    tasksIdentityLinksLogLookupPipeline.add(project(it))
                }

                lookupPipeline.add(
                    lookup(
                        collections.historicIdentityLinkLogColl.namespace.collectionName,
                        listOf(
                            HistoricTaskInstanceDto::id.variableDefinition(),
                            HistoricTaskInstanceDto::processInstanceId.variableDefinition(),
                            (HistoricTaskInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                            (HistoricTaskInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                        ),
                        HistoricTaskInstanceDto::relations / Relations::historicIdentityLinkLogs,
                        *tasksIdentityLinksLogLookupPipeline.toTypedArray()
                    )
                )
            }


            pipeline.add(
                lookup(
                    collections.historicTaskInstanceColl.namespace.collectionName,
                    listOf(
                        HistoricProcessInstanceDto::id.variableDefinition(),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                    ),
                    HistoricProcessInstanceDto::relations / Relations::historicTaskInstances,
                    *lookupPipeline.toTypedArray()
                )
            )
        }

        if (queryConfig.includeExternalTaskLogs) {

            val lookupPipeline: MutableList<Bson> = mutableListOf()

            lookupPipeline.add(
                match(
                    expr(
                        "eq".projection from listOf(
                            (HistoricExternalTaskLogDto::metadata / EngineMetadata::clusterName).projection,
                            "\$\$clusterName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            (HistoricExternalTaskLogDto::metadata / EngineMetadata::engineName).projection,
                            "\$\$engineName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            HistoricExternalTaskLogDto::processInstanceId.projection,
                            HistoricProcessInstanceDto::id.variable
                        )
                    )
                )
            )

            queryConfig.externalTaskLogsFilter?.let {
                lookupPipeline.add(match(it))
            }

            queryConfig.externalTaskLogsProjection?.let {
                lookupPipeline.add(project(it))
            }

            pipeline.add(
                lookup(
                    collections.historicExternalTaskLogColl.namespace.collectionName,
                    listOf(
                        HistoricProcessInstanceDto::id.variableDefinition(),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                    ),
                    HistoricProcessInstanceDto::relations / Relations::historicExternalTaskLogs,
                    *lookupPipeline.toTypedArray()
                )
            )
        }


        if (queryConfig.includeSubProcessInstances) {

            val lookupPipeline: MutableList<Bson> = mutableListOf()

            lookupPipeline.add(
                match(
                    expr(
                        "eq".projection from listOf(
                            (HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName).projection,
                            "\$\$clusterName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            (HistoricProcessInstanceDto::metadata / EngineMetadata::engineName).projection,
                            "\$\$engineName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            HistoricProcessInstanceDto::superProcessInstanceId.projection,
                            HistoricProcessInstanceDto::id.variable
                        )
                    )
                )
            )

            queryConfig.subProcessInstancesFilter?.let {
                lookupPipeline.add(match(it))
            } //@TODO should filters like this go into the original match???

            queryConfig.subProcessInstancesProjection?.let {
                lookupPipeline.add(project(it))
            }

            pipeline.add(
                lookup(
                    collections.historicProcessInstanceColl.namespace.collectionName,
                    listOf(
                        HistoricProcessInstanceDto::id.variableDefinition(),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                    ),
                    HistoricProcessInstanceDto::relations / Relations::historicProcessInstances,
                    *lookupPipeline.toTypedArray()
                )
            )
        }

        if (queryConfig.includeProcessDefinition) {

            val lookupPipeline: MutableList<Bson> = mutableListOf()

            lookupPipeline.add(
                match(
                    expr(
                        "eq".projection from listOf(
                            (HistoricProcessDefinitionDto::metadata / EngineMetadata::clusterName).projection,
                            "\$\$clusterName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            (HistoricProcessDefinitionDto::metadata / EngineMetadata::engineName).projection,
                            "\$\$engineName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            HistoricProcessDefinitionDto::id.projection,
                            HistoricProcessInstanceDto::processDefinitionId.variable
                        )
                    )
                )
            )

            queryConfig.processDefinitionFilter?.let {
                lookupPipeline.add(match(it))
            } //@TODO should filters like this go into the original match???

            queryConfig.processDefinitionProjection?.let {
                lookupPipeline.add(project(it))
            }

            pipeline.add(
                lookup(
                    collections.historicProcessDefinitionColl.namespace.collectionName,
                    listOf(
                        HistoricProcessInstanceDto::processDefinitionId.variableDefinition(),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                    ),
                    HistoricProcessInstanceDto::relations / Relations::historicProcessDefinitions,
                    *lookupPipeline.toTypedArray()
                )
            )
        }

        if (queryConfig.includeIdentityLinkLogs) {
            // identity links specific to a process instance's definition / is not related to a specific task
            val lookupPipeline: MutableList<Bson> = mutableListOf()

            lookupPipeline.add(
                match(
                    expr(
                        "eq".projection from listOf(
                            (HistoricIdentityLinkLogDto::metadata / EngineMetadata::clusterName).projection,
                            "\$\$clusterName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            (HistoricIdentityLinkLogDto::metadata / EngineMetadata::engineName).projection,
                            "\$\$engineName"
                        )
                    ),
                    or(
                        or(
                            expr(
                                "eq".projection from listOf(
                                    HistoricIdentityLinkLogDto::rootProcessInstanceId.projection,
                                    HistoricProcessInstanceDto::id.variable
                                )
                            ),
                            expr(
                                "eq".projection from listOf(
                                    HistoricIdentityLinkLogDto::processInstanceId.projection,
                                    HistoricProcessInstanceDto::id.variable
                                )
                            )
                        ),
                        or(
                            expr(
                                "eq".projection from listOf(
                                    HistoricIdentityLinkLogDto::processDefinitionId.projection,
                                    HistoricProcessInstanceDto::processDefinitionId.variable
                                )
                            ),
                            expr(
                                "eq".projection from listOf(
                                    HistoricIdentityLinkLogDto::rootProcessInstanceId.projection,
                                    null
                                )
                            ),
                            expr(
                                "eq".projection from listOf(
                                    HistoricIdentityLinkLogDto::processInstanceId.projection,
                                    null
                                )
                            ),
                            expr(
                                "eq".projection from listOf(
                                    HistoricIdentityLinkLogDto::taskId.projection,
                                    null
                                )
                            )
                        )
                    )
                )
            )

            queryConfig.identityLinkLogsFilter?.let {
                lookupPipeline.add(match(it))
            }

            queryConfig.identityLinkLogsProjection?.let {
                lookupPipeline.add(project(it))
            }

            pipeline.add(
                lookup(
                    collections.historicIdentityLinkLogColl.namespace.collectionName,
                    listOf(
                        HistoricProcessInstanceDto::processDefinitionId.variableDefinition(),
                        HistoricProcessInstanceDto::id.variableDefinition(),
                            (HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                    ),
                    HistoricProcessInstanceDto::relations / Relations::historicIdentityLinkLogs,
                    *lookupPipeline.toTypedArray()
                )
            )
        }

        if (queryConfig.includeUserOperationEntryLogs) {

            val lookupPipeline: MutableList<Bson> = mutableListOf()

            lookupPipeline.add(
                match(
                    expr(
                        "eq".projection from listOf(
                            (HistoricUserOperationLogEntryDto::metadata / EngineMetadata::clusterName).projection,
                            "\$\$clusterName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            (HistoricUserOperationLogEntryDto::metadata / EngineMetadata::engineName).projection,
                            "\$\$engineName"
                        )
                    ),
                    expr(
                        "eq".projection from listOf(
                            HistoricUserOperationLogEntryDto::processInstanceId.projection,
                            HistoricProcessInstanceDto::id.variable
                        )
                    ),
                )
            )

            queryConfig.userOperationEntryLogsFilter?.let {
                lookupPipeline.add(match(it))
            }

            queryConfig.userOperationEntryLogsProjection?.let {
                lookupPipeline.add(project(it))
            }

            pipeline.add(
                lookup(
                    collections.historicUserOperationLogColl.namespace.collectionName,
                    listOf(
                        HistoricProcessInstanceDto::id.variableDefinition(),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::clusterName).variableDefinition("clusterName"),
                        (HistoricProcessInstanceDto::metadata / EngineMetadata::engineName).variableDefinition("engineName")
                    ),
                    HistoricProcessInstanceDto::relations / Relations::historicUserOperationLogEntries,
                    *lookupPipeline.toTypedArray()
                )
            )
        }


        val result = collections.historicProcessInstanceColl.aggregate(pipeline, HistoricProcessInstanceDto::class.java)

        queryConfig.allowDiskUse?.let {
            result.allowDiskUse(it)
        }

        return result
    }
}