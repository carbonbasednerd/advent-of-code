import java.io.File
import java.math.BigInteger
import kotlin.math.abs

enum class Direction {
    NORTH, EAST, SOUTH, WEST
}

val compass = listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)

val navigationData = mutableListOf<Pair<Char, Int>>()

fun readNavigationData(fileName: String) {
    File(fileName).forEachLine {
        navigationData.add(it[0] to it.substring(1).toInt())
    }
}

fun trackMoves(): Int {
    var ns = 0
    var ew = 0
    var direction = Direction.EAST
    navigationData.forEach {
        val result = move(ns, ew, direction, it.first, it.second)
        ns = result.first
        ew = result.second
        direction = result.third
    }
    return abs(ns) + abs(ew)
}

fun move(ns: Int, ew: Int, direction: Direction, command: Char, value: Int): Triple<Int, Int, Direction> {
    var tempNS = ns
    var tempEW = ew
    var tempDirection = direction
    when (command) {
        'N', 'S' -> {
            tempNS = motorNS(command, value, ns)
        }
        'E', 'W' -> {
            tempEW = motorEW(command, value, ew)
        }
        'L', 'R' -> {
            tempDirection = rotate(direction, command, value)
        }
        'F' -> {
            val result = motorForward(direction, value, ns, ew)
            tempNS = result.first
            tempEW = result.second
        }
    }
    return Triple(tempNS, tempEW, tempDirection)
}

fun motorForward(direction: Direction, value: Int, ns: Int, ew: Int): Pair<Int, Int> {
    var tempNS = ns
    var tempEW = ew

    when (direction) {
        Direction.NORTH -> {
            tempNS = motorNS('N', value, ns)
        }
        Direction.SOUTH -> {
            tempNS = motorNS('S', value, ns)
        }
        Direction.EAST -> {
            tempEW = motorEW('E', value, ew)
        }
        Direction.WEST -> {
            tempEW = motorEW('W', value, ew)
        }
    }
    return tempNS to tempEW
}

fun motorNS(command: Char, value: Int, ns: Int): Int {
    var output = 0
    when(command) {
        'N'-> { output = ns + value}
        'S'->{ output =  ns - value}
    }
    return output
}

fun motorEW(command: Char, value: Int, ew: Int): Int {
    var output = 0
    when(command) {
        'E'-> { output = ew + value}
        'W'-> { output =  ew - value}
    }
    return output
}

fun rotate(direction: Direction, command: Char, value: Int): Direction {
    if (value == 0) return direction

    val x = value / 90
    var mod = 0
    var ordinal = direction.ordinal
    if (command == 'R') {
        ordinal += x
        mod = ordinal % 4
    } else if (command == 'L') {
        ordinal -= x
        mod = ordinal % 4
        if (mod < 0) {
            mod = compass.size + mod
        }
    }

    return compass[mod]

}

var shipNS = 0
var shipEW = 0
var waypointNS = 1
var waypointEW = 10

// part two - got some free time? combine part 1 and 2
fun trackMovesAdvanced(): Int {
    navigationData.forEach {
        moveAdvanced(it.first, it.second)
    }
    return abs(shipNS) + abs(shipEW)
}

fun moveAdvanced(command: Char, value: Int) {
    when (command) {
        'N','S' -> {
            advancedMotorNS(command, value)
        }
        'E', 'W' -> {
            advancedMotorEW(command, value)
        }
        'L', 'R' -> {
            advancedRotate(command, value)
        }
        'F' -> {
            advancedMotorForward(value)
        }
    }
}

fun advancedMotorNS(command: Char, value: Int) {
    val mod = if (command == 'N') 1 else -1
    waypointNS += value * mod
}

fun advancedMotorEW(command: Char, value: Int) {
    val mod = if (command == 'E') 1 else -1
    waypointEW += value * mod
}

fun advancedMotorForward(value: Int) {
    shipNS += value * waypointNS
    shipEW += value * waypointEW
}

fun advancedRotate(command: Char, value: Int) {
    if (value == 0) return

    //cheating - saw the data didn't have anything over 270
    if (value == 180) {
        waypointNS *= -1
        waypointEW *= -1
    }
    else if (value == 270) {
        if (command == 'R') {
            val tempEW = waypointNS * -1
            val tempNS = waypointEW
            waypointNS = tempNS
            waypointEW = tempEW
        } else {
            val tempEW = waypointNS
            val tempNS = waypointEW * -1
            waypointNS = tempNS
            waypointEW = tempEW
        }
    }
    else if (value == 90) {
        if (command == 'L') {
            val tempEW = waypointNS * -1
            val tempNS = waypointEW
            waypointNS = tempNS
            waypointEW = tempEW
        } else {
            val tempEW = waypointNS
            val tempNS = waypointEW * -1
            waypointNS = tempNS
            waypointEW = tempEW
        }
    }
    else {
        println("UNKNOWN $command $value")
    }
}

fun testRotations() {
    var direction = rotate(Direction.EAST, 'R', 360)
    direction = rotate(Direction.EAST, 'L', 360)
    direction = rotate(Direction.NORTH, 'R', 360)
    direction = rotate(Direction.NORTH, 'L', 360)
    direction = rotate(Direction.WEST, 'R', 360)
    direction = rotate(Direction.WEST, 'L', 360)
    direction = rotate(Direction.SOUTH, 'R', 360)
    direction = rotate(Direction.SOUTH, 'L', 360)

    direction = rotate(Direction.EAST, 'R', 90)
    direction = rotate(Direction.EAST, 'L', 90)
    direction = rotate(Direction.NORTH, 'R', 90)
    direction = rotate(Direction.NORTH, 'L', 90)
    direction = rotate(Direction.WEST, 'R', 90)
    direction = rotate(Direction.WEST, 'L', 90)
    direction = rotate(Direction.SOUTH, 'R', 90)
    direction = rotate(Direction.SOUTH, 'L', 90)

    direction = rotate(Direction.EAST, 'R', 180)
    direction = rotate(Direction.EAST, 'L', 180)
    direction = rotate(Direction.NORTH, 'R', 180)
    direction = rotate(Direction.NORTH, 'L', 180)
    direction = rotate(Direction.WEST, 'R', 180)
    direction = rotate(Direction.WEST, 'L', 180)
    direction = rotate(Direction.SOUTH, 'R', 180)
    direction = rotate(Direction.SOUTH, 'L', 180)
}

// too high 1911171224 too high 914935576
fun main() {
    readNavigationData("data/data_day12")
    println("Final distance is ${trackMoves()}")
//    testRotations()
    println("Final distance is ${trackMovesAdvanced()}")
}