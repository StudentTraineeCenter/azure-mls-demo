/**
 * @author: Jakub Ferenčík, Microsoft STC
 * @see: [GitHub Repository](https://github.com/MicrosoftSTC/azure-mls-demo.git)
 * */
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitStringResult
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    val request = askForParams()
    runBlocking {
        makeRequestToAzure(request)
    }.also { it?.printResponse() }


    println("Try again? (yes, no)")
    if (readLine() == "yes")
        main(emptyArray())
}

/**
 * Prints response data to Console
 * */
fun AzureResponse.printResponse() {
    val resNames = this.results.output1.value.columnNames
    val resTypes = this.results.output1.value.columnTypes
    val resValues = this.results.output1.value.values[0]

    println("\n\n---------------------------")
    println("Response:")
    println(jsonFormat.encodeToString(this)) // Print JSON formatted result
    println("\n\n---------------------------")
    resValues.forEachIndexed { i, it ->
        if (i == 9) {
            println("=====")
            println("${resNames[i]} (${resTypes[i]}): € $it")
        } else {
            println("${resNames[i]} (${resTypes[i]}): $it")
        }
    }
    println("\n\n---------------------------")
}

/**
 * Makes HTTP Post request to defined URL, receives response and prints error message if failed.
 * @param request Azure Request object with data from user
 * @return Azure Response object with data from Azure ML Studio if success, or null if failure
 * */
suspend fun makeRequestToAzure(request: AzureRequest): AzureResponse? {
    Fuel.post(azureUrl) // HTTP Post request
        .authentication().bearer(apiToken) // Authenticate request
        .jsonBody(jsonFormat.encodeToString(request)) // Append JSON body
        // .also { println(it) } // Print request info
        .also { println("Sending request to ${it.url}...") }
        .awaitStringResult() // Send request & suspend fun
        .fold({ data ->
            return jsonFormat.decodeFromString<AzureResponse>(data)
        }, { err ->
            println("Error of type ${err.exception}>> ${err.message}")
        })
    return null
}

/**
 * Asks user for input parameters via Console.
 * @return Azure Request object with entered data
 * */
fun askForParams(): AzureRequest {
    fun cpu(): String {
        println("Select CPU (i3, i5, i7):")
        val cpu = readLine()
        if (cpu.isNullOrBlank() or !listOf("i3", "i5", "i7").contains(cpu)) {
            println("Invalid value! Try again...")
            return cpu()
        }
        return cpu!!
    }

    fun ghz(): String {
        println("Enter CPU speed [GHz] (number):")
        val ghz = readLine()
        if (ghz?.toDoubleOrNull() == null) {
            println("Invalid value! Try again...")
            return ghz()
        }
        return ghz
    }

    fun gpu(): String {
        println("Select GPU (intel, nvidia, amd):")
        val gpu = readLine()
        if (gpu.isNullOrBlank() or !listOf("intel", "nvidia", "amd").contains(gpu)) {
            println("Invalid value! Try again...")
            return gpu()
        }
        return gpu!!
    }

    fun ram(): String {
        println("Enter RAM size [gb] (number):")
        val ram = readLine()
        if (ram?.toDoubleOrNull() == null) {
            println("Invalid value! Try again...")
            return ram()
        }
        return ram
    }

    fun ramType(): String {
        println("Select RAM type (ddr3, ddr4):")
        val ramType = readLine()
        if (ramType.isNullOrBlank() or !listOf("ddr3", "ddr4").contains(ramType)) {
            println("Invalid value! Try again...")
            return ramType()
        }
        return ramType!!
    }

    fun screen(): String {
        println("Enter screen size [in] (number):")
        val screen = readLine()
        if (screen?.toDoubleOrNull() == null) {
            println("Invalid value! Try again...")
            return screen()
        }
        return screen
    }

    fun storage(): String {
        println("Enter storage size [gb] (number):")
        val storage = readLine()
        if (storage?.toDoubleOrNull() == null) {
            println("Invalid value! Try again...")
            return storage()
        }
        return storage
    }

    fun ssd(): Boolean {
        println("Is storage SSD (true, false):")
        val ssd = readLine()
        if (ssd.isNullOrBlank() or !listOf("true", "false").contains(ssd)) {
            println("Invalid value! Try again...")
            return ssd()
        }
        return ssd!!.toBoolean()
    }

    fun weight(): String {
        println("Enter weight [kg] (number):")
        val weight = readLine()
        if (weight?.toDoubleOrNull() == null) {
            println("Invalid value! Try again...")
            return weight()
        }
        return weight
    }

    return AzureRequest.create(cpu(), ghz(), gpu(), ram(), ramType(), screen(), storage(), ssd(), weight())
}
/**
 * Json Format
 * */
val jsonFormat = Json { prettyPrint = true }

////////////////////////////////////////////////////////////////

/**
 * Azure ML Studio > Web Service > HTTP Request/Response > URL
 * */
const val azureUrl =
    "<url of your web service>"

/**
 * Azure ML Studio > Web Service > API token
 * */
const val apiToken = "<your api token>"