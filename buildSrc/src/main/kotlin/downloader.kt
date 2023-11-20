import org.apache.http.HttpHeaders
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.gradle.api.Project
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.copyTo
import kotlin.io.path.fileSize
import kotlin.io.path.readText
import org.apache.http.HttpEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.util.EntityUtils
import java.net.URI

private const val HTTP_HEADER_CONTENT_LENGTH = "Content-Length"

fun ensureDownloaded(project: Project, url: String, targetFile: File, authToken: Pair<String, String>? = null) {
  val logger = LoggerFactory.getLogger("downloader")

  val hash = (url + targetFile.path).sha256().substring(0, 12)
  val markerFile = project.rootProject.buildDir / "download-marker-$hash.txt"
  val authProvider = authToken?.let {
    BasicCredentialsProvider().apply {
//      setCredentials(AuthScope("jetbrains.team", 443), UsernamePasswordCredentials(it.first, it.second))
//      setCredentials(AuthScope("packages.jetbrains.team", 443), UsernamePasswordCredentials(it.first, it.second))
    }
  }

  val httpClient = HttpClients.custom()
    .setDefaultCredentialsProvider(authProvider)
    .build()

  if (!targetFile.exists() ||
    !markerFile.exists() ||
    markerFile.readText() != targetFile.length().toString()
  ) {
    try {
      Files.createDirectories(markerFile.toPath().parent)
      Files.createDirectories(targetFile.toPath().parent)

      logger.warn("Downloading $url to $targetFile")

      Files.deleteIfExists(targetFile.toPath())

      val tempFile = Files.createTempFile(targetFile.toPath().parent, targetFile.name, ".tmp")
      try {
        val request = HttpGet(url).apply {
          setHeader(HttpHeaders.USER_AGENT, "Gradle")
        }

        val response = httpClient.execute(request)
        if (response.statusLine.statusCode == 401) {
          error("401 Unauthorized. Probably you need to set up authorization token for $url. Read BUILD.md for details")
        }

        require(response.statusLine.statusCode == 200) {
          val size = try { tempFile.fileSize() } catch (e: Throwable) { -1 }
          when {
            size < 0L -> println("No response body received")
            size == 0L -> println("~~~Empty response~~~")
            size < 2000L -> println("~~~Response~~~\n${tempFile.readText()}\n~~~Response end~~~")
            else -> {
              val tempFile2 = Files.createTempFile(targetFile.toPath().parent, targetFile.name + "_error", ".tmp")
              tempFile.copyTo(tempFile2, overwrite = true)
            }
          }
          "Error downloading $url: non-200 http status code ${response.statusLine.statusCode}\n" +
              response.allHeaders.joinToString("\n") { it.name + ": " + it.value }
        }

        val contentLength = response.getFirstHeader(HTTP_HEADER_CONTENT_LENGTH).value.toLong()
        require(contentLength > 0) {
          "Header '$HTTP_HEADER_CONTENT_LENGTH' is missing or zero for uri '$url'"
        }

        val entity = response.entity
        val outstream = FileOutputStream(tempFile.toFile())
        try {
          entity?.content?.copyTo(outstream)
        } finally {
          outstream.close()
        }

        val fileSize = Files.size(tempFile)
        require(fileSize == contentLength) {
          "Wrong file length after downloading uri '$url' to '$tempFile': expected length $contentLength " +
              "from Content-Length header, but got $fileSize on disk"
        }

        Files.move(tempFile, targetFile.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
      } finally {
        Files.deleteIfExists(tempFile)
      }
// do not cache response
//      markerFile.writeText(targetFile.length().toString())
      logger.warn("Downloaded: $targetFile = $url")
    } catch (t: Throwable) {
      throw IOException("Could not download $url to $targetFile: ${t.message}", t)
    }
  } else {
    logger.warn("UP-TO-DATE: $targetFile = $url")
  }
}

fun uploadFile(file: File, urlString: URI) {
  val httpClient: CloseableHttpClient = HttpClients.createDefault()

  try {
    val httpPost = HttpPost(urlString)

    // Add headers
    httpPost.addHeader("Accept", "*/*")
    httpPost.addHeader("Origin", "https://${urlString.host}")
    httpPost.addHeader("Referer", "https://${urlString.host}/index.html")
    httpPost.addHeader("Accept-Encoding", "gzip, deflate, br")
    httpPost.addHeader("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8")

    // File entity
    val entity: HttpEntity = MultipartEntityBuilder.create()
      .addBinaryBody("pluginFile", file, ContentType.create("application/java-archive"), file.name)
      .build()

    httpPost.entity = entity

    // Execute and get the response
    httpClient.execute(httpPost).use { response: CloseableHttpResponse ->
      val entity: HttpEntity? = response.entity
      entity?.let {
        println(EntityUtils.toString(it))
      }
    }
  } finally {
    httpClient.close()
  }
}