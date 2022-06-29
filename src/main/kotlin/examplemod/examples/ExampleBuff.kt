package examplemod.examples

import necesse.entity.mobs.buffs.ActiveBuff
import necesse.entity.mobs.buffs.BuffModifiers
import necesse.entity.mobs.buffs.staticBuffs.Buff

class ExampleBuff : Buff() {
    init {
        canCancel = true
        isVisible = true
        shouldSave = true
    }

    override fun init(activeBuff: ActiveBuff) {
        // Apply modifiers here
        activeBuff.setModifier(BuffModifiers.SPEED, 0.5f) // +50% speed
    }

    override fun serverTick(buff: ActiveBuff) {
        // You can do server ticks here
    }

    override fun clientTick(buff: ActiveBuff) {
        // You can do client ticks here, like adding particles to buff.owner
    }
}