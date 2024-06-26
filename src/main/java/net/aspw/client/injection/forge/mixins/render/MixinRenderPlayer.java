package net.aspw.client.injection.forge.mixins.render;

import net.aspw.client.Launch;
import net.aspw.client.features.module.impl.visual.CustomModel;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {

    private final ResourceLocation rabbit = new ResourceLocation("client/models/rabbit.png");
    private final ResourceLocation fred = new ResourceLocation("client/models/freddy.png");
    private final ResourceLocation imposter = new ResourceLocation("client/models/imposter.png");

    @Inject(method = {"getEntityTexture"}, at = {@At("HEAD")}, cancellable = true)
    public void getEntityTexture(AbstractClientPlayer entity, CallbackInfoReturnable<ResourceLocation> ci) {
        final CustomModel customModel = Objects.requireNonNull(Launch.moduleManager.getModule(CustomModel.class));

        if (customModel.getState()) {
            if (customModel.getMode().get().contains("Rabbit")) {
                ci.setReturnValue(rabbit);
            }
            if (customModel.getMode().get().contains("Freddy")) {
                ci.setReturnValue(fred);
            }
            if (customModel.getMode().get().contains("Imposter")) {
                ci.setReturnValue(imposter);
            }
        }
    }
}