import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AzureResponse(
    @SerialName("Results") val results: Results
)

@Serializable
data class Results(
    @SerialName("output1") val output1: Output
)

@Serializable
data class Output(
    @SerialName("type") val type: String,
    @SerialName("value") val value: Value
)

@Serializable
data class Value(
    @SerialName("ColumnNames") val columnNames: List<String>,
    @SerialName("ColumnTypes") val columnTypes: List<String>,
    @SerialName("Values") val values: List<List<String>>
)