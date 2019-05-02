package taxipark

import kotlin.math.roundToInt

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
       this.allDrivers.filter { driver ->
           this.trips.none { trip -> trip.driver == driver }
       }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        this.allPassengers.filter { passenger ->
            this.trips.count { trip ->
                trip.passengers.contains(passenger)
            } >= minTrips
        }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        this.allPassengers.filter { passenger ->
            this.trips.filter { trip ->
                trip.passengers.contains(passenger)
            }.count { trip ->
                trip.driver == driver
            } > 1
        }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        this.allPassengers.filter { passenger ->
            val (discount, nodiscount) = this.trips.filter { trip ->
                trip.passengers.contains(passenger)
            }.partition { trip ->
                if (trip.discount != null) trip.discount > 0.0 else false
            }
            println(discount.size)
            println(nodiscount.size)
            discount.size > nodiscount.size
        }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    fun Int.lowerBound() = (this/10) * 10
    fun Int.upperBound() = ((this/10) * 10) + 9
    val groupedByDuration = this.trips.groupBy { trip ->
        trip.duration.lowerBound()..trip.duration.upperBound()
    }.maxBy { entry -> entry.value.size }
    return groupedByDuration?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false
    val twentyPercentDrivers = (allDrivers.size * 0.2).roundToInt()
    val totalIncome = this.trips.sumByDouble { it.cost }
    val driversIncomes = this.trips.groupBy { it.driver }.map { entry ->
        entry.key to entry.value.sumByDouble { it.cost }
    }.sortedByDescending { it.second }
    val twentyPercerntDriversIncome = driversIncomes.take(twentyPercentDrivers).sumByDouble { it.second }
    return twentyPercerntDriversIncome >= (totalIncome * 0.8)
}