import java.io.File
import java.util.*

val tiles = mutableListOf<Tile>()

//flipped: 0 not flipped, 1 left and right flip, 2 top and bottom flip
data class Tile(
    val tileNumber: Int, val tileLayout: MutableList<String>,
    var isTopEdge: Boolean = false, var isBottomEdge: Boolean = false,
    var isRightEdge: Boolean = false, var isLeftEdge: Boolean = false
) {
    fun topL(): Long = tileLayout.first().toLong()
    fun bottomL(): Long = tileLayout.last().toLong()
    fun leftL(): Long {
        val builder = StringBuilder()
        tileLayout.forEach {
            builder.append(it.first())
        }
        return builder.toString().toLong()
    }

    fun rightL(): Long {
        val builder = StringBuilder()
        tileLayout.forEach {
            builder.append(it.last())
        }
        return builder.toString().toLong()
    }

    fun printTile() {
        tileLayout.forEach {
            println(it)
        }
    }

    fun rotate() {
        val m = tileLayout.size;
        val n = tileLayout[0].length;

        val rotatedLayout = mutableListOf<StringBuilder>()
        rotatedLayout.addAll(tileLayout.map { StringBuilder(it) })
        (0..m - 1).forEachIndexed { ix, x ->
            (0..n - 1).forEachIndexed { iy, y ->
                rotatedLayout[iy][m - ix - 1] = tileLayout[ix][iy]
            }
        }
        tileLayout.clear()
        tileLayout.addAll(rotatedLayout.map { it.toString() })
    }

    fun flipHead() {
        val tempLayout = mutableListOf<String>()
        tileLayout.forEach {
            tempLayout.add(0, it)
        }
        tileLayout.clear()
        tileLayout.addAll(tempLayout)
    }

    fun flipSides() {
        val tempLayout = mutableListOf<String>()
        tileLayout.forEach {
            tempLayout.add(it.reversed())
        }
        tileLayout.clear()
        tileLayout.addAll(tempLayout)
    }

    fun stripEdges() {
        println("Stripping $tileNumber")
        tileLayout.removeAt(0)
        tileLayout.removeAt(tileLayout.size-1)
        val tempList = mutableListOf<String>()
        tileLayout.forEach {
            tempList.add(it.drop(1).dropLast(1))
        }
        tileLayout.clear()
        tileLayout.addAll(tempList)
    }
}


fun readTiles(fileName: String) {
    var newTile = true
    var tempTileName: Int = 0
    var tempLayout: MutableList<String> = mutableListOf()
    File(fileName).forEachLine {
        if (newTile) {
            val header = it.split(" ")
            tempTileName = header[1].replace(":", "").toInt()
            tempLayout = mutableListOf()
            newTile = false
        } else if (it == "") {
            newTile = true
            tiles.add(Tile(tempTileName, tempLayout))
        } else {
            val listPoints = StringBuilder()
            it.forEach { p ->
                if (p == '.') {
                    listPoints.append('0')
                } else {
                    listPoints.append('1')
                }
            }
            tempLayout.add(listPoints.toString())
        }
    }
}

//part one
fun tilesAssemble(): Long {
    var foundCorner = false
    var answer = 1L
    tiles.forEach main@{ t ->
        var bottom = false
        var top = false
        var right = false
        var left = false
        tiles.forEach sub@{
            if (it.tileNumber == t.tileNumber) return@sub

            (1..4).forEach { i ->
                if ((t.topL() xor it.bottomL()) == 0L) top = true
                if ((t.bottomL() xor it.topL()) == 0L) bottom = true
                if ((t.rightL() xor it.leftL()) == 0L) right = true
                if ((t.leftL() xor it.rightL()) == 0L) left = true
                it.rotate()
            }

            it.flipSides()
            (1..4).forEach { i ->
                if ((t.topL() xor it.bottomL()) == 0L) top = true
                if ((t.bottomL() xor it.topL()) == 0L) bottom = true
                if ((t.rightL() xor it.leftL()) == 0L) right = true
                if ((t.leftL() xor it.rightL()) == 0L) left = true
                it.rotate()
            }

            //reset
            it.flipSides()

            it.flipHead()
            (1..4).forEach { i ->
                if ((t.topL() xor it.bottomL()) == 0L) top = true
                if ((t.bottomL() xor it.topL()) == 0L) bottom = true
                if ((t.rightL() xor it.leftL()) == 0L) right = true
                if ((t.leftL() xor it.rightL()) == 0L) left = true
                it.rotate()
            }

            //reset
            it.flipHead()
        }
        if (!top && !left && right && bottom) {
            answer *= t.tileNumber
            t.isTopEdge = true
            t.isLeftEdge = true
            println("Tile: ${t.tileNumber}")
        }

        if (!top && !right && left && bottom) {
            answer *= t.tileNumber
            t.isTopEdge = true
            t.isRightEdge = true
            println("Tile: ${t.tileNumber}")
        }
        if (!bottom && !left && right && top) {
            answer *= t.tileNumber
            t.isBottomEdge = true
            t.isLeftEdge = true
            println("Tile: ${t.tileNumber}")
        }
        if (!bottom && !right && left && top) {
            answer *= t.tileNumber
            t.isBottomEdge = true
            t.isRightEdge = true
            println("Tile: ${t.tileNumber}")
        }

        //todo: need to align the corners with an edge (1399)
        if (t.isTopEdge && t.isLeftEdge && t.tileNumber==1399) {
            combinedImage[0][0] = t
            usedTiles.add(t.tileNumber)
        }

    }
    return answer
}

