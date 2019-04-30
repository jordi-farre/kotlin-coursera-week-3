package nicestring

fun Char.isVowel(): Boolean {
    return "aeiou".any { it == this }
}

fun String.isNice(): Boolean {
    val containsInvalidSubstringsScore = if (this.contains("b[a,e,u]".toRegex())) 0 else 1
    val threeVowelsScore = if (this.count { it.isVowel() } >= 3) 1 else 0
    val doubleLetterScore = if (this.zipWithNext().any { it.first == it.second }) 1 else 0
    return (containsInvalidSubstringsScore + threeVowelsScore + doubleLetterScore) >= 2
}