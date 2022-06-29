package examplemod.examples

import necesse.engine.tickManager.TickManager
import necesse.engine.util.GameRandom
import necesse.entity.mobs.GameDamage
import necesse.entity.mobs.MobDrawable
import necesse.entity.mobs.PlayerMob
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI
import necesse.entity.mobs.ai.behaviourTree.trees.CollisionPlayerChaserWandererAI
import necesse.entity.mobs.hostile.HostileMob
import necesse.entity.particle.FleshParticle
import necesse.entity.particle.Particle
import necesse.gfx.camera.GameCamera
import necesse.gfx.drawOptions.DrawOptions
import necesse.gfx.drawables.OrderableDrawables
import necesse.gfx.gameTexture.GameTexture
import necesse.inventory.lootTable.LootTable
import necesse.inventory.lootTable.lootItem.ChanceLootItem
import necesse.level.maps.Level
import java.awt.Rectangle

class ExampleMob : HostileMob(200) {
    // MUST HAVE an empty constructor
    init {
        speed = 50f
        friction = 3f

        // Hitbox, collision box, and select box (for hovering)
        collision = Rectangle(-10, -7, 20, 14)
        hitBox = Rectangle(-14, -12, 28, 24)
        selectBox = Rectangle(-14, -7 - 34, 28, 48)
    }

    companion object {
        // Loaded in examplemod.ExampleMod.initResources()
        var texture: GameTexture? = null
        var lootTable = LootTable(
            ChanceLootItem.between(0.5f, "exampleitem", 1, 3) // 50% chance to drop between 1-3 example items
        )
    }

    override fun init() {
        super.init()
        // Setup AI
        ai = BehaviourTreeAI(this, CollisionPlayerChaserWandererAI(null, 12 * 32, GameDamage(25f), 25, 40000))
    }

    override fun getLootTable(): LootTable {
        return Companion.lootTable
    }

    override fun spawnDeathParticles(knockbackX: Float, knockbackY: Float) {
        // Spawn flesh debris particles
        for (i in 0..3) {
            level.entityManager.addParticle(FleshParticle(
                level, texture,
                GameRandom.globalRandom.nextInt(5),  // Randomize between the debris sprites
                8,
                32,
                x, y, 20f,  // Position
                knockbackX, knockbackY // Basically start speed of the particles
            ), Particle.GType.IMPORTANT_COSMETIC)
        }
    }

    override fun addDrawables(list: MutableList<MobDrawable>, tileList: OrderableDrawables,
                              topList: OrderableDrawables, level: Level, x: Int, y: Int,
                              tickManager: TickManager, camera: GameCamera, perspective: PlayerMob) {
        super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective)
        // Tile positions are basically level positions divided by 32. getTileX() does this for us etc.
        val light = level.getLightLevel(tileX, tileY)
        val drawX = camera.getDrawX(x) - 32
        var drawY = camera.getDrawY(y) - 51

        // A helper method to get the sprite of the current animation/direction of this mob
        val sprite = getAnimSprite(x, y, dir)
        drawY += getBobbing(x, y)
        drawY += getLevel().getTile(tileX, tileY).getMobSinkingAmount(this)
        val drawOptions: DrawOptions = texture!!.initDraw()
            .sprite(sprite.x, sprite.y, 64)
            .light(light)
            .pos(drawX, drawY)
        list.add(object : MobDrawable() {
            override fun draw(tickManager: TickManager) {
                drawOptions.draw()
            }
        })
        addShadowDrawables(tileList, x, y, light, camera)
    }

    public override fun getRockSpeed(): Int {
        // Change the speed at which this mobs animation plays
        return 20
    }
}