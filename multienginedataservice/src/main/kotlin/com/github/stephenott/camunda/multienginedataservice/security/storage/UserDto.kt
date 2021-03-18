package com.github.stephenott.camunda.multienginedataservice.security.storage

import java.time.Instant

data class UserDto(
    val _id: String? = null,
    val username: String? = null,
    val password: String? = null,
    val canRoleSelect: Boolean? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
    )