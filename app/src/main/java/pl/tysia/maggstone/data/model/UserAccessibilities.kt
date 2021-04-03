package pl.tysia.maggstone.data.model

import pl.tysia.maggstone.constants.MenuTileType

class UserAccessibilities {
    var accessibleTiles : List<MenuTileType> = MenuTileType.values().toCollection(ArrayList())
}