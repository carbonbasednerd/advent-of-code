import java.io.File

val tiles = mutableListOf<Tile>()

data class Tile(val tileNumber: Int, val tileLayout: List<List<Boolean>>)

fun readTiles(fileName: String) {
    var newTile = true
    var tempTileName: Int = 0
    var tempLayout: MutableList<List<Boolean>> = mutableListOf()
    File(fileName).forEachLine {
        if (newTile) {
            val header = it.split(" ")
            tempTileName = header[1].replace(":","").toInt()
            tempLayout = mutableListOf()
            newTile = false
        } else if(it == "") {
            newTile = true
            tiles.add(Tile(tempTileName, tempLayout))
        } else {
            val listPoints = mutableListOf<Boolean>()
            listPoints.addAll(it.map{p -> p == '.'})
            tempLayout.add(listPoints)
        }
    }
}

//part one
fun tilesAssemble() {
    // create a tile grid? This way we can find the corners first.
    // a top should have no matching border to it's top, bottom to bottom etc..
    //so a corner would be defined as, top right no matching Top and right
    //All we need is the four corners so, we don't need to figure out the rest
    // we can figure out the grid size by dividing it by two (our data set is 144 tiles)
    

}

fun main() {
    readTiles("data/data_day20")
    println(tiles.count())
}