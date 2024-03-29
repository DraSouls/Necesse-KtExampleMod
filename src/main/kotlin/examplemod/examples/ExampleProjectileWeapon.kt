package examplemod.examples

import necesse.engine.Screen
import necesse.engine.localization.Localization
import necesse.engine.network.PacketReader
import necesse.engine.network.packet.PacketSpawnProjectile
import necesse.engine.registries.DamageTypeRegistry
import necesse.engine.sound.SoundEffect
import necesse.engine.util.GameRandom
import necesse.entity.mobs.AttackAnimMob
import necesse.entity.mobs.GameDamage
import necesse.entity.mobs.PlayerMob
import necesse.entity.projectile.Projectile
import necesse.gfx.GameResources
import necesse.gfx.gameTooltips.ListGameTooltips
import necesse.inventory.InventoryItem
import necesse.inventory.PlayerInventorySlot
import necesse.inventory.item.toolItem.projectileToolItem.ProjectileToolItem
import necesse.level.maps.Level

class ExampleProjectileWeapon : ProjectileToolItem(400) {

    // This weapon will shoot out some projectiles.
    // Different classes for specific projectile weapon are already in place that you can use:
    // GunProjectileToolItem, BowProjectileToolItem, BoomerangToolItem, etc.

    init {
        rarity = Rarity.RARE
        animSpeed = 300 // 300 ms attack time
        attackDmg = GameDamage(DamageTypeRegistry.MAGIC, 20f) // 20 magic damage
        velocity = 100 // Velocity of projectiles
        knockback = 50 // Knockback of projectiles
        attackRange = 1500 // Range of the projectile

        // Offsets of the attack item sprite relative to the player arm
        attackXOffset = 12
        attackYOffset = 22
    }

    override fun getTooltips(item: InventoryItem, perspective: PlayerMob): ListGameTooltips {
        val tooltips = super.getTooltips(item, perspective)
        tooltips.add(Localization.translate("itemtooltip", "examplestafftip"))
        tooltips.add(getAttackDamageTip(item, perspective)) // Add attack damage to tooltip
        tooltips.add(getAttackSpeedTip(item, perspective)) // Adds attack speed to tooltip
        addCritChanceTip(tooltips, item, perspective) // Adds crit chance if above 0%
        return tooltips
    }

    override fun showAttack(level: Level, x: Int, y: Int, mob: AttackAnimMob, attackHeight: Int,
                            item: InventoryItem, seed: Int, contentReader: PacketReader) {
        if (level.isClientLevel) {
            // Play magic bolt sound effect with 70% volume, and a random pitch between 100 and 110%
            Screen.playSound(GameResources.magicbolt1, SoundEffect.effect(mob)
                .volume(0.7f)
                .pitch(GameRandom.globalRandom.getFloatBetween(1.0f, 1.1f))
            )
        }
    }

    override fun onAttack(level: Level, x: Int, y: Int, player: PlayerMob, attackHeight: Int,
                          item: InventoryItem, slot: PlayerInventorySlot, animAttack: Int,
                          seed: Int, contentReader: PacketReader): InventoryItem {
        // This method is ran on the attacking client and on the server.
        // This means we need to tell other clients that a projectile is being "summoned".
        // Every projectile weapon is set to include an integer seed used to make sure that the attacking client
        // and the server gives the projectiles summoned the same uniqueID.

        // Example we use our example projectile
        val projectile: Projectile = ExampleProjectile(
            level, player,  // Level and owner
            player.x, player.y,  // Start position of projectile
            x.toFloat(), y.toFloat(),  // Target position of projectile
            getVelocity(item, player).toFloat(),  // Will add player buffs, enchantments etc
            getAttackRange(item),  // Will add player buffs, enchantments etc
            getDamage(item),  // Will add player buffs, enchantments etc
            getKnockback(item, player) // Will add player buffs, enchantments etc
        )
        // Sync the uniqueID using the given seed
        val random = GameRandom(seed.toLong())
        projectile.resetUniqueID(random)

        // We can move the projectile 40 units out
        projectile.moveDist(40.0)

        // Add the projectile without sending it to clients
        level.entityManager.projectiles.addHidden(projectile)

        // Since we didn't send it to clients, we can do it here
        if (level.isServerLevel) {
            // We do attacking client as an exception, since the above logic is already running on his side
            level.server.network.sendToClientsAtExcept(
                PacketSpawnProjectile(projectile),
                player.serverClient,
                player.serverClient
            )
        }

        // Should return the item after it's been used.
        // Example: if it consumes the item, you can use item.setAmount(item.getAmount() - 1)
        return item
    }
}