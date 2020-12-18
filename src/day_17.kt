import java.io.File

val cubeData = mutableMapOf<Int, MutableList<MutableList<Boolean>>>()
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
            it.add(false)
        }
        val newList = mutableListOf<Boolean>()
        (1..width).forEach { _ ->
            newList.add(false)
        }
        dimension.value.add(newList)
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
        if (counter > 1) {
            expandExistingSpace()
        }
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
        height += 2
        width += 2

    }
    return registerActivity()
}

//somethings wrong - getting wrong values on cycle 1
fun main() {
    readCubeData("data/test_data_day17")
    println("Cube Space Initialized. ${initCubeSpace(1)} cubes active")
}
