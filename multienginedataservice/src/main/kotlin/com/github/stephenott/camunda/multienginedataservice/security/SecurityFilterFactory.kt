package com.github.stephenott.camunda.multienginedataservice.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.stephenott.camunda.multienginedataservice.dto.*
import org.bson.BsonDocument
import org.bson.conversions.Bson

class SecurityFilterFactory(
    private val jsonMapper: ObjectMapper = jacksonObjectMapper()
) {

    fun customSecurityFilter(filter: Map<String, Any?>): Bson {
        return BsonDocument.parse(jsonMapper.writeValueAsString(filter))
    }

    fun customSecurityFilter(filter: String): Bson {
        return BsonDocument.parse(filter)
    }

    fun historicProcessInstanceSecurityFilter(
        operator: ConditionOperator = ConditionOperator.OR,
        processInstanceIds: List<String>? = null,
        processDefinitionIds: List<String>? = null,
        processDefinitionKeys: List<String>? = null,
        processDefinitionKeyPattern: String? = null,
        startUserIds: List<String>? = null,
        startUserIdPattern: String? = null,
        businessKeys: List<String>? = null,
        businessKeyPattern: String? = null
    ): String {

        val conditions: MutableList<Map<String, Any?>> = mutableListOf()


        processInstanceIds?.let {
            conditions.add(propertiesWithEnum(HistoricProcessInstanceDto::id.name, it))
        }

        processDefinitionIds?.let {
            conditions.add(propertiesWithEnum(HistoricProcessInstanceDto::processDefinitionId.name, it))
        }

        processDefinitionKeys?.let {
            conditions.add(propertiesWithEnum(HistoricProcessInstanceDto::processDefinitionKey.name, it))
        }

        processDefinitionKeyPattern?.let {
            conditions.add(propertiesWithPattern(HistoricProcessInstanceDto::processDefinitionKey.name, it))
        }

        startUserIds?.let {
            conditions.add(propertiesWithEnum(HistoricProcessInstanceDto::startUserId.name, it))
        }

        startUserIdPattern?.let {
            conditions.add(propertiesWithPattern(HistoricProcessInstanceDto::startUserId.name, it))
        }

        businessKeys?.let {
            conditions.add(propertiesWithEnum(HistoricProcessInstanceDto::businessKey.name, it))
        }

        businessKeyPattern?.let {
            conditions.add(propertiesWithPattern(HistoricProcessInstanceDto::businessKey.name, it))
        }

        val wrapper = when (operator) {
            ConditionOperator.OR -> {
                anyOf(conditions)
            }
            ConditionOperator.AND -> {
                allOf(conditions)
            }
            else -> {
                throw IllegalArgumentException("Unexpected condition operator.")
            }
        }

        return jsonMapper.writeValueAsString(wrapper)
    }

    enum class ConditionOperator{
        OR, AND
    }

    private fun propertiesWithEnum(propertyName: String, enumValues: List<String>): Map<String, Any?> {
        return mapOf(
            "properties" to mapOf(
                propertyName to mapOf(
                    "enum" to enumValues
                )
            )
        )
    }

    private fun propertiesWithPattern(propertyName: String, pattern: String): Map<String, Any?> {
        return mapOf(
            "properties" to mapOf(
                propertyName to mapOf(
                    "pattern" to pattern
                )
            )
        )
    }

    private fun anyOf(conditions: MutableList<Map<String, Any?>>): Map<String, List<Map<String, Any?>>> {
        return mapOf("anyOf" to conditions)
    }

    private fun allOf(conditions: MutableList<Map<String, Any?>>): Map<String, List<Map<String, Any?>>> {
        return mapOf("allOf" to conditions)
    }

}