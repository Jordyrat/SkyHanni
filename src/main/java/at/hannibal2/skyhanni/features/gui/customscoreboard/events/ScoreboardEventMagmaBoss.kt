package at.hannibal2.skyhanni.features.gui.customscoreboard.events

import at.hannibal2.skyhanni.data.HypixelData
import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.features.gui.customscoreboard.CustomScoreboardUtils.getSBLines
import at.hannibal2.skyhanni.features.gui.customscoreboard.ScoreboardPattern
import at.hannibal2.skyhanni.utils.LorenzUtils.isInIsland
import at.hannibal2.skyhanni.utils.RegexUtils.allMatches
import at.hannibal2.skyhanni.utils.RegexUtils.matches

// scoreboard
// scoreboard update event
object ScoreboardEventMagmaBoss : ScoreboardEvent() {

    override fun getDisplay() = elementPatterns.allMatches(getSBLines())

    override fun showWhen() = ScoreboardPattern.magmaChamberPattern.matches(HypixelData.skyBlockArea)

    override val configLine = "§7(All Magma Boss Lines)\n§7Boss: §c0%\n§7Damage Soaked:\n§e▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎§7▎▎▎▎▎"

    override val elementPatterns = listOf(
        ScoreboardPattern.magmaBossPattern,
        ScoreboardPattern.damageSoakedPattern,
        ScoreboardPattern.killMagmasPattern,
        ScoreboardPattern.killMagmasDamagedSoakedBarPattern,
        ScoreboardPattern.reformingPattern,
        ScoreboardPattern.bossHealthPattern,
        ScoreboardPattern.bossHealthBarPattern,
    )

    override fun showIsland() = IslandType.CRIMSON_ISLE.isInIsland()
}
