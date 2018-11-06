package com.ykrc17.gradle

import org.gradle.api.plugins.ExtensionContainer

inline fun <reified T> ExtensionContainer.create(name: String): T = create(name, T::class.java)