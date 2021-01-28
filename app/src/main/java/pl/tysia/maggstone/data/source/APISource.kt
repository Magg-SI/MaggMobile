package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager

open class APISource(netAddressManager: NetAddressManager) {
    protected var BASE_URL : String = netAddressManager.getNetAddress()

}