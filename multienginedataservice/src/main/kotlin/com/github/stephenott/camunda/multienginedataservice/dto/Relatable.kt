package com.github.stephenott.camunda.multienginedataservice.dto

import com.fasterxml.jackson.annotation.JsonProperty

interface Relatable {
    @get:JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val relations: Relations?
}