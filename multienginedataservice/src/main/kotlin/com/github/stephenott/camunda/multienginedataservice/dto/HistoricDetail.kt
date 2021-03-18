package com.github.stephenott.camunda.multienginedataservice.dto

import java.util.*


//@JsonTypeInfo(
//    use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type"
//)
//@JsonSubTypes(
//    Type(name = "variableUpdate", value = HistoricDetailVariableUpdateDto::class),
//    Type(name = "formField", value = HistoricDetailFormFieldDto::class)
//)
interface HistoricDetail: HistoricData {
    val processDefinitionKey: String?
    val processDefinitionId: String?
    val processInstanceId: String?
    val activityInstanceId: String?
    val executionId: String?
    val caseDefinitionKey: String?
    val caseDefinitionId: String?
    val caseInstanceId: String?
    val caseExecutionId: String?
    val taskId: String?
    val userOperationId: String?
    val time: Date?
    val removalTime: Date?
    val rootProcessInstanceId: String?
}

