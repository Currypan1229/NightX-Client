package net.aspw.client.injection.forge.mixins.gui;

import net.aspw.client.features.api.PacketManager;
import net.aspw.client.utils.ServerUtils;
import net.aspw.client.visual.font.semi.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public abstract class MixinGuiIngameMenu extends MixinGuiScreen {

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        this.buttonList.add(new GuiButton(1337, this.width / 2 - 100, this.height / 4 + 128, "Reconnect"));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        if (button.id == 1337 && !this.mc.isIntegratedServerRunning()) {
            mc.theWorld.sendQuittingDisconnectingPacket();
            ServerUtils.connectToLastServer();
        }
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void drawScreen(CallbackInfo callbackInfo) {
        try {
            Fonts.minecraftFont.drawStringWithShadow("§7Username: §d" + mc.getSession().getUsername(), 6f, 6f, 0xffffff);

            Fonts.minecraftFont.drawStringWithShadow("§7Ping: §d" + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "ms", 6f, 16f, 0xffffff);

            Fonts.minecraftFont.drawStringWithShadow("§7Packets Sent: §d" + PacketManager.sendPacketCounts, 6f, 26f, 0xffffff);
            Fonts.minecraftFont.drawStringWithShadow("§7Packets Received: §d" + PacketManager.receivePacketCounts, 6f, 36f, 0xffffff);

            Fonts.minecraftFont.drawStringWithShadow("§7LastTPPos: §dX: " + PacketManager.lastTpX + ", Y: " + PacketManager.lastTpY + ", Z: " + PacketManager.lastTpZ, 6f, 46f, 0xffffff);
        } catch (final Exception ignored) {
        }
    }
}