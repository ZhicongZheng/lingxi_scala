package interfaces.api

import interfaces.api.endpoints.{ArticleEndpoints, CommentEndpoints, FileEndpoints, RoleEndpoints, SiteEndpoints, UserEndpoints}
import sttp.apispec.openapi.Info
import sttp.apispec.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter

import javax.inject.Singleton

@Singleton
class ApiDocumentation {

  private val openApiInfo = Info("Tapir By ZhengZhiCong", "1.0.0")

  val endpoints =
    UserEndpoints.endpoints ++ RoleEndpoints.endpoints ++ FileEndpoints.endpoints ++ ArticleEndpoints.endpoints ++ SiteEndpoints.endpoints ++ CommentEndpoints.endpoints

  private val openApiDocs = OpenAPIDocsInterpreter().toOpenAPI(endpoints, openApiInfo)

  val openApiYaml: String = openApiDocs.toYaml

}
