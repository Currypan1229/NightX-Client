package net.aspw.client.visual.hud.element.elements.targets.impl

import net.aspw.client.util.extensions.darker
import net.aspw.client.util.newfont.FontLoaders
import net.aspw.client.util.render.RenderUtils
import net.aspw.client.util.render.Stencil
import net.aspw.client.value.BoolValue
import net.aspw.client.value.FloatValue
import net.aspw.client.visual.font.Fonts
import net.aspw.client.visual.hud.element.Border
import net.aspw.client.visual.hud.element.elements.TargetHud
import net.aspw.client.visual.hud.element.elements.targets.TargetStyle
import net.aspw.client.visual.hud.element.elements.targets.utils.CharRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11

class Chill(inst: TargetHud) : TargetStyle("Chill", inst, true) {

    val chillFontSpeed =
        FloatValue("Chill-FontSpeed", 0.5F, 0.01F, 1F, { targetHudInstance.styleValue.get().equals("chill", true) })
    val chillRoundValue =
        BoolValue("Chill-RoundedBar", true, { targetHudInstance.styleValue.get().equals("chill", true) })

    private val numberRenderer = CharRenderer(false)

    private var calcScaleX = 0F
    private var calcScaleY = 0F
    private var calcTranslateX = 0F
    private var calcTranslateY = 0F

    fun updateData(_a: Float, _b: Float, _c: Float, _d: Float) {
        calcTranslateX = _a
        calcTranslateY = _b
        calcScaleX = _c
        calcScaleY = _d
    }

    override fun drawTarget(entity: EntityPlayer) {
        updateAnim(entity.health)

        val name = entity.name
        val health = entity.health
        val tWidth = (45F + FontLoaders.SF20.getStringWidth(name)
            .coerceAtLeast(FontLoaders.SF24.getStringWidth(decimalFormat.format(health)))).coerceAtLeast(120F)
        val playerInfo = mc.netHandler.getPlayerInfo(entity.uniqueID)

        // background
        RenderUtils.drawRoundedRect(0F, 0F, tWidth, 48F, 7F, targetHudInstance.bgColor.rgb)
        GlStateManager.resetColor()
        GL11.glColor4f(1F, 1F, 1F, 1F)

        // head
        if (playerInfo != null) {
            Stencil.write(false)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            RenderUtils.fastRoundedRect(4F, 4F, 34F, 34F, 7F)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            Stencil.erase(true)
            drawHead(playerInfo.locationSkin, 4, 4, 30, 30, 1F - targetHudInstance.getFadeProgress())
            Stencil.dispose()
        }

        GlStateManager.resetColor()
        GL11.glColor4f(1F, 1F, 1F, 1F)

        // name + health
        FontLoaders.SF20.drawString(name, 38F, 6F, getColor(-1).rgb)
        numberRenderer.renderChar(
            health,
            calcTranslateX,
            calcTranslateY,
            38F,
            17F,
            calcScaleX,
            calcScaleY,
            false,
            chillFontSpeed.get(),
            getColor(-1).rgb
        )

        // health bar
        RenderUtils.drawRoundedRect(4F, 38F, tWidth - 4F, 44F, 3F, targetHudInstance.barColor.darker(0.5F).rgb)

        Stencil.write(false)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        RenderUtils.fastRoundedRect(4F, 38F, tWidth - 4F, 44F, 3F)
        GL11.glDisable(GL11.GL_BLEND)
        Stencil.erase(true)
        if (chillRoundValue.get())
            RenderUtils.customRounded(
                4F,
                38F,
                4F + (easingHealth / entity.maxHealth) * (tWidth - 8F),
                44F,
                0F,
                3F,
                3F,
                0F,
                targetHudInstance.barColor.rgb
            )
        else
            RenderUtils.drawRect(
                4F,
                38F,
                4F + (easingHealth / entity.maxHealth) * (tWidth - 8F),
                44F,
                targetHudInstance.barColor.rgb
            )
        Stencil.dispose()
    }

    override fun handleBlur(entity: EntityPlayer) {
        val tWidth = (45F + FontLoaders.SF20.getStringWidth(entity.name)
            .coerceAtLeast(FontLoaders.SF24.getStringWidth(decimalFormat.format(entity.health)))).coerceAtLeast(120F)
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderUtils.fastRoundedRect(0F, 0F, tWidth, 48F, 7F)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    override fun handleShadowCut(entity: EntityPlayer) = handleBlur(entity)

    override fun handleShadow(entity: EntityPlayer) {
        val tWidth = (45F + FontLoaders.SF20.getStringWidth(entity.name)
            .coerceAtLeast(FontLoaders.SF24.getStringWidth(decimalFormat.format(entity.health)))).coerceAtLeast(120F)
        RenderUtils.originalRoundedRect(0F, 0F, tWidth, 48F, 7F, shadowOpaque.rgb)
    }

    override fun getBorder(entity: EntityPlayer?): Border {
        entity ?: return Border(0F, 0F, 120F, 48F)
        val tWidth = (45F + FontLoaders.SF20.getStringWidth(entity.name)
            .coerceAtLeast(FontLoaders.SF24.getStringWidth(decimalFormat.format(entity.health)))).coerceAtLeast(120F)
        return Border(0F, 0F, tWidth, 48F)
    }

}