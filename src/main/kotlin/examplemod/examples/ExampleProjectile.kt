package examplemod.examples

import necesse.engine.tickManager.TickManager
import necesse.entity.mobs.GameDamage
import necesse.entity.mobs.Mob
import necesse.entity.mobs.PlayerMob
import necesse.entity.projectile.followingProjectile.FollowingProjectile
import necesse.entity.trails.Trail
import necesse.gfx.camera.GameCamera
import necesse.gfx.drawables.EntityDrawable
import necesse.gfx.drawables.LevelSortedDrawable
import necesse.gfx.drawables.OrderableDrawables
import necesse.level.maps.Level
import java.awt.Color

class ExampleProjectile : FollowingProjectile {
    // Textures are loaded from resources/projectiles/<projectileStringID>
    // If shadow path is defined when registering the projectile, it is loaded from
    // that path into this projectile shadowTexture field
    // Each projectile must have an empty constructor for the registry to construct them
    constructor() {}

    // We use this constructor on attack to spawn the projectile with the correct parameters
    constructor(level: Level?, owner: Mob?, x: Float, y: Float, targetX: Float, targetY: Float,
                speed: Float, distance: Int, damage: GameDamage?, knockback: Int) {
        this.level = level
        this.owner = owner
        this.x = x
        this.y = y
        this.setTarget(targetX, targetY)
        this.speed = speed
        this.distance = distance
        this.damage = damage
        this.knockback = knockback
    }

    override fun init() {
        super.init()
        turnSpeed = 1.25f // This is a homing projectile with a turn speed
        givesLight = false // The projectile does not give off light
        height = 18f // It's flying 18 pixels above ground
        trailOffset = -14f // The trail is 14 pixels behind the projectile
        setWidth(16f, true) // Extends the hitbox to 16 pixels wide
        piercing = 2 // Pierces 2 mobs before disappearing (total of 3 hits)
        bouncing = 10 // Can bounce 10 times off walls
    }

    override fun getParticleColor(): Color {
        // Projectiles sometimes spawn particles. You can return null for no particles.
        return Color(63, 157, 18)
    }

    override fun getTrail(): Trail {
        // Projectiles sometimes spawn trails. You can return null for no trail.
        return Trail(this, level, Color(191, 147, 22), 26f, 500, getHeight())
    }

    override fun updateTarget() {
        // When we have traveled longer than 20 distance, start to find and update the target
        if (traveledDistance > 20) {
            findTarget(
                { m: Mob -> m.isHostile }, 200f, 450f
            )
        }
    }

    override fun addDrawables(list: MutableList<LevelSortedDrawable>, tileList: OrderableDrawables,
                              topList: OrderableDrawables, overlayList: OrderableDrawables, level: Level,
                              tickManager: TickManager, camera: GameCamera, perspective: PlayerMob) {
        if (removed()) return
        val light = level.getLightLevel(this)
        val drawX = camera.getDrawX(x) - texture.width / 2
        val drawY = camera.getDrawY(y)
        val options = texture.initDraw()
            .light(light)
            .rotate(getAngle(), texture.width / 2, 2) // We rotate the texture around the tip of it
            .pos(drawX, drawY - getHeight().toInt())

        list.add(object : EntityDrawable(this) {
            override fun draw(tickManager: TickManager) {
                options.draw()
            }
        })

        addShadowDrawables(tileList, drawX, drawY, light, getAngle(), texture.width / 2, 2)
    }
}