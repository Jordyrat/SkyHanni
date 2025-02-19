package at.hannibal2.skyhanni.features.misc

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.events.DebugDataCollectEvent
import at.hannibal2.skyhanni.events.GuiRenderEvent
import at.hannibal2.skyhanni.events.chat.SkyHanniChatEvent
import at.hannibal2.skyhanni.events.minecraft.WorldChangeEvent
import at.hannibal2.skyhanni.features.garden.SensitivityReducer
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.utils.ChatUtils
import at.hannibal2.skyhanni.utils.RegexUtils.matches
import at.hannibal2.skyhanni.utils.RenderUtils.renderString
import at.hannibal2.skyhanni.utils.repopatterns.RepoPattern
import net.minecraft.client.Minecraft

@SkyHanniModule
object LockMouseLook {

    /**
     * REGEX-TEST: §aTeleported you to §r§aPlot
     */
    private val gardenTeleportPattern by RepoPattern.pattern(
        "chat.garden.teleport",
        "§aTeleported you to .*",
    )

    private val config get() = SkyHanniMod.feature.misc
    private val storage get() = SkyHanniMod.feature.storage
    var lockedMouse = false
    private const val lockedPosition = -1F / 3F

    @HandleEvent
    fun onWorldChange(event: WorldChangeEvent) {
        if (lockedMouse) toggleLock()
        val gameSettings = Minecraft.getMinecraft().gameSettings
        if (gameSettings.mouseSensitivity == lockedPosition) {
            gameSettings.mouseSensitivity = storage.savedMouselockedSensitivity
            ChatUtils.chat("§bMouse rotation is now unlocked because you left it locked.")
        }
    }

    @HandleEvent
    fun onChat(event: SkyHanniChatEvent) {
        if (!gardenTeleportPattern.matches(event.message)) return
        if (lockedMouse) toggleLock()
    }

    fun toggleLock() {
        lockedMouse = !lockedMouse

        val gameSettings = Minecraft.getMinecraft().gameSettings
        var mouseSensitivity = gameSettings.mouseSensitivity
        if (SensitivityReducer.isEnabled()) mouseSensitivity = SensitivityReducer.doTheMath(mouseSensitivity, true)

        if (lockedMouse) {
            storage.savedMouselockedSensitivity = mouseSensitivity
            gameSettings.mouseSensitivity = lockedPosition
            if (config.lockMouseLookChatMessage) {
                ChatUtils.chat("§bMouse rotation is now locked. Type /shmouselock to unlock your rotation")
            }
        } else {
            if (!SensitivityReducer.isEnabled()) gameSettings.mouseSensitivity = storage.savedMouselockedSensitivity
            else gameSettings.mouseSensitivity = SensitivityReducer.doTheMath(storage.savedMouselockedSensitivity)
            if (config.lockMouseLookChatMessage) {
                ChatUtils.chat("§bMouse rotation is now unlocked.")
            }
        }
    }

    @HandleEvent
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        if (!lockedMouse) return
        config.lockedMouseDisplay.renderString("§eMouse Locked", posLabel = "Mouse Locked")
    }

    fun autoDisable() {
        if (lockedMouse) {
            toggleLock()
        }
    }

    @HandleEvent
    fun onDebug(event: DebugDataCollectEvent) {
        event.title("Mouse Lock")

        if (!lockedMouse) {
            event.addIrrelevant("not enabled")
            return
        }

        event.addData {
            add("Stored Sensitivity: ${storage.savedMouselockedSensitivity}")
        }
    }
}
