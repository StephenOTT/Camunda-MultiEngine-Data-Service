package com.github.stephenott.camunda.multienginedataservice.moddler

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.graalvm.polyglot.Context
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class ModdlerConverter(
    private val objectMapper: ObjectMapper = jacksonObjectMapper(),
    private val nodeRequireCwd:String = "./moddle-packages"
) {

    //@TODO add check to ensure that moddle should occur: can use a extension property to tell the converter to ignore the bpmn/ignore the resource

    fun convertToTree(moddledMap: Map<String, Any?>): JsonNode {
        return objectMapper.valueToTree(moddledMap)
    }

    fun convertToTreeToMap(jsonNode: JsonNode): Map<String, Any?> {
        return objectMapper.convertValue(jsonNode)
    }

    fun buildProcessDefinitionModdle(moddledBpmn: ModdledBpmn, processDefinitionKey: String): Map<String, Any?>?{
           return if (moddledBpmn != null) {
                val tree = convertToTree(moddledBpmn.value)
                //@TODO add error handling:
                val processObject: ObjectNode =
                    tree["rootElement"]["rootElements"].single { it["id"].textValue() == processDefinitionKey } as ObjectNode

               //@TODO write a unit test for large BPMNs to see what happens
                processObject.replace("flowElements",
                    convertToTree(
                        processObject["flowElements"].map { node ->
                            tree["elementsById"].get(node["id"].asText())
                        }.associateBy {
                            it["id"].asText()
                        }
                    )
                )

                convertToTreeToMap(processObject)
            } else {
                null
            }
    }

//    fun buildDecisionDefinitionModdle(moddledDmn: ModdledBpmn, processDefinitionKey: String): Map<String, Any?>?{
//        return if (moddledDmn != null) {
//            val tree = convertToTree(moddledDmn.value)
//            //@TODO add error handling:
//            val processObject: ObjectNode =
//                tree["rootElement"]["rootElements"].single { it["id"].textValue() == processDefinitionKey } as ObjectNode
//
//            //@TODO write a unit test for large BPMNs to see what happens
//            processObject.replace("flowElements",
//                convertToTree(
//                    processObject["flowElements"].map { node ->
//                        tree["elementsById"].get(node["id"].asText())
//                    }.associateBy {
//                        it["id"].asText()
//                    }
//                )
//            )
//
//            convertToTreeToMap(processObject)
//        } else {
//            null
//        }
//    }

    fun convertBpmnXml(xml: String, maxParseTime: Duration = Duration.ofSeconds(10), nodeRequireCwd:String = this.nodeRequireCwd): ModdledBpmn {

        val languageId = "js"

        val options: Map<String, String> = mapOf(
            "js.commonjs-require" to "true",
            "js.commonjs-require-cwd" to nodeRequireCwd
        )

        val cx = Context.newBuilder(languageId)
            .allowAllAccess(true) //@TODO Review - this is required to make the js work atm
            .options(options)
            .build()

        val jsSource = """
                (async function(xmlStr) {
                    const moddle = require('bpmn-moddle')()
                    let result = await moddle.fromXML(xmlStr)
                    return JSON.stringify(result)
                })
        """.trimIndent()

            val bpmnEvalCf = CompletableFuture<String>()

            cx.eval("js", jsSource)
                .execute(xml)
                .invokeMember("catch", Consumer<Throwable> { bpmnEvalCf.completeExceptionally(it) })
                .invokeMember("then", Consumer<String> { bpmnEvalCf.complete(it) })

            //@TODO add handling for the complete Exceptionally to be used/caught: currently it would not be caught
            val resultString: String = kotlin.runCatching { bpmnEvalCf.get(maxParseTime.toMillis(), TimeUnit.MILLISECONDS) }.getOrElse {
                cx.close(true) //@TODO review to ensure that cx is in a different thread
                throw it
            }

            return ModdledBpmn(objectMapper.readValue(resultString))
    }

//    fun convertDmnXml(xml: String, maxParseTime: Duration = Duration.ofSeconds(10), nodeRequireCwd:String = this.nodeRequireCwd): ModdledBpmn {
// //DOES NOT WORK... WAITING FOR DMN-MODDLE to use MODDLE-XML@9x
//        val languageId = "js"
//
//        val options: Map<String, String> = mapOf(
//            "js.commonjs-require" to "true",
//            "js.commonjs-require-cwd" to nodeRequireCwd
//        )
//
//        val cx = Context.newBuilder(languageId)
//            .allowAllAccess(true) //@TODO Review - this is required to make the js work atm
//            .options(options)
//            .build()
//
//        val jsSource = """
//                (async function(xmlStr) {
//                    const moddle = require('dmn-moddle')()
//                    let result = await moddle.fromXML(xmlStr, 'dmn:Definitions')
//                    return JSON.stringify(result)
//                })
//        """.trimIndent()
//
//        val dmnEvalCf = CompletableFuture<String>()
//
//        cx.eval("js", jsSource)
//            .execute(xml)
//            .invokeMember("catch", Consumer<Any> { dmnEvalCf.completeExceptionally(IllegalStateException(it.toString())) })
//            .invokeMember("then", Consumer<String> { dmnEvalCf.complete(it) })
//
//        val resultString: String = kotlin.runCatching { dmnEvalCf.get(maxParseTime.toMillis(), TimeUnit.MILLISECONDS) }.getOrElse {
//            cx.close(true) //@TODO review to ensure that cx is in a different thread
//            throw it
//        }
//
//        return ModdledBpmn(objectMapper.readValue(resultString))
//    }
}