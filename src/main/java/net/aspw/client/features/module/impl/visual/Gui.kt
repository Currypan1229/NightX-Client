package net.aspw.client.features.module.impl.visual

import net.aspw.client.Launch
import net.aspw.client.features.module.Module
import net.aspw.client.features.module.ModuleCategory
import net.aspw.client.features.module.ModuleInfo
import net.aspw.client.utils.render.ColorUtils.LiquidSlowly
import net.aspw.client.utils.render.ColorUtils.fade
import net.aspw.client.utils.render.RenderUtils
import net.aspw.client.value.BoolValue
import net.aspw.client.value.FloatValue
import net.aspw.client.value.IntegerValue
import net.aspw.client.value.ListValue
import net.aspw.client.visual.client.clickgui.dropdown.style.styles.DropDown
import net.aspw.client.visual.client.clickgui.smooth.SmoothClickGui
import net.aspw.client.visual.client.clickgui.tab.NewUi
import org.lwjgl.input.Keyboard
import java.awt.Color
import java.util.*

@ModuleInfo(
    name = "Gui",
    category = ModuleCategory.VISUAL,
    keyBind = Keyboard.KEY_RSHIFT,
    forceNoSound = true,
    onlyEnable = true,
    array = false
)
class Gui : Module() {
    private val styleValue: ListValue = object : ListValue(
        "Style",
        arrayOf("Smooth", "DropDown", "Tab"),
        "Smooth"
    ) {
    }

    val guiBlur = BoolValue("Blur", true)

    @JvmField
    val picturesValue = BoolValue("Pictures", true) { styleValue.get() == "Smooth" }

    @JvmField
    val animationValue = ListValue("Animation", arrayOf("None", "Zoom"), "Zoom") { styleValue.get() == "DropDown" }

    @JvmField
    val scaleValue =
        FloatValue("Scale", 1.0f, 0.4f, 2f) { styleValue.get() == "DropDown" || styleValue.get() == "Smooth" }

    override fun onEnable() {
        Launch.clickGui.progress = 0.0
        Launch.clickGui.slide = 0.0
        Launch.clickGui.lastMS = System.currentTimeMillis()
        mc.displayGuiScreen(Launch.clickGui)
        when (styleValue.get().lowercase(Locale.getDefault())) {
            "smooth" -> mc.displayGuiScreen(SmoothClickGui())

            "dropdown" -> Launch.clickGui.style =
                DropDown()

            "tab" -> mc.displayGuiScreen(NewUi.getInstance())
        }
    }

    private val colorModeValue = ListValue(
        "Color",
        arrayOf("Custom", "Sky", "Rainbow", "LiquidSlowly", "Fade"),
        "Sky"
    ) { styleValue.get() == "DropDown" || styleValue.get() == "Tab" }
    private val colorRedValue =
        IntegerValue("Red", 255, 0, 255) { styleValue.get() == "DropDown" || styleValue.get() == "Tab" }
    private val colorGreenValue =
        IntegerValue("Green", 255, 0, 255) { styleValue.get() == "DropDown" || styleValue.get() == "Tab" }
    private val colorBlueValue =
        IntegerValue("Blue", 255, 0, 255) { styleValue.get() == "DropDown" || styleValue.get() == "Tab" }
    private val saturationValue =
        FloatValue("Saturation", 0.4f, 0f, 1f) { styleValue.get() == "DropDown" || styleValue.get() == "Tab" }
    private val brightnessValue =
        FloatValue("Brightness", 1f, 0f, 1f) { styleValue.get() == "DropDown" || styleValue.get() == "Tab" }
    private val mixerSecondsValue =
        IntegerValue("Seconds", 6, 1, 10) { styleValue.get() == "DropDown" || styleValue.get() == "Tab" }

    fun generateColor(): Color {
        var c = Color(255, 255, 255, 255)
        when (colorModeValue.get().lowercase(Locale.getDefault())) {
            "custom" -> c = Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get())
            "rainbow" -> c = Color(
                RenderUtils.getRainbowOpaque(
                    mixerSecondsValue.get(),
                    saturationValue.get(),
                    brightnessValue.get(),
                    0
                )
            )

            "sky" -> c = RenderUtils.skyRainbow(0, saturationValue.get(), brightnessValue.get())
            "liquidslowly" -> c = LiquidSlowly(System.nanoTime(), 0, saturationValue.get(), brightnessValue.get())
            "fade" -> c = fade(Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()), 0, 100)
        }
        return c
    }
}