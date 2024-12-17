data class Player(
    var name: String
) {
    override fun equals(other: Any?): Boolean {
        return name == (other as? Player)?.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}