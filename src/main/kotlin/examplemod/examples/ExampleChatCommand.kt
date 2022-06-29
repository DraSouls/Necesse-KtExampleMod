package examplemod.examples

import necesse.engine.commands.CmdParameter
import necesse.engine.commands.CommandLog
import necesse.engine.commands.ModularChatCommand
import necesse.engine.commands.PermissionLevel
import necesse.engine.commands.parameterHandlers.ItemParameterHandler
import necesse.engine.commands.parameterHandlers.PresetStringParameterHandler
import necesse.engine.network.client.Client
import necesse.engine.network.server.Server
import necesse.engine.network.server.ServerClient
import necesse.engine.registries.ItemRegistry
import necesse.inventory.item.Item

class ExampleChatCommand : ModularChatCommand(
    "example", "An example chat command", PermissionLevel.ADMIN, false,
    // Parameter handlers are used for autocomplete, and will parse the input into their type
    CmdParameter("item", ItemParameterHandler(), true),
    CmdParameter("string", PresetStringParameterHandler("option1", "otheroption"), false)
) {

    // Args will correspond with the parameters parsed objects
    // client will be null when ran from server, serverClient won't be null if ran from a server client
    override fun runModular(client: Client, server: Server, serverClient: ServerClient, args: Array<Any>,
                            errors: Array<String>, commandLog: CommandLog) {
        // Parse the arguments, their index will be the same that they are passed to in constructor
        val item = args[0] as Item  // Optional parameters are still parsed
        val string = args[1] as String

        // Do stuff with the parsed args
        if (item == null) {
            commandLog.add("No item given")
        } else {
            commandLog.add("Item: " + ItemRegistry.getDisplayName(item.id))
        }
        commandLog.add("String: $string")
    }
}