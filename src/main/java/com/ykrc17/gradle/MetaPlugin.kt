package com.ykrc17.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.jvm.tasks.Jar

@Suppress("unused")
open class MetaPlugin : Plugin<Project> {

    override fun apply(target: Project) = target.run {
        checkGradleVersion()
        val jarTasks = checkJarTask()
        
        val ext = extensions.create("metaPlugin", MetaPluginExtension::class.java)
        MetaInfoWriter.appendMetaInfo(target, ext, jarTasks)

        dependencies.add("compileOnly", dependencies.gradleApi())
        Unit
    }

    private fun Project.checkGradleVersion() {
        val gradleVersion = gradle.gradleVersion
        val gradleVersionSplits = gradleVersion.split(".")
        if (Integer.parseInt(gradleVersionSplits[0]) < 3) {
            throw RuntimeException(String.format("Minimum supported Gradle version is 3.0, current is %s", gradleVersion))
        }
    }

    private fun Project.checkJarTask(): ArrayList<Jar> {
        val jarTasks = arrayListOf<Jar>()
        try {
            jarTasks.add(tasks.getByName("jar") as Jar)
        } catch (e: UnknownTaskException) {

        }
        try {
            jarTasks.add(tasks.getByName("shadowJar") as Jar)
        } catch (e: UnknownTaskException) {

        }
        if (jarTasks.isEmpty()) {
            throw RuntimeException("Need at least one of the plugins: java, shadowJar")
        }
        return jarTasks
    }
}