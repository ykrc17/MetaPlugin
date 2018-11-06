package com.ykrc17.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.jvm.tasks.Jar

@Suppress("unused")
open class MetaPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        validateGradleVersion(target)
        val jarTasks = getJarTasks()

        val ext = extensions.create<MetaPluginExtension>("metaPlugin")
        MetaInfoWriter.appendMetaInfo(target, ext, jarTasks)

        dependencies.add("compileOnly", dependencies.gradleApi())
        Unit
    }

    private fun validateGradleVersion(target: Project) {
        val gradleVersion = target.gradle.gradleVersion
        val gradleVersionSplits = gradleVersion.split(".")
        if (Integer.parseInt(gradleVersionSplits[0]) < 3) {
            error(kotlin.String.format("Minimum supported Gradle version is 3.0, current is %s", gradleVersion))
        }
    }

    private fun Project.getJarTasks(): ArrayList<Jar> {
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
            error("Need at least one of the plugins: java, shadowJar")
        }
        return jarTasks
    }
}