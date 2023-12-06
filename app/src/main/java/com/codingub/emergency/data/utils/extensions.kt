package com.codingub.emergency.data.utils

fun String.isSimilar(alt: String): Boolean {
    if (alt.isBlank()) return false

    val n = this.length
    val m = alt.length

    val dp = Array(n + 1) { IntArray(m + 1) }
    for (i in 0..n) {
        for (j in 0..m) {
            if (i == 0) {
                dp[i][j] = j
            } else if (j == 0) {
                dp[i][j] = i
            } else if (this[i - 1] == alt[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1]
            } else {
                dp[i][j] = 1 + minOf(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1])
            }
        }
    }
    val levenshteinDistance = dp[n][m]
    val similarityThreshold = 2
    return levenshteinDistance <= similarityThreshold
}