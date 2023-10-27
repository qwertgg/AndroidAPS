package info.nightscout.androidaps.plugins.pump.insight.descriptors

class CartridgeStatus {

    var isInserted = false
    var cartridgeType: CartridgeType? = null
    var symbolStatus: SymbolStatus? = null
    var remainingAmount = 0.0
}