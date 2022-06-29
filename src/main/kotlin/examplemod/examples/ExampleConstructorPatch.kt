package examplemod.examples

import necesse.engine.modLoader.annotations.ModConstructorPatch
import necesse.entity.mobs.friendly.critters.RabbitMob
import net.bytebuddy.asm.Advice

/**
 * Intercepts a constructor
 * Check out ExampleMethodPatch class for some documentation
 */
@ModConstructorPatch(target = RabbitMob::class, arguments = []) // No arguments
object ExampleConstructorPatch {

    /*
        Other than printing a debug message, this is currently set up to change
        the speed of all rabbits to 60 (double of normal).
     */

    @Advice.OnMethodExit
    fun onExit(@Advice.This rabbitMob: RabbitMob) {
        rabbitMob.speed = 60f
        // Debug message to know it's working
        println("Exited RabbitMob constructor: " + rabbitMob.stringID)
    }
}