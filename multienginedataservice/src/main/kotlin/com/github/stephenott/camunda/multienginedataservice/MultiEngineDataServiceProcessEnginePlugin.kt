package com.github.stephenott.camunda.multienginedataservice

import com.fasterxml.jackson.databind.module.SimpleModule
import com.github.stephenott.camunda.multienginedataservice.deployer.DeploymentMongoHistoryProducer
import com.github.stephenott.camunda.multienginedataservice.dto.HistoricData
import com.github.stephenott.camunda.multienginedataservice.moddler.ModdlerConverter
import com.github.stephenott.camunda.multienginedataservice.serializers.JacksonJsonNodeSerializer
import com.github.stephenott.camunda.multienginedataservice.session.MongoTransactionSessionFactory
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.camunda.bpm.engine.ProcessEngineException
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.spin.impl.json.jackson.JacksonJsonNode
import org.litote.kmongo.KMongo
import org.litote.kmongo.bson
import org.litote.kmongo.util.KMongoConfiguration
import org.slf4j.LoggerFactory

/**
 * Camunda Process Engine Plugin that adds a Mongo based History Event Handler and a Session for use by the Handler to ensure transaction support
 */
open class MultiEngineDataServiceProcessEnginePlugin(
    /**
     * Mongo Client Settings.  Defaults to standard settings.  Use this when you want to set specific configurations through code.
     * Default: `MongoClientSettings.builder().build()`
     */
    var clientSettings: MongoClientSettings = MongoClientSettings.builder().build(),

    /**
     * Mongo Connection String.  Will be added to the Client Settings if used.
     * Default: null
     */
    var datasourceConnectionString: String? = null,

    /**
     * The Database name for Multi-Engine Data Service.
     * Default: camunda-history
     */
    var databaseName: String = "camunda-history",

    /**
     * If Multi-Engine Data Service ping fails, then engine startup will fail.
     * Default: true
     */
    var stopStartupIfNoDbConnection: Boolean = true,

    /**
     * Pings the Multi-Engine Data Service Database to ensure a active connection on startup
     * If false, then stopStartupIfNoDbConnection has no effect.
     *
     * Default: true
     */
    var pingDbOnStartup: Boolean = true,

    var generateIdentityLinkInstances: Boolean = true,

    var clusterName: String? = null,

    /**
     * Set to true if you want resources (BPMN, DMN) to be moddle / converted to JSON on deployment and stored as part of the historic definition.
     * If true and the `moddleConverter` argument is null, then a default ModdleConverter will be used.
     * Default: false
     */
    var moddleDefinitionsOnDeployment: Boolean = false,

    var moddlePackageDirectory: String = "./moddle-packages",

    var moddleConverter: ModdlerConverter? = null

) : AbstractProcessEnginePlugin() {

    private val LOG = LoggerFactory.getLogger(MultiEngineDataServiceProcessEnginePlugin::class.java)

    /**
     * Mongo Client
     */
    private lateinit var client: MongoClient

    /**
     * Mongo Database used
     */
    private lateinit var database: MongoDatabase

    //@TODO review if this is needed at this level?
    lateinit var collections: HistoryEventMongoCollections

    init {
        KMongoConfiguration.registerBsonModule(
            SimpleModule("MongoCamundaSpinSerializer").addSerializer(
                JacksonJsonNode::class.java,
                JacksonJsonNodeSerializer()
            )
        )
    }

    companion object {
        var clusterName: String? = null
    }


    override fun preInit(processEngineConfiguration: ProcessEngineConfigurationImpl) {

        MultiEngineDataServiceProcessEnginePlugin.clusterName = this.clusterName

        moddleConverter = if (moddleDefinitionsOnDeployment && moddleConverter == null) {
            ModdlerConverter(nodeRequireCwd = moddlePackageDirectory)
        } else {
            null
        }

        client = KMongo.createClient(
            MongoClientSettings.builder(clientSettings).apply {
                datasourceConnectionString?.let {
                    applyConnectionString(ConnectionString(it))
                }
            }.build()
        )

        database = client.getDatabase(databaseName)

        if (pingDbOnStartup) {
            kotlin.runCatching {
                LOG.info("Checking connection to History Database: $databaseName")
                //@TODO look at adding a timeout capability
                database.runCommand("{ping: 1}".bson)

            }.onFailure {
                LOG.error("Failed to connect to Multi-Engine Data Service: $databaseName", it)
                if (stopStartupIfNoDbConnection) {
                    throw ProcessEngineException("Unable to connect to Multi-Engine Data Service: $databaseName", it)
                }

            }.onSuccess {
                LOG.info("Connected to Multi-Engine Data Service: $databaseName")
            }
        }

        //@TODO consider a map of collection names for custom configuration?
        collections = HistoryEventMongoCollections(
            historicActivityInstanceColl = database.getCollection(
                "historicActivityInstanceColl",
                HistoricData::class.java
            ),
            historicDecisionInstanceColl = database.getCollection(
                "historicDecisionInstanceColl",
                HistoricData::class.java
            ),
            historicDetailColl = database.getCollection("historicDetailColl", HistoricData::class.java),
            historicExternalTaskLogColl = database.getCollection(
                "historicExternalTaskLogColl",
                HistoricData::class.java
            ),
            historicIdentityLinkLogColl = database.getCollection(
                "historicIdentityLinkLogColl",
                HistoricData::class.java
            ),
            historicIncidentColl = database.getCollection("historicIncidentColl", HistoricData::class.java),
            historicJobLogColl = database.getCollection("historicJobLogColl", HistoricData::class.java),
            historicProcessInstanceColl = database.getCollection(
                "historicProcessInstanceColl",
                HistoricData::class.java
            ),
            historicTaskInstanceColl = database.getCollection("historicTaskInstanceColl", HistoricData::class.java),
            historicUserOperationLogColl = database.getCollection(
                "historicUserOperationLogColl",
                HistoricData::class.java
            ),
            historicVariableInstanceColl = database.getCollection(
                "historicVariableInstanceColl",
                HistoricData::class.java
            ),
            historicCaseActivityInstanceColl = database.getCollection(
                "historicCaseActivityInstanceColl",
                HistoricData::class.java
            ),
            historicCaseInstanceColl = database.getCollection("historicCaseInstanceColl", HistoricData::class.java),
            historicDeploymentColl = database.getCollection("historicDeploymentColl", HistoricData::class.java),
            historicDeploymentResourceColl = database.getCollection(
                "historicDeploymentResourceColl",
                HistoricData::class.java
            ),
            historicProcessDefinitionColl = database.getCollection(
                "historicProcessDefinitionColl",
                HistoricData::class.java
            ),
            historicIdentityLinkInstanceColl = database.getCollection(
                "historicIdentityLinkInstanceColl",
                HistoricData::class.java
            ),
            historicExternalTaskInstanceColl = database.getCollection(
                "historicExternalTaskInstanceColl",
                HistoricData::class.java
            ),
            historicDecisionDefinitionColl = database.getCollection(
                "historicDecisionDefinitionColl",
                HistoricData::class.java
            ),
            historicDecisionRequirementsDefinitionColl = database.getCollection(
                "historicDecisionRequirementsDefinitionColl",
                HistoricData::class.java
            )
        )

        val historicEventHandlerConfig = HistoricEventHandlerConfig(
            generateIdentityLinkInstances = this.generateIdentityLinkInstances,
            clusterName = this.clusterName
        )

        // Adds History Event Handler
        processEngineConfiguration.customHistoryEventHandlers.add(
            MongoEnhancedHistoryEventHandler(
                collections,
                historicEventHandlerConfig
            )
        )

        // Adds Session so the History Event Handler's DB will perform rollbacks if engine's transaction fails.
        if (processEngineConfiguration.customSessionFactories == null) {
            processEngineConfiguration.customSessionFactories = mutableListOf()
        }
        processEngineConfiguration.customSessionFactories.add(
            MongoTransactionSessionFactory(
                client = client
            )
        )

        if (processEngineConfiguration.customPostDeployers == null) {
            processEngineConfiguration.customPostDeployers = mutableListOf()
        }
        processEngineConfiguration.customPostDeployers.add(
            DeploymentMongoHistoryProducer(
                deploymentHistoryColl = collections.historicDeploymentColl,
                deploymentResourceHistoryColl = collections.historicDeploymentResourceColl,
                historicProcessDefinitionColl = collections.historicProcessDefinitionColl,
                historicDecisionDefinitionColl = collections.historicDecisionDefinitionColl,
                historicDecisionRequirementsDefinitionColl = collections.historicDecisionRequirementsDefinitionColl,
                moddlerConverter = if (moddleDefinitionsOnDeployment) {
                    moddleConverter
                } else {
                    null
                }
            )
        )

//        processEngineConfiguration.isRestrictUserOperationLogToAuthenticatedUsers = false

    }

//    override fun postProcessEngineBuild(processEngine: ProcessEngine) {

//        HistoryService(processEngine, collections = collections)
//            .processInstanceQuery(queryConfig = ProcessInstanceQueryConfig()).toList()

//        super.postProcessEngineBuild(processEngine)
//    }
}