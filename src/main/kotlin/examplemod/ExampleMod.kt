package examplemod

import examplemod.examples.*
import necesse.engine.commands.CommandsManager
import necesse.engine.modLoader.annotations.ModEntry
import necesse.engine.registries.*
import necesse.gfx.gameTexture.GameTexture
import necesse.inventory.recipe.Ingredient
import necesse.inventory.recipe.Recipe
import necesse.inventory.recipe.Recipes
import necesse.level.maps.biomes.Biome

@ModEntry
class ExampleMod {
    fun init() {
        println("Hello world from my example mod!")

        // Register our tiles
        TileRegistry.registerTile("exampletile", ExampleTile(), 1f, true)

        // Register out objects
        ObjectRegistry.registerObject("exampleobject", ExampleObject(), 2f, true)

        // Register our items
        ItemRegistry.registerItem("exampleitem", ExampleMaterialItem(), 10f, true)
        ItemRegistry.registerItem("examplesword", ExampleSwordItem(), 20f, true)
        ItemRegistry.registerItem("examplestaff", ExampleProjectileWeapon(), 30f, true)

        // Register our mob
        MobRegistry.registerMob("examplemob", ExampleMob::class.java, true)

        // Register our projectile
        ProjectileRegistry.registerProjectile("exampleprojectile", ExampleProjectile::class.java, "exampleprojectile", "exampleprojectile_shadow")

        // Register our buff
        BuffRegistry.registerBuff("examplebuff", ExampleBuff())

        PacketRegistry.registerPacket(ExamplePacket::class.java)
    }

    fun initResources() {
        ExampleMob.texture = GameTexture.fromFile("mobs/examplemob")
    }

    fun postInit() {
        // Add recipes
        // Example item recipe, crafted in inventory for 2 iron bars
        Recipes.registerModRecipe(Recipe(
            "exampleitem",
            1,
            RecipeTechRegistry.NONE,
            arrayOf(
                Ingredient("ironbar", 2)
            )
        ).showAfter("woodboat")) // Show recipe after wood boat recipe
        // Example sword recipe, crafted in iron anvil using 4 example items and 5 copper bars
        Recipes.registerModRecipe(Recipe(
            "examplesword",
            1,
            RecipeTechRegistry.IRON_ANVIL,
            arrayOf(
                Ingredient("exampleitem", 4),
                Ingredient("copperbar", 5)
            )
        ))
        // Example staff recipe, crafted in workstation using 4 example items and 10 gold bars
        Recipes.registerModRecipe(Recipe(
            "examplestaff",
            1,
            RecipeTechRegistry.WORKSTATION,
            arrayOf(
                Ingredient("exampleitem", 4),
                Ingredient("goldbar", 10)
            )
        ).showAfter("exampleitem")) // Show the recipe after example item recipe

        // Add out example mob to default cave mobs.
        // Spawn tables use a ticket/weight system. In general, common mobs have about 100 tickets.
        Biome.defaultCaveMobs
            .add(100, "examplemob")

        // Register our server chat command
        CommandsManager.registerServerCommand(ExampleChatCommand())
    }
}