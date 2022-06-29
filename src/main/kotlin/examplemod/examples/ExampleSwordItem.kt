package examplemod.examples

import necesse.inventory.item.toolItem.swordToolItem.CustomSwordToolItem

// Extends CustomSwordToolItem
class ExampleSwordItem  // Weapon attack textures are loaded from resources/player/weapons/<itemStringID>
    : CustomSwordToolItem(Rarity.UNCOMMON, 300, 20, 120, 100, 400)