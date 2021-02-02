import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AzureRequest(
    @SerialName("Inputs") val inputs: Inputs
) {
    companion object {
        /**
         * Create Azure Request from params
         * */
        fun create(
            cpu: String,
            ghz: String,
            gpu: String,
            ram: String,
            ramType: String,
            screen: String,
            storage: String,
            ssd: Boolean,
            weight: String
        ): AzureRequest {
            val columnNames = with(AzureRequestColumnNames) {
                listOf(CPU, GHz, GPU, RAM, RAMType, Screen, Storage, SSD, Weight)
            }
            val values = listOf(cpu, ghz, gpu, ram, ramType, screen, storage, ssd.toString(), weight)

            return AzureRequest(Inputs(Input(columnNames, listOf(values))))
        }
    }
}

@Serializable
data class Inputs(
    @SerialName("input1") val input1: Input
)

@Serializable
data class Input(
    @SerialName("ColumnNames") var columnNames: List<String>,
    @SerialName("Values") var values: List<List<String>>
)

/**
 * Constant names of columns in request
 * */
object AzureRequestColumnNames {
    const val CPU = "CPU"
    const val GHz = "GHz"
    const val GPU = "GPU"
    const val RAM = "RAM"
    const val RAMType = "RAMType"
    const val Screen = "Screen"
    const val Storage = "Storage"
    const val SSD = "SSD"
    const val Weight = "Weight"
}