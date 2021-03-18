package com.github.stephenott.camunda.multienginedataservice.dto

import com.github.stephenott.camunda.multienginedataservice.deployer.HistoricDeploymentDto
import com.github.stephenott.camunda.multienginedataservice.deployer.HistoricProcessDefinitionDto
import com.github.stephenott.camunda.multienginedataservice.deployer.HistoricResourceDto
import com.github.stephenott.camunda.multienginedataservice.dto.*

data class Relations(
    val historicProcessInstances: List<HistoricProcessInstanceDto>? = null,
    val historicVariableInstances: List<HistoricVariableInstanceDto>? = null,
    val historicActivityInstances: List<HistoricActivityInstanceDto>? = null,
    val historicTaskInstances: List<HistoricTaskInstanceDto>? = null,
    val historicExternalTaskLogs: List<HistoricExternalTaskLogDto>? = null,
    val historicIncidents: List<HistoricIncidentDto>? = null,
    val historicDetails: List<HistoricDetail>? = null,
    val historicJobLogs: List<HistoricJobLogDto>? = null,
    val historicIdentityLinkLogs: List<HistoricIdentityLinkLogDto>? = null,
    val historicUserOperationLogEntries: List<HistoricUserOperationLogEntryDto>? = null,
    val historicDecisionInstances: List<HistoricDecisionInstanceDto>? = null,
    val historicDeployments: List<HistoricDeploymentDto>? = null,
    val historicDeploymentResources: List<HistoricResourceDto>? = null,
    val historicProcessDefinitions: List<HistoricProcessDefinitionDto>? = null
)


