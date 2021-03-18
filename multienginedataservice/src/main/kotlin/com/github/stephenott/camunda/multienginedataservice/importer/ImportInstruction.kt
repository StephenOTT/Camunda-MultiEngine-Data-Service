package com.github.stephenott.camunda.multienginedataservice.importer

import com.github.stephenott.camunda.multienginedataservice.dto.HistoricData
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.impl.history.event.HistoryEvent

data class ImportInstruction(
    /**
     * Unique name of instruction: used to identify failures
     */
    val name: String,
    val client: MongoClient,
    val collection: MongoCollection<HistoricData>,
    val query: (processEngine: ProcessEngine) -> List<HistoryEvent>
)