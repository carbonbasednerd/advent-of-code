import java.io.File
import java.lang.Exception

val cubeData = mutableMapOf<Int, MutableList<MutableList<Boolean>>>()
val hyperCubeData = mutableMapOf<Int, MutableMap<Int, MutableList<MutableList<Boolean>>>>()
var width = 0
var height = 0
fun readCubeData(fileName: String) {
    val tempList = mutableListOf<MutableList<Boolean>>()
    File(fileName).forEachLine {
        tempList.add(it.map { state -> state == '#' }.toMutableList())
    }
    cubeData[0] = tempList
    width = cubeData[0]!![0].size
    height = cubeData[0]!!.size
}

//part one
fun expandExistingSpace() {
    cubeData.forEach { dimension ->
        dimension.value.forEach {
            it.add(false)
            it.add(0,false)
        }
        val newList = mutableListOf<Boolean>()
        (1..width).forEach { _ ->
            newList.add(false)
        }
        dimension.value.add(0,newList)
        dimension.value.add(newList)
    }
}

fun initNewSpace(z: Int) {
    val tempSpace = mutableListOf<MutableList<Boolean>>()
    (1..height).forEach { _ ->
        val tempWidth = mutableListOf<Boolean>()
        (1..width).forEach { _ ->
            tempWidth.add(false)
        }
        tempSpace.add(tempWidth)
    }
    cubeData[z] = tempSpace
}
fun localityCheck(x: Int, y: Int, z: Int): Boolean {
    if (x < 0 || x >= height || y<0 || y>= width) {
        return false
    } else {
        return cubeData[z]!![x][y]
    }
}

fun planeCheck(x: Int, y: Int, z: Int, home: Boolean): Int {
    var activeCount = 0
    if (!home) {
        if (localityCheck(x,y,z)) activeCount++
    }
    if(localityCheck(x-1,y,z)) activeCount++
    if(localityCheck(x-1,y-1,z)) activeCount++
    if(localityCheck(x-1,y+1,z)) activeCount++
    if(localityCheck(x+1,y,z)) activeCount++
    if(localityCheck(x+1,y-1,z)) activeCount++
    if(localityCheck(x+1,y+1,z)) activeCount++
    if(localityCheck(x,y-1,z)) activeCount++
    if(localityCheck(x,y+1,z)) activeCount++

    return activeCount
}

fun checkNeighborActivity(x: Int, y: Int, z: Int): Int {
    var activeCount = 0
    // check home plane z = z
    activeCount += planeCheck(x, y, z, true)
    //check negative plane z - 1
    if (cubeData.keys.contains(z-1)) {
       activeCount += planeCheck(x, y, z-1, false)
    }
    //check positive plane z + 1
    if (cubeData.keys.contains(z+1)) {
        activeCount += planeCheck(x, y, z+1, false)
    }
    return activeCount
}

fun registerActivity(): Int {
    var activeCubes = 0
    cubeData.forEach {
        it.value.forEach {i ->
           activeCubes += i.count { x-> x }
        }
    }
    return activeCubes
}

fun printDimension(cycle: Int) {
    println("Cycle $cycle")
    cubeData.keys.sorted().forEach {
        println("z=$it")
        printSpace(it)
        println("\n")
    }
    println("\n==============================\n")
}

fun printSpace(z: Int) {
    cubeData[z]!!.forEach {
        println(it.map{x-> if(x) "#" else "."}.toString().replace(",",""))
    }
}


fun initCubeSpace(cycles: Int): Int {
    var counter = 1
    printDimension(0)
    while (counter <= cycles) {
        //this logic could be improved!
        height += 2
        width += 2
        expandExistingSpace()
        initNewSpace(counter)
        initNewSpace(counter*-1)


        val tempDimensionMap = mutableMapOf<Int, MutableList<MutableList<Boolean>>>()
        cubeData.forEach {
            val z = it.key
            val tempSpaceList = mutableListOf<MutableList<Boolean>>()
            it.value.forEachIndexed {height, h ->
                val tempCubeList = mutableListOf<Boolean>()
                h.forEachIndexed {width, w ->
                    val nearbyActivity = checkNeighborActivity(height,width,z)
                    when(w) {
                        true -> {
                            if (nearbyActivity in (2..3)){
                                tempCubeList.add(true)
                            } else {
                                tempCubeList.add(false)
                            }
                        }
                        false -> {
                            if (nearbyActivity == 3) {
                                tempCubeList.add(true)
                            } else {
                                tempCubeList.add(false)
                            }
                        }
                    }
                }
                tempSpaceList.add(tempCubeList)
            }
            tempDimensionMap[z] = tempSpaceList
        }
        cubeData.clear()
        cubeData.putAll(tempDimensionMap)
        printDimension(counter)
        counter++

    }
    return registerActivity()
}

//part two
fun readHyperCubeData(fileName: String) {
    val tempList = mutableListOf<MutableList<Boolean>>()
    File(fileName).forEachLine {
        tempList.add(it.map { state -> state == '#' }.toMutableList())
    }
    val map = mutableMapOf<Int, MutableList<MutableList<Boolean>>>()
    map[0] = tempList
    hyperCubeData[0] = map
    width = hyperCubeData[0]!![0]!![0].size
    height = hyperCubeData[0]!![0]!!.size
}

