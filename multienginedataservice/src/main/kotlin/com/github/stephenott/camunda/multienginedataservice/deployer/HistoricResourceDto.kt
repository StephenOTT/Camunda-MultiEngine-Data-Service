package com.github.stephenott.camunda.multienginedataservice.deployer

import com.github.stephenott.camunda.multienginedataservice.dto.HistoricData
import com.github.stephenott.camunda.multienginedataservice.dto.EngineMetadata
import java.util.*

data class HistoricResourceDto(
    override val _id: String? = null,
    override val id: String? = null,
    val name: String? = null,
    val createTime: Date? = null,
    val isGenerated: Boolean? = null,
    override val tenantId: String? = null,
    val type: Int? = null,
    val deploymentId: String? = null,
    val bytes: ByteArray? = null,
    val moddle: Map<String, Any?>? = null,
    override val metadata: EngineMetadata? = null
// file size, file extension, process name, process key, process id, number of process definitions (when multiple pools), is executable, other resource types (like json files)

    ): HistoricData {

    override val EVENT_TYPE: String = "historicDeploymentResource"


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistoricResourceDto

        if (_id != other._id) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (createTime != other.createTime) return false
        if (isGenerated != other.isGenerated) return false
        if (tenantId != other.tenantId) return false
        if (type != other.type) return false
        if (deploymentId != other.deploymentId) return false
        if (bytes != null) {
            if (other.bytes == null) return false
            if (!bytes.contentEquals(other.bytes)) return false
        } else if (other.bytes != null) return false
        if (moddle != other.moddle) return false
        if (metadata != other.metadata) return false
        if (EVENT_TYPE != other.EVENT_TYPE) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id?.hashCode() ?: 0
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (createTime?.hashCode() ?: 0)
        result = 31 * result + (isGenerated?.hashCode() ?: 0)
        result = 31 * result + (tenantId?.hashCode() ?: 0)
        result = 31 * result + (type ?: 0)
        result = 31 * result + (deploymentId?.hashCode() ?: 0)
        result = 31 * result + (bytes?.contentHashCode() ?: 0)
        result = 31 * result + (moddle?.hashCode() ?: 0)
        result = 31 * result + (metadata?.hashCode() ?: 0)
        result = 31 * result + EVENT_TYPE.hashCode()
        return result
    }


}
