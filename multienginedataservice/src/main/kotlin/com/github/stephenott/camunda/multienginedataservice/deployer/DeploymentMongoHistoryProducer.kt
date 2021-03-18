package com.github.stephenott.camunda.multienginedataservice.deployer

import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.generateMetadata
import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.generateMongoId
import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.getMongoSessionFromContext
import com.github.stephenott.camunda.multienginedataservice.dto.HistoricData
import com.github.stephenott.camunda.multienginedataservice.moddler.ModdledBpmn
import com.github.stephenott.camunda.multienginedataservice.moddler.ModdlerConverter
import com.mongodb.client.MongoCollection
import org.camunda.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity
import org.camunda.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity
import org.camunda.bpm.engine.impl.persistence.deploy.Deployer
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity
import java.io.File

class DeploymentMongoHistoryProducer(
    private val deploymentHistoryColl: MongoCollection<HistoricData>,
    private val deploymentResourceHistoryColl: MongoCollection<HistoricData>,
    private val historicProcessDefinitionColl: MongoCollection<HistoricData>,
    private val historicDecisionDefinitionColl: MongoCollection<HistoricData>,
    private val historicDecisionRequirementsDefinitionColl: MongoCollection<HistoricData>,
    private val moddlerConverter: ModdlerConverter? //@TODO add path for node cwd into the resources folder
) : Deployer {

    //@TODO refactor to be neutral without mongo
    override fun deploy(deployment: DeploymentEntity) {

        val moddledReources: MutableMap<String, ModdledBpmn> = mutableMapOf()

        val resources: List<HistoricResourceDto> = deployment.resources.map {
            //@TODO add logic to add additional resource details when certain types of files (like a bpmn)
            HistoricResourceDto(
                id = it.value.id,
                name = it.value.name,
                createTime = it.value.createTime,
                isGenerated = it.value.isGenerated,
                tenantId = it.value.tenantId,
                type = it.value.type,
                deploymentId = it.value.deploymentId,
                bytes = it.value.bytes,
                moddle = kotlin.run {
                    moddlerConverter?.let { mc ->
                        val fileExtension = File(it.value.name).extension
                        if (fileExtension == "bpmn") {
                            val moddledBpmn = mc.convertBpmnXml(it.value.bytes.decodeToString())
                            moddledReources[it.value.name] = moddledBpmn
                            moddledBpmn.value
//                    } else if (fileExtension == "dmn") {
//                        val moddledDmn = moddlerConverter.convertDmnXml(it.value.bytes.decodeToString())
//                        moddledReources[it.value.name] = moddledDmn
//                        moddledDmn.value
                        } else {
                            null
                        }
                    }
                },
                _id = generateMongoId(),
                metadata = generateMetadata()
            )
        }

        val deploymentDto = HistoricDeploymentDto(
            id = deployment.id,
            name = deployment.name,
            resources = resources.map {
                requireNotNull(it._id)
                it._id
            }.toSet(), //@TODO review if Set is actually required?
            deploymentTime = deployment.deploymentTime,
            source = deployment.source,
            tenantId = deployment.tenantId,
            _id = generateMongoId(),
            metadata = generateMetadata()
        )

        deploymentHistoryColl.insertOne(getMongoSessionFromContext(), deploymentDto)

        resources.forEach {
            deploymentResourceHistoryColl.insertOne(getMongoSessionFromContext(), it)
        }

        deployment.deployedArtifacts.forEach { artifact ->
            when (artifact.key) {
                ProcessDefinitionEntity::class.java -> {
                    (artifact.value as List<ProcessDefinitionEntity>).forEach { def ->
                        val defDto = HistoricProcessDefinitionDto(
                            id = def.id,
                            key = def.key,
                            category = def.category,
                            description = def.description,
                            name = def.name,
                            version = def.version,
                            resource = def.resourceName,
                            deploymentId = def.deploymentId,
                            diagram = def.diagramResourceName,
                            suspended = def.isSuspended,
                            versionTag = def.versionTag,
                            tenantId = def.tenantId,
                            historyTimeToLive = def.historyTimeToLive,
                            isStartableInTasklist = def.isStartableInTasklist,
                            isGraphicalNotationDefined = def.isGraphicalNotationDefined,
                            hasStartFormKey = def.hasStartFormKey,
                            previousProcessDefinitionId = def.previousProcessDefinitionId,
                            moddled = kotlin.run {
                                moddlerConverter?.let { mc ->
                                    mc.buildProcessDefinitionModdle(
                                        moddledReources.getOrElse(def.resourceName, { throw IllegalStateException("") }),
                                        def.key
                                    )
                                }
                            },
                            _id = generateMongoId(),
                            metadata = generateMetadata()
                        )
                        historicProcessDefinitionColl.insertOne(getMongoSessionFromContext(), defDto)
                    }
                }
                DecisionDefinitionEntity::class.java -> {
                    (artifact.value as List<DecisionDefinitionEntity>).forEach { def ->
                        val defDto = HistoricDecisionDefinitionDto(
                            id = def.id,
                            key = def.key,
                            category = def.category,
                            name = def.name,
                            version = def.version,
                            resource = def.resourceName,
                            deploymentId = def.deploymentId,
                            versionTag = def.versionTag,
                            tenantId = def.tenantId,
                            historyTimeToLive = def.historyTimeToLive,
                            previousDecisionDefinitionId = def.previousDecisionDefinitionId,
                            decisionRequirementsDefinitionId = def.decisionRequirementsDefinitionId,
                            decisionRequirementsDefinitionKey = def.decisionRequirementsDefinitionKey,
//                            moddled = moddlerConverter.buildDecisionDefinitionModdle(
//                                moddledReources.getOrElse(def.resourceName, { throw IllegalStateException("") }),
//                                def.key
//                            ),
                            _id = generateMongoId(),
                            metadata = generateMetadata()
                        )
                        historicDecisionDefinitionColl.insertOne(getMongoSessionFromContext(), defDto)
                    }
                }
                DecisionRequirementsDefinitionEntity::class.java -> {
                    (artifact.value as List<DecisionRequirementsDefinitionEntity>).forEach { def ->
                        val defDto = HistoricDecisionRequirementsDefinitionDto(
                            id = def.id,
                            key = def.key,
                            category = def.category,
                            name = def.name,
                            version = def.version,
                            resource = def.resourceName,
                            deploymentId = def.deploymentId,
                            diagram = def.diagramResourceName,
                            tenantId = def.tenantId,
                            previousDecisionRequirementsDefinitionId = def.previousDecisionRequirementsDefinitionId,
//                            moddled = moddlerConverter.buildProcessDefinitionModdle(
//                                moddledReources.getOrElse(def.resourceName, { throw IllegalStateException("") }),
//                                def.key
//                            ),
                            decisions = def.decisions.map { (it as DecisionDefinitionEntity).id },
                            _id = generateMongoId(),
                            metadata = generateMetadata()
                        )
                        historicDecisionRequirementsDefinitionColl.insertOne(getMongoSessionFromContext(), defDto)
                    }
                }
                else -> {
                    TODO("add extra logic!!")
                }
            }
        }
    }
}