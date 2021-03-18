package com.github.stephenott.camunda.multienginedataservice.security.storage

import java.time.Instant

data class RoleDataAccessRule(
    val _id: String? = null,
    val collection: String,
    val role: String,
    val name: String? = null,
//    val canInsert: Boolean? = null,
//    val canUpdate: Boolean? = null,
//    val canDelete: Boolean? = null,
//    val canSearch: Boolean? = null,
//    val canRead: Boolean? = null,
    val accessSchema: Map<String, Any?>? = null,
    val description: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)

