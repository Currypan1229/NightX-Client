package net.aspw.client.injection.forge.mixins.render;

import net.aspw.client.Launch;
import net.aspw.client.features.api.PacketManager;
import net.aspw.client.features.module.impl.player.LegitScaffold;
import net.aspw.client.features.module.impl.player.Scaffold;
import net.aspw.client.features.module.impl.visual.Animations;
import net.aspw.client.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.UUID;

@Mixin(LayerHeldItem.class)
public class MixinLayerHeldItem {

    @Shadow
    @Final
    private RendererLivingEntity<?> livingEntityRenderer;

    /**
     * @author As_pw
     * @reason Render Layer
     */
    @Overwrite
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        ItemStack itemstack;

        if (Objects.requireNonNull(Launch.moduleManager.getModule(Scaffold.class)).getState() && entitylivingbaseIn == MinecraftInstance.mc.thePlayer)
            itemstack = MinecraftInstance.mc.thePlayer.inventory.getStackInSlot(Objects.requireNonNull(Launch.moduleManager.getModule(Scaffold.class)).getLastSlot());
        else if (Objects.requireNonNull(Launch.moduleManager.getModule(LegitScaffold.class)).getState() && entitylivingbaseIn == MinecraftInstance.mc.thePlayer)
            itemstack = MinecraftInstance.mc.thePlayer.inventory.getStackInSlot(Objects.requireNonNull(Launch.moduleManager.getModule(LegitScaffold.class)).getLastSlot());
        else itemstack = entitylivingbaseIn.getHeldItem();

        if (itemstack != null) {
            GlStateManager.pushMatrix();

            if (this.livingEntityRenderer.getMainModel().isChild) {
                float f = 0.5F;
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                GlStateManager.scale(f, f, f);
            }

            Item item = itemstack.getItem();
            final UUID uuid = entitylivingbaseIn.getUniqueID();
            final EntityPlayer entityplayer = MinecraftInstance.mc.theWorld.getPlayerEntityByUUID(uuid);

            if (!Animations.thirdPersonBlockingValue.get().equals("Off") && entityplayer != null && Animations.thirdPersonBlockingValue.get().equals("1.7") && (entityplayer.isBlocking() || PacketManager.isVisualBlocking)) {
                ((ModelBiped) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0325F);
                if (entitylivingbaseIn.isSneaking())
                    GlStateManager.translate(-0.58F, 0.3F, -0.2F);
                else GlStateManager.translate(-0.48F, 0.2F, -0.2F);
                GlStateManager.rotate(-24390.0F, 137290.0F, -2009900.0F, -2054900.0F);
            } else {
                ((ModelBiped) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F);
            }

            GlStateManager.translate(-0.0625F, 0.4375F, 0.0625F);

            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer) entitylivingbaseIn).fishEntity != null) {
                itemstack = new ItemStack(Items.fishing_rod, 0);
            }

            Minecraft minecraft = MinecraftInstance.mc;

            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2) {
                GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
                GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                float f1 = 0.375F;
                GlStateManager.scale(-f1, -f1, f1);
            }

            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.203125F, 0.0F);
            }

            minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }
}