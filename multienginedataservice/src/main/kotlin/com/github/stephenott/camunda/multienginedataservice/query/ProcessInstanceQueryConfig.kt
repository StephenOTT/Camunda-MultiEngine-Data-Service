package com.github.stephenott.camunda.multienginedataservice.query

import com.github.stephenott.camunda.multienginedataservice.security.SecurityFilter
import org.bson.conversions.Bson

data class ProcessInstanceQueryConfig(
    val processInstanceFilter: Bson? = null,
    val processInstanceProjection: Bson? = null,

    val preRelationsSecurityFilter: SecurityFilter? = null,

    /**
     * Filters out non-root process instances.
     * Runs before the process instance filter.
     * False means the process instance is not a root instance.
     */
    val isRootInstance: Boolean? = null, //@TODO rename this to the leaf vs leaflet ?

    val includeProcessVariables: Boolean = false,
    val processVariableFilter: Bson? = null,
    val processVariableSecurityFilter: SecurityFilter? = null,
    val processVariablesProjection: Bson? = null,

    val includeProcessVariableDetails: Boolean = false,
    val processVariableDetailsFilter: Bson? = null,
    val processVariablesDetailsProjection: Bson? = null,


//    val includeProcessVariableUpdateDetails: Boolean = false,

    val includeActivityInstances: Boolean = false,
    val activityInstancesFilter: Bson? = null,
    val activityInstancesProjection: Bson? = null,

//    val includeLocalVariables: Boolean = false, // @TODO local variables should show on activity instances...
//    val localVariableFilter: Bson? = null,
//    val localVariablesProjection: Bson? = null,

    val includeIncidents: Boolean = false,
    val incidentsFilter: Bson? = null,
    val incidentsProjection: Bson? = null,

    val includeJobLogs: Boolean = false,
    val jobLogsFilter: Bson? = null,
    val jobLogsProjection: Bson? = null,

    val includeTasks: Boolean = false,
    val tasksFilter: Bson? = null,
    val tasksProjection: Bson? = null,

    val includeTasksIdentityLinkLogs: Boolean = false,
    val tasksIdentityLinkLogsFilter: Bson? = null,
    val tasksIdentityLinkLogsProjection: Bson? = null,

    val includeExternalTaskLogs: Boolean = false,
    val externalTaskLogsFilter: Bson? = null,
    val externalTaskLogsProjection: Bson? = null,

    val includeSubProcessInstances: Boolean = false,
    val subProcessInstancesFilter: Bson? = null,
    val subProcessInstancesProjection: Bson? = null,

    val includeProcessDefinition: Boolean = false,
    val processDefinitionFilter: Bson? = null,
    val processDefinitionProjection: Bson? = null,

    /**
     * Joins with all identity links related to the process instance (definition id and tasks)
     *
     * Use the Filter to further limit results.
     *
     * Identity Link Logs with a TaskId are specific to a Task.
     *
     * Identity Links without a taskId and no processInstanceId or RootProcessInstanceId are specific to a processDefinitionId.
     */
    val includeIdentityLinkLogs: Boolean = false,
    val identityLinkLogsFilter: Bson? = null,
    val identityLinkLogsProjection: Bson? = null,
    //@TODO identityLinks (at least ones that are not expressions?) on a process def (starter users and groups) are tied to the definition and have no process instance IDs.

    /**
     * Joins with all User Operation Entry Logs related to the process instance.
     *
     * Use the Filter to further limit results.
     */
    val includeUserOperationEntryLogs: Boolean = false,
    val userOperationEntryLogsFilter: Bson? = null,
    val userOperationEntryLogsProjection: Bson? = null,

    val postRelationsSecurityFilter: SecurityFilter? = null,

    val sort: Bson? = null,

//    val sortbyTimeandSequence  //@TODO

    //sort(ascending(HistoricProcessInstanceDto::startTime, HistoricProcessInstanceDto::sequenceCounter)), // @TODO should this be start time first or seq counter?
    val limit: Int? = null,
    val skip: Int? = null,
    val allowDiskUse: Boolean? = null
){
}