package com.andredigital.catancounter

import kotlinx.browser.localStorage


actual fun storeId(id: String) {
    localStorage.setItem("id", id)
}

actual fun retrieveId(): String? {
    return localStorage.getItem("id")
}

actual fun storeStateData(state: String) {
    localStorage.setItem("data", state)
}

actual fun retrieveStateData(): String? {
    return localStorage.getItem("data")
}