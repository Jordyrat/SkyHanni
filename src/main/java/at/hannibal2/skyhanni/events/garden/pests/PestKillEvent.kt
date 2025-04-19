package at.hannibal2.skyhanni.events.garden.pests

import at.hannibal2.skyhanni.api.event.SkyHanniEvent
import at.hannibal2.skyhanni.features.garden.pests.PestType
import at.hannibal2.skyhanni.utils.NeuInternalName

class PestKillEvent(val pestType: PestType, val item: NeuInternalName, val amount: Int) : SkyHanniEvent() {
    var blockedReason: String? = null
}
