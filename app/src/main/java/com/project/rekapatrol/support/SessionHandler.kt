package com.project.rekapatrol.support

object SessionHandler {
    var onSessionExpired: (() -> Unit)? = null

    fun triggerSessionExpired() {
        onSessionExpired?.invoke()
    }
 }