package com.example.final_project.data.remote.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

object StringOrListSerializer : KSerializer<String?> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StringOrList", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String? {
        val input = decoder as? JsonDecoder ?: return decoder.decodeString()
        val element = input.decodeJsonElement()

        return when (element) {
            is JsonArray -> element.joinToString(" / ") { it.jsonPrimitive.content }
            is JsonPrimitive -> element.content
            else -> null
        }
    }

    override fun serialize(encoder: Encoder, value: String?) {
        encoder.encodeString(value ?: "")
    }
}
@Serializable
data class GlossDto(
    @SerialName("S")
    @Serializable(with = StringOrListSerializer::class)
    val subject: String? = null,

    @SerialName("V")
    @Serializable(with = StringOrListSerializer::class)
    val verb: String? = null,

    @SerialName("O")
    @Serializable(with = StringOrListSerializer::class)
    val obj: String? = null,

    @SerialName("TIME")
    @Serializable(with = StringOrListSerializer::class)
    val time: String? = null,

    @SerialName("PLACE")
    @Serializable(with = StringOrListSerializer::class)
    val place: String? = null,
)
