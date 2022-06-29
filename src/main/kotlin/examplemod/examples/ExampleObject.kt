package examplemod.examples

import necesse.engine.tickManager.TickManager
import necesse.entity.mobs.PlayerMob
import necesse.entity.objectEntity.ObjectEntity
import necesse.gfx.camera.GameCamera
import necesse.gfx.drawables.LevelSortedDrawable
import necesse.gfx.drawables.OrderableDrawables
import necesse.gfx.gameTexture.GameTexture
import necesse.inventory.item.toolItem.ToolType
import necesse.level.gameObject.GameObject
import necesse.level.maps.Level
import java.awt.Color
import java.awt.Rectangle

class ExampleObject : GameObject(Rectangle(4, 4, 26, 26)) {
    private var texture: GameTexture? = null

    init {
        // Remember that tiles are 32x32 pixels in size
        hoverHitbox = Rectangle(0, -32, 32, 64) // 2 tiles high mouse hover hitbox
        toolType = ToolType.ALL // Can be broken by all tools
        isLightTransparent = true // Lets light pass through
        mapColor = Color(31, 150, 148) // Also applies as debris color if not set
    }

    override fun loadTextures() {
        super.loadTextures()
        texture = GameTexture.fromFile("objects/exampleobject")
    }

    override fun addDrawables(list: MutableList<LevelSortedDrawable>, tileList: OrderableDrawables,
                              level: Level, tileX: Int, tileY: Int, tickManager: TickManager,
                              camera: GameCamera, perspective: PlayerMob) {
        val light = level.getLightLevel(tileX, tileY)
        val drawX = camera.getTileDrawX(tileX)
        val drawY = camera.getTileDrawY(tileY)
        // Use the rotation if you have rotation on your object
//        int rotation = level.getObjectRotation(tileX, tileY);
        val options = texture!!.initDraw().light(light).pos(drawX, drawY - texture!!.height + 32)
        // Can choose sprite with texture.initDraw().sprite(...)
        list.add(object : LevelSortedDrawable(this, tileX, tileY) {
            override fun getSortY(): Int {
                // Basically where this will be sorted on the Y axis (when it will be behind the player etc.)
                // Should be in [0 - 32] range
                return 16
            }

            override fun draw(tickManager: TickManager) {
                options.draw()
            }
        })
    }

    override fun drawPreview(level: Level, tileX: Int, tileY: Int, rotation: Int, alpha: Float,
                             player: PlayerMob, camera: GameCamera) {
        val drawX = camera.getTileDrawX(tileX)
        val drawY = camera.getTileDrawY(tileY)
        texture!!.initDraw().alpha(alpha).draw(drawX, drawY - texture!!.height + 32)
    }

    override fun getNewObjectEntity(level: Level, x: Int, y: Int): ObjectEntity? {
        // If this object has an object entity, return something else
        return null
    }
}