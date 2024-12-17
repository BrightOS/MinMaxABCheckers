data class Move(var player: Player, var inRow: Int, var inCol: Int, var fiRow: Int, var fiCol: Int) {
    override fun toString(): String {
        return "[$inRow, $inCol] -> [$fiRow, $fiCol]"
    }

    fun toPerfectString(state: State): String {
        return "${state.letter[inRow]}${state.number[inCol]} -> ${state.letter[fiRow]}${state.number[fiCol]}"
    }

    override fun equals(other: Any?): Boolean {
        return (other as? Move?)?.let {
            it.inRow == inRow && it.inCol == inCol && it.fiRow == fiRow && it.fiCol == fiCol
        } ?: false
    }

    override fun hashCode(): Int {
        var result = inRow
        result = 31 * result + inCol
        result = 31 * result + fiRow
        result = 31 * result + fiCol
        return result
    }
}