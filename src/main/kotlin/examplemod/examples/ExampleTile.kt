package examplemod.examples

import necesse.engine.util.GameRandom
import necesse.gfx.gameTexture.GameTexture
import necesse.gfx.gameTexture.GameTextureSection
import necesse.level.gameTile.TerrainSplatterTile
import necesse.level.maps.Level
import java.awt.Color
import java.awt.Point

class ExampleTile : TerrainSplatterTile(false, "exampletile") {
    private var texture: GameTexture? = null
    private val drawRandom : GameRandom     // Used only in draw function

    init {
        canBeMined = true
        drawRandom = GameRandom()
        roomProperties.add("outsidefloor")
        mapColor = Color(200, 50, 200)
    }

    override fun loadTextures() {
        super.loadTextures()
        texture = GameTexture.fromFile("tiles/exampletile")
    }

    override fun getTerrainSprite(gameTextureSection: GameTextureSection, level: Level, tileX: Int, tileY: Int): Point {
        // This runs asynchronously, so if you want to randomize the tile that's
        // being drawn we have to synchronize the random call
        var tile: Int
        synchronized(drawRandom) { tile = drawRandom.seeded(getTileSeed(tileX, tileY)).nextInt(texture!!.height / 32) }
        return Point(0, tile)
    }

    override fun getTerrainPriority(): Int {
        return PRIORITY_TERRAIN
    }
}