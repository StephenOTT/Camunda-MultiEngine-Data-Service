package com.github.stephenott.camunda.multienginedataservice.importer

import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.generateMetadata
import com.github.stephenott.camunda.multienginedataservice.MongoHistoryEventHandlerUtils.generateMongoId
import com.github.stephenott.camunda.multienginedataservice.dto.HistoricData
import com.github.stephenott.camunda.multienginedataservice.toHistoricData
import org.camunda.bpm.engine.ProcessEngine
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.streams.toList


/**
 * Data Import capability for use on a Camunda Process Engine.
 *
 * Allows the definition of instructions which are used to import data.
 */
class DataImporter(
    val instructions: MutableList<ImportInstruction> = mutableListOf()
) {

    fun addInstructions(instruction: ImportInstruction): DataImporter {
        this.instructions.add(instruction)
        return this
    }

    fun addInstructions(instructions: List<ImportInstruction>): DataImporter {
        this.instructions.addAll(instructions)
        return this
    }

    /**
     * Import data from processEngine using instructions
     */
    fun executeImport(
        processEngine: ProcessEngine,
        logImports: Boolean = false,
        logger: Logger = LoggerFactory.getLogger(DataImporter::class.java)
    ) {
        // @TODO add CompletableFuture support

        instructions.forEach { instruc ->

            val transactionId: String = "import-" + generateMongoId()

            logger.info("Starting import instruction: ${instruc.name} in transaction: $transactionId")

            val session = instruc.client.startSession()

            session.startTransaction()

            //@TODO use a while statement and check if stop() was called.
            //@TODO consider turning into a callable?

            //@TODO turn this into a sequence/parallel
            kotlin.runCatching {
                val events: List<HistoricData> = instruc.query.invoke(processEngine).parallelStream().map {
                    if (logImports) {
                        logger.info("Generating Historic Data for ${it::class.qualifiedName} -> $it")
                    }
                    it.toHistoricData(
                        generateMetadata(processEngine = processEngine, transactionId = transactionId),
                        generateMongoId()
                    )
                }.toList()

                logger.info("Inserting events from transaction: $transactionId into Mongo")

                if (events.isNotEmpty()){
                    instruc.collection.insertMany(session, events)
                }

                session.commitTransaction()

                logger.info("Finished import instruction: ${instruc.name} in transaction: $transactionId")

            }.onFailure {
                logger.error("Instruction Failure: ${instruc.name} : Error: ${it.message}", it)
                session.abortTransaction()
            }
        }
    }
}