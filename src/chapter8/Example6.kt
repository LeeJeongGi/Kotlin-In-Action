package chapter8

data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)

enum class OS {
    Windows, MacOS, Linux
}

fun List<SiteVisit>.averageDurationFor(os: OS) =
    filter { it.os == os }.map(SiteVisit::duration).average()

fun List<SiteVisit>.averageDurationFor2(predicate: (SiteVisit) -> Boolean) =
    filter(predicate).map(SiteVisit::duration).average()

fun main() {
    val log = listOf(
        SiteVisit("/", 30.0, OS.Windows),
        SiteVisit("/", 31.0, OS.MacOS),
        SiteVisit("/", 32.0, OS.Linux),
        SiteVisit("/", 33.0, OS.Linux),
        SiteVisit("/signup", 34.0, OS.Linux),
    )

    val average = log
        .filter { it.os == OS.Linux }
        .map(SiteVisit::duration)
        .average()
    println("average: $average")

    val averageDuration = log.averageDurationFor(OS.MacOS)
    println("average: $averageDuration")

    val averageDuration2 = log.averageDurationFor2{
        it.os == OS.Linux && it.path == "/signup"
    }

    println("averageDuration2: $averageDuration2")
}