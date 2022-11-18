package infra.oss

import com.typesafe.config.Config
import play.api.ConfigLoader

case class OssConfig(endpoint: String, accessKeyId: String, accessKeySecret: String, bucketName: String)

object OssConfig {

  implicit val OssConfigConfigLoader: ConfigLoader[OssConfig] =
    (rootConfig: Config, path: String) => {
      val config = rootConfig.getConfig(path)
      OssConfig(
        config.getString("endpoint"),
        config.getString("accessKeyId"),
        config.getString("accessKeySecret"),
        config.getString("bucketName")
      )
    }
}
