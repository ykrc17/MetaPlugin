package com.ykrc17.gradle

import org.gradle.api.Action

open class MetaPluginExtension {
    internal val registry = arrayListOf<MetaPluginSpec>()

    fun plugin(action: Action<MetaPluginSpec>) {
        val registry = MetaPluginSpec()
        action.execute(registry)
        if (registry.id.isEmpty() || registry.implClass.isEmpty()) {
            error("`id` and `implClass` must be set")
        }
        this.registry.add(registry)
    }
}