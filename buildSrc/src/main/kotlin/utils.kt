
import com.google.common.io.MoreFiles
import com.google.common.io.RecursiveDeleteOption
import org.gradle.api.Project
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest

operator fun File.div(s: String): File = File(this, s).canonicalFile

fun Iterable<File>.toZkmScriptFileList(): String =
  joinToString("\n") { "\"${it.absolutePath}\"" }

@Suppress("UnstableApiUsage")
fun File.deleteDirectoryContents(): Unit =
  MoreFiles.deleteDirectoryContents(toPath(), RecursiveDeleteOption.ALLOW_INSECURE)

fun String.sha256(): String {
  val md = MessageDigest.getInstance("SHA-256")
  val digest = md.digest(toByteArray())
  return digest.fold("") { str, it -> str + "%02x".format(it) }
}