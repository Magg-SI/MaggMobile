package pl.tysia.maggstone.constants

enum class UserTypes(val value : Int) {
    MAIN_MAGAZINE_WORKER(0),
    OTHER_MAGAZINE_WORKER(1);

    companion object {
        fun fromInt(value: Int) = UserTypes.values().first { it.value == value }
    }
}