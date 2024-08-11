package com.example.rad.complexity

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class RawAlgorithmInfo(
    val intro: String,
    val time_complexity: String,
    val space_complexity: String,
    val insights: String,
    val conclusion: String
)

fun parseAlgorithmInfoJson(input: String): AlgorithmInfo {
    val json = Json { ignoreUnknownKeys = true }
    val rawInfo = json.decodeFromString<RawAlgorithmInfo>(input)

    return AlgorithmInfo(
        intro = rawInfo.intro,
        timeComplexity = rawInfo.time_complexity,
        spaceComplexity = rawInfo.space_complexity,
        insights = rawInfo.insights,
        conclusion = rawInfo.conclusion
    )
}
