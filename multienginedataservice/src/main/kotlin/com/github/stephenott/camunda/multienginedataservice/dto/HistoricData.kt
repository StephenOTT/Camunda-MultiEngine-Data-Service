package com.github.stephenott.camunda.multienginedataservice.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.stephenott.camunda.multienginedataservice.deployer.HistoricDeploymentDto
import com.github.stephenott.camunda.multienginedataservice.deployer.HistoricProcessDefinitionDto
import com.github.stephenott.camunda.multienginedataservice.deployer.HistoricResourceDto


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "@type")
@JsonSubTypes(
    JsonSubTypes.Type(name = "historicDecisionInstance", value = HistoricDecisionInstanceDto::class),
    JsonSubTypes.Type(name = "historicDetailFormField", value = HistoricDetailFormFieldDto::class),
    JsonSubTypes.Type(name = "historicDetailVariableUpdate", value = HistoricDetailVariableUpdateDto::class),
    JsonSubTypes.Type(name = "historicExternalTaskLog", value = HistoricExternalTaskLogDto::class),
    JsonSubTypes.Type(name = "historicIdentityLink", value = HistoricIdentityLinkLogDto::class),
    JsonSubTypes.Type(name = "historicIncident", value = HistoricIncidentDto::class),
    JsonSubTypes.Type(name = "historicJobLog", value = HistoricJobLogDto::class),
    JsonSubTypes.Type(name = "historicProcessInstance", value = HistoricProcessInstanceDto::class),
    JsonSubTypes.Type(name = "historicTaskInstance", value = HistoricTaskInstanceDto::class),
    JsonSubTypes.Type(name = "historicUserOperationLogEntry", value = HistoricUserOperationLogEntryDto::class),
    JsonSubTypes.Type(name = "historicVariableInstance", value = HistoricVariableInstanceDto::class),
    JsonSubTypes.Type(name = "historicActivityInstance", value = HistoricActivityInstanceDto::class),
    JsonSubTypes.Type(name = "historicProcessDefinition", value = HistoricProcessDefinitionDto::class),
    JsonSubTypes.Type(name = "historicDeploymentResource", value = HistoricResourceDto::class),
    JsonSubTypes.Type(name = "historicDeployment", value = HistoricDeploymentDto::class),
)
interface HistoricData: EventTyped, Metadataed, MongoIdentified {
    val id: String?
    val tenantId: String?
}