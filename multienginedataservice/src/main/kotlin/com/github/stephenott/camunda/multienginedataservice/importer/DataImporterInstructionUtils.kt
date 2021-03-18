package com.github.stephenott.camunda.multienginedataservice.importer

import com.github.stephenott.camunda.multienginedataservice.dto.HistoricData
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.impl.history.event.HistoryEvent

object DataImporterInstructionUtils {

    fun processInstanceInstructions(
        processEngine: ProcessEngine,
        step: Int = 100,
        collection: MongoCollection<HistoricData>,
        mongoClient: MongoClient
    ): List<ImportInstruction> {
        val totalInstances = processEngine.historyService.createHistoricProcessInstanceQuery().count()

        val list = mutableListOf<ImportInstruction>()

        (0..totalInstances step step.toLong()).forEachIndexed { index, currentStep ->

            val startAt: Int = if (index == 0) currentStep.toInt() else (currentStep).toInt()

            list.add(
                ImportInstruction(
                    name = "Process Instance: Start: $startAt  max Results: $step",
                    collection = collection, client = mongoClient
                ) {
                    val result = it.historyService.createHistoricProcessInstanceQuery()
                        .listPage(startAt, step) as List<HistoryEvent>
                    result
                })
        }

        return list
    }

}