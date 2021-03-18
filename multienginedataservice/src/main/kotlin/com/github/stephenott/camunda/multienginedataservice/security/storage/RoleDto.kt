package com.github.stephenott.camunda.multienginedataservice.security.storage

data class RoleDto(
    val _id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val priority: Int? = null,
    val dataAccessRules: List<RoleDataAccessRule>? = null,
){
}