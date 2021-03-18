package com.github.stephenott.camunda.multienginedataservice.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.camunda.spin.impl.json.jackson.JacksonJsonNode

class JacksonJsonNodeSerializer: StdSerializer<JacksonJsonNode>(JacksonJsonNode::class.java) {
    override fun serialize(value: JacksonJsonNode, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeTree(value.unwrap())
    }

    override fun serializeWithType(
        value: JacksonJsonNode,
        gen: JsonGenerator,
        serializers: SerializerProvider,
        typeSer: TypeSerializer
    ) {
        gen.writeTree(value.unwrap())
    }
}