package examplemod.examples

import necesse.engine.network.NetworkPacket
import necesse.engine.network.Packet
import necesse.engine.network.PacketReader
import necesse.engine.network.PacketWriter
import necesse.engine.network.client.Client
import necesse.engine.network.server.ServerClient

class ExamplePacket : Packet {
    val playerSlot: Int
    val someInteger: Int
    val someBoolean: Boolean
    val someString: String
    val someContent: Packet

    // MUST HAVE - Used for construction in registry
    constructor(data: ByteArray?) : super(data) {
        val reader = PacketReader(this)
        // Important that it's same order as written in
        playerSlot = reader.nextByteUnsigned // Since player slots never go over 255
        someInteger = reader.nextInt
        someBoolean = reader.nextBoolean
        someString = reader.nextString
        someContent = reader.nextContentPacket
    }

    constructor(client: ServerClient, someInteger: Int, someBoolean: Boolean, someString: String, someContent: Packet) {
        playerSlot = client.slot
        this.someInteger = someInteger
        this.someBoolean = someBoolean
        this.someString = someString
        this.someContent = someContent

        val writer = PacketWriter(this)
        // Important that it's same order as read in
        writer.putNextByteUnsigned(playerSlot)
        writer.putNextInt(someInteger)
        writer.putNextBoolean(someBoolean)
        writer.putNextString(someString)
        writer.putNextContentPacket(someContent)

        // Examples how to send packets:
//        client.sendPacket(this); // To a single client
//        server.network.sendToAllClients(packet); // To all clients
    }

    override fun processClient(packet: NetworkPacket, client: Client) {
        // Do some stuff with the packet
    }
}