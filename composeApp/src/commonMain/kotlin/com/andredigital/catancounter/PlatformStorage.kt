package com.andredigital.catancounter


expect fun storeId(id: String)

expect fun retrieveId(): String?

expect fun storeStateData(state: String)
expect fun retrieveStateData(): String?