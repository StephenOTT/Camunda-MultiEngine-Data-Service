package com.github.stephenott.camunda.multienginedataservice.security

import org.bson.conversions.Bson
import org.litote.kmongo.bson
import org.litote.kmongo.jsonSchema

data class SecurityFilter(
    /**
     * Inner content of of a `$match` command.
     * use `.wrappedInMatch()` to wrap the content in a `$match` command.
     */
    val matchContent: Bson
){

    companion object {
        /**
         * Creates `$jsonSchema` object with schema within
         */
        fun fromJsonSchema(schema: Bson): SecurityFilter{
            return SecurityFilter(jsonSchema(schema))
        }

        /**
         * Creates `$jsonSchema` object with schema within
         */
        fun fromJsonSchema(schema: String): SecurityFilter{
            return SecurityFilter(jsonSchema(schema.bson))
        }
    }
}