//part 2 - my part one solution won't solve the solution. It will give me the corners
// but that's it. I'll need to build the whole thing. Use part 1 to find a corner and then build the rest off it.

val combinedImage = mutableListOf<MutableList<Tile?>>()
var dimensions = 0
var usedTiles = mutableSetOf<Int>()

fun fitTileTop(piece: Long): Tile {
    var top = false
    tiles.forEach main@{ it ->
        if (usedTiles.contains(it.tileNumber)) return@main

        (1..4).forEach { i ->
            if ((piece xor it.leftL()) == 0L) top = true
            if (top) {
                return it
            }
            it.rotate()
        }

        it.flipSides()
        (1..4).forEach { i ->
            if ((piece xor it.leftL()) == 0L) top = true
            if (top) {
                return it
            }
            it.rotate()
        }

        //reset
        it.flipSides()

        it.flipHead()
        (1..4).forEach { i ->
            if ((piece xor it.leftL()) == 0L) top = true
            if (top) {
                return it
            }
            it.rotate()
        }

        //reset
        it.flipHead()
    }
    //shouldn't come to this
    return Tile(0, mutableListOf("000"))
}

fun fitTile(piece: Long): Tile {
    var top = false
    tiles.forEach main@{ it ->
        if (usedTiles.contains(it.tileNumber)) return@main

        (1..4).forEach { i ->
            if ((piece xor it.topL()) == 0L) top = true
            if (top) {
                return it
            }
            it.rotate()
        }

        it.flipSides()
        (1..4).forEach { i ->
            if ((piece xor it.topL()) == 0L) top = true
            if (top) {
                return it
            }
            it.rotate()
        }

        //reset
        it.flipSides()

        it.flipHead()
        (1..4).forEach { i ->
            if ((piece xor it.topL()) == 0L) top = true
            if (top) {
                return it
            }
            it.rotate()
        }

        //reset
        it.flipHead()
    }
    //shouldn't come to this
    return Tile(0, mutableListOf("000"))
}

fun initCombinedImage() {
    (0..dimensions - 1).forEach { x ->
        val listy = mutableListOf<Tile?>()
        (0..dimensions - 1).forEach { y ->
            listy.add(null)
        }
        combinedImage.add(listy)
    }
}

fun imagesAssembleFull() {
    combinedImage.forEachIndexed { ix, x ->
        x.forEachIndexed inner@{ iy, y ->
            if (y != null) return@inner
            var tile: Tile
            if (ix == 0) {
                tile = fitTileTop(combinedImage[ix][iy - 1]!!.rightL())
            } else {
                tile = fitTile(combinedImage[ix - 1][iy]!!.bottomL())
            }
            usedTiles.add(tile.tileNumber)
            combinedImage[ix][iy] = tile
        }
    }
}

lateinit var finalImageTile: Tile
fun mergePhotoData() {
    combinedImage.forEach {
        it.forEach { t ->
            t!!.stripEdges()
        }
    }

    val dataSize = combinedImage[0][0]!!.tileLayout.size
    val perLine = combinedImage[0].size
    val finalImage = mutableListOf<String>()
    combinedImage.forEach {
        (0 until dataSize).forEach { a ->
            val fullImageString = StringBuilder()
            (0 until perLine).forEach {b ->
                fullImageString.append(it[b]!!.tileLayout[a])
            }
            finalImage.add(fullImageString.toString())
        }
    }
    finalImageTile = Tile(0, finalImage)
}

fun findTheMonster() {
    var currentIndex = 0
    var found = 0
    while (found == 0) {
        while (currentIndex < finalImageTile.tileLayout.size) {
            //check for regex
            val middle = "1....11....11....111".toRegex()
            val tileSlice = finalImageTile.tileLayout[currentIndex]
            val findResult = middle.find(tileSlice)
            var foundOne = false
            if (findResult != null) {
                val range = findResult.range
                if (currentIndex != 0 && currentIndex < finalImageTile.tileLayout.size) {
                    val headSlice = finalImageTile.tileLayout[currentIndex - 1]
                    val bellySlice = finalImageTile.tileLayout[currentIndex + 1]
                    if (headSlice[range.last - 1] == '1') {
                        val bellySubString = bellySlice.substring(range.first + 1, range.last - 2)
                        val bellyRegEx = "1..1..1..1..1..1".toRegex()
                        if (bellySubString.contains(bellyRegEx)) {
                            foundOne = true
                        }
                    }
                }
            }

            if (foundOne) {
                found++
                currentIndex += 2
            } else {
                currentIndex++
            }
        }
        if (found == 0) {
            finalImageTile.rotate()
            currentIndex = 0
            println("Rotated...")
        }
    }

    println(found)
}

fun main() {
    readTiles("data/data_day20")
    dimensions = tiles.size / 2
    initCombinedImage()
//    calculateEdges()
//    println(tiles.count())
//    println(tiles)
    println("All the corners = ${tilesAssemble()}")
    imagesAssembleFull()
//    println(combinedImage.map { it.map { x -> x!!.tileNumber } })
    mergePhotoData()
//    println(finalImage)
    findTheMonster()
}