package com.github.stephenott.camunda.multienginedataservice.index

import com.github.stephenott.camunda.multienginedataservice.dto.*
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.CreateIndexOptions
import com.mongodb.client.model.IndexModel

class IndexFactory {

    fun generateHistoricProcessInstanceIndexes(collection: MongoCollection<HistoricProcessInstanceDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricActivityInstanceIndexes(collection: MongoCollection<HistoricActivityInstanceDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricDecisionInstanceIndexes(collection: MongoCollection<HistoricDecisionInstanceDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricDetailIndexes(collection: MongoCollection<HistoricDetail>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricExternalTaskInstanceIndexes(collection: MongoCollection<HistoricExternalTaskInstanceDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricExternalTaskLogIndexes(collection: MongoCollection<HistoricExternalTaskLogDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricIdentityLinkInstanceInstanceIndexes(collection: MongoCollection<HistoricIdentityLinkInstanceDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricIdentityLinkLogInstanceIndexes(collection: MongoCollection<HistoricIdentityLinkLogDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricIncidentIndexes(collection: MongoCollection<HistoricIncidentDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricJobLogIndexes(collection: MongoCollection<HistoricJobLogDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricTaskInstanceInstanceIndexes(collection: MongoCollection<HistoricTaskInstanceDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricUserOperationLogEntryIndexes(collection: MongoCollection<HistoricUserOperationLogEntryDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }

    fun generateHistoricVariableInstanceIndexes(collection: MongoCollection<HistoricVariableInstanceDto>, models: List<IndexModel>, options: CreateIndexOptions = CreateIndexOptions()) {
        collection.createIndexes(models, options)
    }
}