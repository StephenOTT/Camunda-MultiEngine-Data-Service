package com.github.stephenott.camunda.multienginedataservice.dto

import com.fasterxml.jackson.annotation.JsonProperty

interface EventTyped {

    @get:JsonProperty("@type")
    val EVENT_TYPE: String

}