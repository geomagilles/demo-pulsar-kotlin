package me.gilles.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.schema.SchemaDefinition
import org.apache.pulsar.client.api.schema.SchemaReader
import org.apache.pulsar.client.api.schema.SchemaWriter
import java.io.InputStream

@Serializable
class LoanEventEnvelope(
    val loanApplied: LoanApplied? = null,
    val loanAccepted: LoanAccepted? = null,
    val loanDeclined: LoanDeclined? = null
) {
    init {
        require(notnull().size == 1)
    }

    fun message() = notnull().first()

    private fun notnull() = listOfNotNull(
        loanApplied,
        loanAccepted,
        loanDeclined
    )

    companion object {
        fun from(event: LoanEvent) = when (event) {
            is LoanApplied -> LoanEventEnvelope(loanApplied = event)
            is LoanAccepted -> LoanEventEnvelope(loanAccepted = event)
            is LoanDeclined -> LoanEventEnvelope(loanDeclined = event)
        }
    }
}

internal fun loanEventSchema() = Schema.JSON<LoanEventEnvelope>(
    SchemaDefinition.builder<LoanEventEnvelope>()
        .withJsonDef(Schema.JSON(LoanEventEnvelope::class.java).schemaInfo.schemaDefinition)
        .withSchemaReader(LoanEventReader)
        .withSchemaWriter(LoanEventWriter)
        .withSupportSchemaVersioning(true)
        .build()
)

internal object LoanEventWriter : SchemaWriter<LoanEventEnvelope> {
    override fun write(message: LoanEventEnvelope) =
        Json.encodeToString(LoanEventEnvelope.serializer(), message).toByteArray()
}

internal object LoanEventReader : SchemaReader<LoanEventEnvelope> {
    override fun read(inputStream: InputStream) =
        Json.decodeFromString(LoanEventEnvelope.serializer(), String(inputStream.readBytes()))

    override fun read(bytes: ByteArray, offset: Int, length: Int) =
        read(bytes.inputStream(offset, length))
}

fun main() {
    println(Schema.JSON(LoanEventEnvelope::class.java).schemaInfo)
}