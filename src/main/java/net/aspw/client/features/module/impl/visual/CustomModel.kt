package net.aspw.client.features.module.impl.visual

import net.aspw.client.features.module.Module
import net.aspw.client.features.module.ModuleCategory
import net.aspw.client.features.module.ModuleInfo
import net.aspw.client.value.ListValue

@ModuleInfo(name = "CustomModel", spacedName = "Custom Model", category = ModuleCategory.VISUAL)
class CustomModel : Module() {
    val mode = ListValue("Mode", arrayOf("Imposter", "Rabbit", "Freddy"), "Imposter")

    override val tag: String
        get() = mode.get()
}