package net.aspw.client.injection.forge.mixins.render;

import net.aspw.client.utils.RotationUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.objectweb.asm.Opcodes.PUTFIELD;

@Mixin(RenderManager.class)
public class MixinRenderManager {

    @Redirect(method = "cacheActiveRenderInfo", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/RenderManager;playerViewX:F", opcode = PUTFIELD))
    public void getPlayerViewX(RenderManager renderManager, float value) {
        renderManager.playerViewX = RotationUtils.perspectiveToggled ? RotationUtils.cameraPitch : value;
    }

    @Redirect(method = "cacheActiveRenderInfo", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/RenderManager;playerViewY:F", opcode = PUTFIELD))
    public void getPlayerViewY(RenderManager renderManager, float value) {
        renderManager.playerViewY = RotationUtils.perspectiveToggled ? RotationUtils.cameraYaw : value;
    }
}