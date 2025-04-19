package at.hannibal2.skyhanni.api

import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.data.jsonobjects.repo.EliteAPISettingsJson
import at.hannibal2.skyhanni.events.RepositoryReloadEvent
import at.hannibal2.skyhanni.events.chat.SkyHanniChatEvent
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@SkyHanniModule
object EliteBotAPI {
    var checkDuration: Duration = 10.minutes
        private set
    var disableFetchingWhenPassed: Boolean = false
        private set
    var disableRefreshCommand: Boolean = false
        private set

    var profileID: UUID? = null
        private set

    @HandleEvent
    fun onRepoReload(event: RepositoryReloadEvent) {
        val data = event.getConstant<EliteAPISettingsJson>("EliteAPISettings")
        checkDuration = data.refreshTimeMinutes.minutes
        disableFetchingWhenPassed = data.disableFetchingWhenPassed
        disableRefreshCommand = data.disableRefreshCommand
    }

    @HandleEvent
    fun onChat(event: SkyHanniChatEvent) {
        if (event.message.startsWith("§8Profile ID: ")) {
            val id = event.message.removePrefix("§8Profile ID: ")
            val newID = try {
                UUID.fromString(id)
            } catch (_: Exception) {
                null
            }
            if (profileID != newID) {
                profileID = newID
            }
        }
    }
}
