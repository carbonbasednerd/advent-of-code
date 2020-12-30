import java.io.File
import java.util.*

val tiles = mutableListOf<Tile>()

//flipped: 0 not flipped, 1 left and right flip, 2 top and bottom flip
data class Tile(
    val tileNumber: Int, val tileLayout: List<String>,
    var top: String = "", var bottom: String = "", var right: String = "",
    var left: String = "", var rotated: Int = 0, var flipped: Int = 0
) {
    fun calcEdges() {
        top = tileLayout.first()
        bottom = tileLayout.last()
        val leftBuilder = StringBuilder()
        val rightBuilder = StringBuilder()
        tileLayout.forEach {
            leftBuilder.append(it.first())
            rightBuilder.append(it.last())
        }
        left = leftBuilder.toString()
        right = rightBuilder.toString()
    }

    fun topReversed(): Long = top.reversed().toLong()
    fun bottomReversed(): Long = bottom.reversed().toLong()
    fun leftReversed(): Long = left.reversed().toLong()
    fun rightReversed(): Long = right.reversed().toLong()

    fun topL(): Long = top.toLong()
    fun bottomL(): Long = bottom.toLong()
    fun leftL(): Long = left.toLong()
    fun rightL(): Long = right.toLong()

    fun printTile() {
        tileLayout.forEach {
            println(it)
        }
    }

    fun rotateLayout() {
        Collections.rotate(tileLayout, 1)
    }

    //methods to get proper side after transform
}

