import com.beust.klaxon.JsonReader
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File
import java.io.StringReader
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.TaskAction

abstract class JsonToKotlinArrayTask : DefaultTask() {
    @get:InputFiles abstract val sourceFiles: ConfigurableFileCollection
    @get:OutputDirectories abstract val outputDirectories: ConfigurableFileCollection

    @TaskAction
    fun generateSources() {
        val fileSpecs: MutableList<FileSpec> = mutableListOf()
        sourceFiles.forEach { srcFile -> processFile(srcFile)?.let { fileSpecs.add(it) } }
        outputDirectories.forEach { outputDir -> fileSpecs.forEach { it.writeTo(outputDir) } }
    }

    private fun processFile(sourceFile: File): FileSpec? =
        sourceFile.run {
            if (!name.endsWith(".json")) {
                null
            } else {
                val outputName: String = generateOutputName(name)
                FileSpec.builder("org.sqids", outputName)
                    .addProperty(
                        PropertySpec.builder(
                                outputName.lowercase(),
                                Set::class.parameterizedBy(String::class)
                            )
                            .initializer(buildCodeBlock { readSetFromArray(path) })
                            .build()
                    )
                    .build()
            }
        }

    private fun generateOutputName(originalName: String): String =
        originalName.lowercase().removeSuffix(".json").split("-").joinToString("") { segment ->
            segment.replaceFirstChar { c -> c.uppercase() }
        }

    private fun CodeBlock.Builder.readSetFromArray(path: Any): CodeBlock.Builder = run {
        add("setOf(\n")
        withIndent {
            readJson(path).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        add("%S,\n", reader.nextString())
                    }
                }
            }
        }
        add(")\n")
    }

    private fun readJson(path: Any): JsonReader = JsonReader(StringReader(readString(path)))

    private fun readString(path: Any): String = buildString {
        project.file(path).forEachLine { append(it) }
    }
}
