package com.github.stephenott.camunda.multienginedataservice

import com.github.stephenott.camunda.multienginedataservice.dto.HistoricData
import com.mongodb.client.MongoCollection

data class HistoryEventMongoCollections(
    val historicActivityInstanceColl: MongoCollection<HistoricData>,
    val historicDecisionInstanceColl: MongoCollection<HistoricData>,
    val historicDetailColl: MongoCollection<HistoricData>,
    val historicExternalTaskLogColl: MongoCollection<HistoricData>,
    val historicIdentityLinkLogColl: MongoCollection<HistoricData>,
    val historicIncidentColl: MongoCollection<HistoricData>,
    val historicJobLogColl: MongoCollection<HistoricData>,
    val historicProcessInstanceColl: MongoCollection<HistoricData>,
    val historicTaskInstanceColl: MongoCollection<HistoricData>,
    val historicUserOperationLogColl: MongoCollection<HistoricData>,
    val historicVariableInstanceColl: MongoCollection<HistoricData>,
    val historicCaseActivityInstanceColl: MongoCollection<HistoricData>,
    val historicCaseInstanceColl: MongoCollection<HistoricData>,
    val historicDeploymentColl: MongoCollection<HistoricData>,
    val historicDeploymentResourceColl: MongoCollection<HistoricData>,
    val historicProcessDefinitionColl: MongoCollection<HistoricData>,
    val historicIdentityLinkInstanceColl: MongoCollection<HistoricData>,
    val historicExternalTaskInstanceColl: MongoCollection<HistoricData>,
    val historicDecisionDefinitionColl: MongoCollection<HistoricData>,
    val historicDecisionRequirementsDefinitionColl: MongoCollection<HistoricData>
) {
}