fun expandExistingHyperSpace(count: Int) {
    hyperCubeData.forEach { hd ->
        hd.value.forEach {dim ->
            dim.value.forEach {
                it.add(false)
                it.add(0,false)
            }
            val newList = mutableListOf<Boolean>()
            (1..width).forEach { _ ->
                newList.add(false)
            }
            dim.value.add(0,newList)
            dim.value.add(newList)
        }

        val tempSpace = mutableListOf<MutableList<Boolean>>()
        (1..height).forEach { _ ->
            val tempWidth = mutableListOf<Boolean>()
            (1..width).forEach { _ ->
                tempWidth.add(false)
            }
            tempSpace.add(tempWidth)
        }
        hd.value[count] = tempSpace
        hd.value[count*-1] = tempSpace
    }
}

fun initNewHyperSpace(w: Int, count: Int) {
    val zmap = mutableMapOf<Int, MutableList<MutableList<Boolean>>>()
    ((count*-1)..(count)).forEach {
        val tempSpace = mutableListOf<MutableList<Boolean>>()
        (1..height).forEach { _ ->
            val tempWidth = mutableListOf<Boolean>()
            (1..width).forEach { _ ->
                tempWidth.add(false)
            }
            tempSpace.add(tempWidth)
        }
        zmap[it] = tempSpace
    }

    hyperCubeData[w] = zmap
}

fun checkHyperNeighborActivity(x: Int, y: Int, z: Int, w: Int): Int {
    var activeCount = 0

    (w-1..w+1).forEach { dw->
        (z-1..z+1).forEach { dz ->
            (x-1..x+1).forEach { dx ->
                (y-1..y+1).forEach { dy ->
                    if (!(dx < 0 || dx >= width || dy < 0 || dy >= height) && !(dx == x && dy == y && dz == z && dw == w)) {
                        try {
                            if (hyperCubeData[dw]!![dz]!![dx][dy]) activeCount++
                        } catch(e: Exception) {

                        }
                    }
                }
            }
        }
    }
    return activeCount
}

fun registerHyperActivity(): Int {
    var activeCubes = 0
    hyperCubeData.forEach {w ->
        w.value.forEach{z ->
            z.value.forEach { i ->
                activeCubes += i.count { x-> x }
            }
        }
    }
    return activeCubes
}

fun printHyperDimension(cycle: Int) {
    println("Cycle $cycle")
    hyperCubeData.keys.sorted().forEach { w ->
        hyperCubeData[w]!!.keys.sorted().forEach { z ->
            println("w=$w : z=$z")
            printHyperSpace(z, w)
            println("\n")
        }
    }
    println("\n==============================\n")
}

fun printHyperSpace(z: Int, w: Int) {
    hyperCubeData[w]!![z]!!.forEach {
        println(it.map{x-> if(x) "#" else "."}.toString().replace(",",""))
    }
}


// I don't like this solution. Too complicated.
fun initHyperCubeSpace(cycles: Int): Int {
    var counter = 1
    printHyperDimension(0)
    while (counter <= cycles) {
        height += 2
        width += 2
        expandExistingHyperSpace(counter)
        initNewHyperSpace(counter,counter)
        initNewHyperSpace(counter*-1, counter)

        val tempHyperDimensionMap = mutableMapOf<Int, MutableMap<Int, MutableList<MutableList<Boolean>>>>()
        hyperCubeData.forEach { dimw->
            val hyperList = mutableMapOf<Int, MutableList<MutableList<Boolean>>>()
            dimw.value.forEach {z ->
                val tempSpaceList = mutableListOf<MutableList<Boolean>>()
                z.value.forEachIndexed {height, h ->
                    val tempCubeList = mutableListOf<Boolean>()
                    h.forEachIndexed {width, w ->
                        val nearbyActivity = checkHyperNeighborActivity(height,width,z.key,dimw.key)
                        when(w) {
                            true -> {
                                if (nearbyActivity in (2..3)){
                                    tempCubeList.add(true)
                                } else {
                                    tempCubeList.add(false)
                                }
                            }
                            false -> {
                                if (nearbyActivity == 3) {
                                    tempCubeList.add(true)
                                } else {
                                    tempCubeList.add(false)
                                }
                            }
                        }
                    }
                    tempSpaceList.add(tempCubeList)
                }
                hyperList[z.key] = tempSpaceList
            }
            tempHyperDimensionMap[dimw.key] = hyperList
        }

        hyperCubeData.clear()
        hyperCubeData.putAll(tempHyperDimensionMap)
        printHyperDimension(counter)
        counter++

    }
    return registerHyperActivity()
}


fun main() {
    readCubeData("data/data_day17")
    println("Cube Space Initialized. ${initCubeSpace(6)} cubes active")

    readHyperCubeData("data/data_day17")
    println("Hypercube init: ${initHyperCubeSpace(6)}")
}
