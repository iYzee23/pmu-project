package com.example.rad.similarity

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class RawAlgorithmComparison(
    val algorithm_name: String,
    val efficiency_time_comparison: String,
    val efficiency_space_comparison: String,
    val implementation_comparison: String,
    val use_cases_comparison: String,
    val conclusion: String
)

@Serializable
data class RawImprovementSuggestions(
    val improvement_suggestions: List<String>,
    val conclusion: String
)

fun parseAlgorithmComparisonsJson(input: String): Pair<List<AlgorithmComparison>, ImprovementSuggestions?> {
    val json = Json { ignoreUnknownKeys = true }
    val jsonArray = json.parseToJsonElement(input).jsonArray

    val algorithmComparisons = mutableListOf<AlgorithmComparison>()
    var improvementSuggestions: ImprovementSuggestions? = null

    jsonArray.forEach { jsonElement ->
        val jsonObject = jsonElement.jsonObject
        if ("algorithm_name" in jsonObject) {
            val rawComparison = json.decodeFromJsonElement<RawAlgorithmComparison>(jsonObject)
            algorithmComparisons.add(
                AlgorithmComparison(
                    algorithmName = rawComparison.algorithm_name,
                    efficiencyTimeComparison = rawComparison.efficiency_time_comparison,
                    efficiencySpaceComparison = rawComparison.efficiency_space_comparison,
                    implementationComparison = rawComparison.implementation_comparison,
                    useCasesComparison = rawComparison.use_cases_comparison,
                    conclusion = rawComparison.conclusion
                )
            )
        } else if ("improvement_suggestions" in jsonObject) {
            val rawSuggestions = json.decodeFromJsonElement<RawImprovementSuggestions>(jsonObject)
            improvementSuggestions = ImprovementSuggestions(
                suggestions = rawSuggestions.improvement_suggestions,
                conclusion = rawSuggestions.conclusion
            )
        }
    }

    return Pair(algorithmComparisons, improvementSuggestions)
}