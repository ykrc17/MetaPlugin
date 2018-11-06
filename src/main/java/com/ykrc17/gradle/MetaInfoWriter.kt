package com.ykrc17.gradle

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

object MetaInfoWriter {

    fun appendMetaInfo(target: Project, ext: MetaPluginExtension, jarTasks: ArrayList<Jar>) {
        jarTasks.forEach { appendMetaInfo(target, ext, it) }
    }

    private fun appendMetaInfo(target: Project, ext: MetaPluginExtension, task: Jar) = target.run {
        val tmpDir = file("$buildDir/tmp/META-INF")

        task.doFirst { _ ->
            if (ext.registry.isEmpty()) {
                error("use `metaPlugin.plugin()` to register plugin")
            }
            ext.registry.forEach { registry ->
                file("$tmpDir/gradle-plugins/${registry.id}.properties").apply {
                    parentFile.mkdirs()
                    writeText("implementation-class=${registry.implClass}")
                }
            }
        }
        task.metaInf {
            it.from(tmpDir)
        }
        task.doLast {
            delete(tmpDir)
        }
        Unit
    }
}