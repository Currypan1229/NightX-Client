package net.aspw.client.features.module.modules.client

import joptsimple.internal.Strings
import net.aspw.client.Client
import net.aspw.client.event.EventTarget
import net.aspw.client.event.PacketEvent
import net.aspw.client.event.UpdateEvent
import net.aspw.client.features.module.Module
import net.aspw.client.features.module.ModuleCategory
import net.aspw.client.features.module.ModuleInfo
import net.aspw.client.utils.ClientUtils
import net.aspw.client.utils.timer.TickTimer
import net.minecraft.network.play.client.C14PacketTabComplete
import net.minecraft.network.play.server.S3APacketTabComplete
import java.util.*

@ModuleInfo(
    name = "Plugins",
    category = ModuleCategory.CLIENT,
    array = false,
    forceNoSound = true
)
class Plugins : Module() {
    private val tickTimer = TickTimer()
    override fun onEnable() {
        if (mc.thePlayer == null) return
        mc.netHandler.addToSendQueue(C14PacketTabComplete("/"))
        tickTimer.reset()
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        tickTimer.update()
        if (tickTimer.hasTimePassed(20)) {
            ClientUtils.displayChatMessage(Client.CLIENT_CHAT + "§cFailed!")
            tickTimer.reset()
            state = false
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (event.packet is S3APacketTabComplete) {
            val plugins: MutableList<String> = ArrayList()
            val commands = event.packet.func_149630_c()
            for (command1 in commands) {
                val command = command1.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (command.size > 1) {
                    val pluginName = command[0].replace("/", "")
                    if (!plugins.contains(pluginName)) plugins.add(pluginName)
                }
            }
            Collections.sort(plugins)
            if (!plugins.isEmpty()) ClientUtils.displayChatMessage(
                Client.CLIENT_CHAT + "§aPlugins §7(§8" + plugins.size + "§7): §c" + Strings.join(
                    plugins.toTypedArray(),
                    "§7, §c"
                )
            ) else ClientUtils.displayChatMessage(Client.CLIENT_CHAT + "§cNo plugins found!")
            state = false
            tickTimer.reset()
        }
    }
}