data class Tile2(
    val tileNumber: Int, val tileLayout: List<String>,
    var top: String = "", var bottom: String = "", var right: String = "",
    var left: String = "", var rotated: Int = 0, var flipped: Int = 0
) {
    fun calcEdges() {
        top = tileLayout.first()
        bottom = tileLayout.last()
        val leftBuilder = StringBuilder()
        val rightBuilder = StringBuilder()
        tileLayout.forEach {
            leftBuilder.append(it.first())
            rightBuilder.append(it.last())
        }
        left = leftBuilder.toString()
        right = rightBuilder.toString()
    }

    fun printTile() {
        tileLayout.forEach {
            println(it)
        }
    }

    fun rotateLayout() {
        Collections.rotate(tileLayout, 1)
    }

    fun topReversed(): Long = top.reversed().toLong()
    fun bottomReversed(): Long = bottom.reversed().toLong()
    fun leftReversed(): Long = left.reversed().toLong()
    fun rightReversed(): Long = right.reversed().toLong()

    fun topL(): Long = top.toLong()
    fun bottomL(): Long = bottom.toLong()
    fun leftL(): Long = left.toLong()
    fun rightL(): Long = right.toLong()

    //methods to get proper side after transform
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
fun calculateEdges() {
    tiles.forEach {
        it.calcEdges()
    }
}

fun tilesAssemble(): Long {
    var answer = 1L
    var tileCorners = mutableListOf<Int>()
    tiles.forEach main@{ t ->
        var bottom = false
        var top = false
        var right = false
        var left = false
        tiles.forEach sub@{
            if (it.tileNumber == t.tileNumber) return@sub

            //unmodified
            if ((t.topL() xor it.bottomL()) == 0L) top = true
            if ((t.bottomL() xor it.topL()) == 0L) bottom = true
            if ((t.rightL() xor it.leftL()) == 0L) right = true
            if ((t.leftL() xor it.rightL()) == 0L) left = true

            //rotated 90
            if ((t.topL() xor it.rightReversed()) == 0L) top = true
            if ((t.bottomL() xor it.leftReversed()) == 0L) bottom = true
            if ((t.rightL() xor it.bottomL()) == 0L) right = true
            if ((t.leftL() xor it.topL()) == 0L) left = true

            //rotated 180
            if ((t.topL() xor it.topReversed()) == 0L) top = true
            if ((t.bottomL() xor it.bottomReversed()) == 0L) bottom = true
            if ((t.rightL() xor it.rightReversed()) == 0L) right = true
            if ((t.leftL() xor it.leftReversed()) == 0L) left = true

            //flipped left right
            if ((t.topL() xor it.bottomReversed()) == 0L) top = true
            if ((t.bottomL() xor it.topReversed()) == 0L) bottom = true
            if ((t.rightL() xor it.rightL()) == 0L) right = true
            if ((t.leftL() xor it.leftL()) == 0L) left = true

            //flipped left right 90
            if ((t.topL() xor it.leftReversed()) == 0L) top = true
            if ((t.bottomL() xor it.rightReversed()) == 0L) bottom = true
            if ((t.rightL() xor it.bottomReversed()) == 0L) right = true
            if ((t.leftL() xor it.topReversed()) == 0L) left = true

            //flipped left right 180
            if ((t.topL() xor it.topL()) == 0L) top = true
            if ((t.bottomL() xor it.bottomL()) == 0L) bottom = true
            if ((t.rightL() xor it.leftReversed()) == 0L) right = true
            if ((t.leftL() xor it.rightReversed()) == 0L) left = true

            //flipped left right 270
            if ((t.topL() xor it.rightL()) == 0L) top = true
            if ((t.bottomL() xor it.leftL()) == 0L) bottom = true
            if ((t.rightL() xor it.topL()) == 0L) right = true
            if ((t.leftL() xor it.bottomL()) == 0L) left = true

            //flipped top bottom
            if ((t.topL() xor it.topReversed()) == 0L) top = true
            if ((t.bottomL() xor it.bottomReversed()) == 0L) bottom = true
            if ((t.rightL() xor it.leftL()) == 0L) right = true
            if ((t.leftL() xor it.rightL()) == 0L) left = true

            //flipped top bottom 90
            if ((t.topL() xor it.rightReversed()) == 0L) top = true
            if ((t.bottomL() xor it.leftReversed()) == 0L) bottom = true
            if ((t.rightL() xor it.topReversed()) == 0L) right = true
            if ((t.leftL() xor it.bottomReversed()) == 0L) left = true

            //flipped top bottom 180
            if ((t.topL() xor it.bottomL()) == 0L) top = true
            if ((t.bottomL() xor it.topL()) == 0L) bottom = true
            if ((t.rightL() xor it.rightReversed()) == 0L) right = true
            if ((t.leftL() xor it.leftReversed()) == 0L) left = true

            //flipped top bottom 270
            if ((t.topL() xor it.leftL()) == 0L) top = true
            if ((t.bottomL() xor it.rightL()) == 0L) bottom = true
            if ((t.rightL() xor it.bottomL()) == 0L) right = true
            if ((t.leftL() xor it.topL()) == 0L) left = true

        }
        if (top && left && !right && !bottom) {
            answer *= t.tileNumber
            tileCorners.add(t.tileNumber)
            combinedImage[dimensions-1][dimensions-1] = t
            usedTiles.add(t.tileNumber)
            println("Tile: ${t.tileNumber}")
        }
        if (top && right && !left && !bottom) {
            answer *= t.tileNumber
            tileCorners.add(t.tileNumber)
            combinedImage[dimensions-1][0] = t
            usedTiles.add(t.tileNumber)
            println("Tile: ${t.tileNumber}")
        }
        if (bottom && left && !top && !right) {
            answer *= t.tileNumber
            tileCorners.add(t.tileNumber)
            combinedImage[0][dimensions-1] = t
            usedTiles.add(t.tileNumber)
            println("Tile: ${t.tileNumber}")
        }
        if (bottom && right && !top && !left) {
            answer *= t.tileNumber
            tileCorners.add(t.tileNumber)
            combinedImage[0][0] = t
            usedTiles.add(t.tileNumber)
            println("Tile: ${t.tileNumber}")
        }
    }
    return answer
}

//part 2 - my part one solution won't solve the solution. It will give me the corners
// but that's it. I'll need to build the whole thing.
val combinedImage = mutableListOf<MutableList<Tile?>>()
var dimensions = 0
var usedTiles = mutableSetOf<Int>()
//fun fitTileEdge(topPiece: Long) : Tile {
//    var top = false
//    tiles.forEach main@{ it ->
//        //unmodified
//        if ((topPiece xor it.topL()) == 0L) top = true
//        if(top) {
//            return it
//        }
//        //rotated 90
//        if ((topPiece xor it.leftReversed()) == 0L) top = true
//        if (top){
//            it.top =  it.leftReversed().to
//            return it
//        }
//        if ((t.topL() xor it.rightReversed()) == 0L) top = true
//        if ((t.bottomL() xor it.leftReversed()) == 0L) bottom = true
//        if ((t.rightL() xor it.bottomL()) == 0L) right = true
//        if ((t.leftL() xor it.topL()) == 0L) left = true
//
//        //rotated 180
//
//        //rotated 270
//
//        //flipped left right
//
//        //flipped left right 90
//
//        //flipped left right 180
//
//        //flipped left right 270
//
//        //flipped top bottom
//
//
//        //flipped top bottom 90
//
//
//        //flipped top bottom 180
//
//
//        //flipped top bottom 270
//
//    }
//}
//fun fitTile(leftPiece: Long): Tile {
//    tiles.forEach main@{ it ->
//        var left = false
//        //unmodified
//        if ((topPiece!! xor it.topL()) == 0L) top = true
//        if ((leftPiece!! xor it.leftL()) == 0L) left = true
//        if ((t.bottomL() xor it.topL()) == 0L) bottom = true
//        if ((t.rightL() xor it.leftL()) == 0L) right = true
//
//
//        //rotated 90
//        if ((t.topL() xor it.rightReversed()) == 0L) top = true
//        if ((t.bottomL() xor it.leftReversed()) == 0L) bottom = true
//        if ((t.rightL() xor it.bottomL()) == 0L) right = true
//        if ((t.leftL() xor it.topL()) == 0L) left = true
//
//        //rotated 180
//        if ((t.topL() xor it.topReversed()) == 0L) top = true
//        if ((t.bottomL() xor it.bottomReversed()) == 0L) bottom = true
//        if ((t.rightL() xor it.rightReversed()) == 0L) right = true
//        if ((t.leftL() xor it.leftReversed()) == 0L) left = true
//
//        //flipped left right
//        if ((t.topL() xor it.bottomReversed()) == 0L) top = true
//        if ((t.bottomL() xor it.topReversed()) == 0L) bottom = true
//        if ((t.rightL() xor it.rightL()) == 0L) right = true
//        if ((t.leftL() xor it.leftL()) == 0L) left = true
//
//        //flipped left right 90
//        if ((t.topL() xor it.leftReversed()) == 0L) top = true
//        if ((t.bottomL() xor it.rightReversed()) == 0L) bottom = true
//        if ((t.rightL() xor it.bottomReversed()) == 0L) right = true
//        if ((t.leftL() xor it.topReversed()) == 0L) left = true
//
//        //flipped left right 180
//        if ((t.topL() xor it.topL()) == 0L) top = true
//        if ((t.bottomL() xor it.bottomL()) == 0L) bottom = true
//        if ((t.rightL() xor it.leftReversed()) == 0L) right = true
//        if ((t.leftL() xor it.rightReversed()) == 0L) left = true
//
//        //flipped left right 270
//        if ((t.topL() xor it.rightL()) == 0L) top = true
//        if ((t.bottomL() xor it.leftL()) == 0L) bottom = true
//        if ((t.rightL() xor it.topL()) == 0L) right = true
//        if ((t.leftL() xor it.bottomL()) == 0L) left = true
//
//        //flipped top bottom
//        if ((t.topL() xor it.topReversed()) == 0L) top = true
//        if ((t.bottomL() xor it.bottomReversed()) == 0L) bottom = true
//        if ((t.rightL() xor it.leftL()) == 0L) right = true
//        if ((t.leftL() xor it.rightL()) == 0L) left = true
//
//        //flipped top bottom 90
//        if ((t.topL() xor it.rightReversed()) == 0L) top = true
//        if ((t.bottomL() xor it.leftReversed()) == 0L) bottom = true
//        if ((t.rightL() xor it.topReversed()) == 0L) right = true
//        if ((t.leftL() xor it.bottomReversed()) == 0L) left = true
//
//        //flipped top bottom 180
//        if ((t.topL() xor it.bottomL()) == 0L) top = true
//        if ((t.bottomL() xor it.topL()) == 0L) bottom = true
//        if ((t.rightL() xor it.rightReversed()) == 0L) right = true
//        if ((t.leftL() xor it.leftReversed()) == 0L) left = true
//
//        //flipped top bottom 270
//        if ((t.topL() xor it.leftL()) == 0L) top = true
//        if ((t.bottomL() xor it.rightL()) == 0L) bottom = true
//        if ((t.rightL() xor it.bottomL()) == 0L) right = true
//        if ((t.leftL() xor it.topL()) == 0L) left = true
//
//
//        if (top && left && !right && !bottom) {
//            combinedImage[0][0] = t
//            println("Tile: ${t.tileNumber}")
//        }
//        if (top && right && !left && !bottom) {
//            r)
//            combinedImage[0][dimensions - 1] = t
//            println("Tile: ${t.tileNumber}")
//        }
//        if (bottom && left && !top && !right) {
//            combinedImage[dimensions - 1][0] = t
//            println("Tile: ${t.tileNumber}")
//        }
//        if (bottom && right && !top && !left) {
//            combinedImage[dimensions - 1][dimensions - 1] = t
//            println("Tile: ${t.tileNumber}")
//        }
//    }
//}

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
            var topToMatch = -1L
            var leftToMatch = -1L
            if (ix != 0) topToMatch = combinedImage[ix - 1][iy]!!.bottomL()
        }
    }
}

fun main() {
    readTiles("data/test_data_day20")
    dimensions = tiles.size / 2
    initCombinedImage()
    calculateEdges()
    println(tiles.count())
    println(tiles)
    println("All the corners = ${tilesAssemble()}")
    tiles[0].printTile()
    tiles[0].rotateLayout()
    println("\n")
    tiles[0].printTile()
}