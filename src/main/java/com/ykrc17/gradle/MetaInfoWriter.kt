package com.ykrc17.gradle

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

object MetaInfoWriter {

    fun appendMetaInfo(target: Project, ext: MetaPluginExtension, jarTasks: ArrayList<Jar>) {
        jarTasks.forEach { appendMetaInfo(target, ext, it) }
    }

    private fun appendMetaInfo(target: Project, ext: MetaPluginExtension, task: Jar) = target.run {
        val tmpDir = file("$buildDir/tmp/META-INF")

        task.doFirst {
            if (ext.id.isEmpty() || ext.implClass.isEmpty()) {
                throw RuntimeException("metaPlugin.id or metaPlugin.implClass not set")
            }
            file("$tmpDir/gradle-plugins/${ext.id}.properties").apply {
                parentFile.mkdirs()
                writeText("implementation-class=${ext.implClass}")
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