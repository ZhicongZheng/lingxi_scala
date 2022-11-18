package infra.oss

import com.aliyun.oss.{OSS, OSSClientBuilder}
import play.api.{Configuration, Logging}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream}
import java.util.zip.{CRC32, CheckedInputStream}
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait OssRepository {

  def upload(input: InputStream, fileName: String): Future[String]

  def extension(fileName: String): String = {
    val index = fileName.indexOf('.')
    if (index > 0) fileName.substring(index + 1) else fileName
  }

  def inputCheckSum(input: InputStream): (Long, InputStream) = {
    val buffer                                 = Array.ofDim[Byte](1024)
    val checkedInputStream: CheckedInputStream = new CheckedInputStream(input, new CRC32)
    val outStream                              = new ByteArrayOutputStream()
    def read: Int                              = checkedInputStream.read(buffer, 0, buffer.length)

    var len = read
    while (len > 1) {
      outStream.write(buffer, 0, len)
      len = read
    }
    outStream.flush()
    checkedInputStream.close()
    (checkedInputStream.getChecksum.getValue, new ByteArrayInputStream(outStream.toByteArray))
  }
}

@Singleton
class AliyunOssRepository @Inject() (config: Configuration) extends OssRepository with Logging {

  lazy val ossConfig: OssConfig = config.get[OssConfig]("oss.aliyun")

  lazy val ossClient: OSS = new OSSClientBuilder().build(ossConfig.endpoint, ossConfig.accessKeyId, ossConfig.accessKeySecret)

  override def upload(input: InputStream, fileName: String): Future[String] = {
    val tuple = inputCheckSum(input)
    val path  = s"${extension(fileName)}/${tuple._1}-$fileName"
    Future {
      Try(ossClient.putObject(ossConfig.bucketName, path, tuple._2))
    } flatMap {
      case Success(_) => Future.successful(path)
      case Failure(ex) =>
        logger.error("upload error ", ex)
        Future.failed(ex)
    }
  }
}
