package api

import api.endpoints.{FileEndpoints, UserEndpoints}
import sttp.apispec.openapi.Info
import sttp.apispec.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter

import javax.inject.Singleton

@Singleton
class ApiDocumentation {

  private val openApiInfo = Info("Tapir By LingXi", "1.0.0")

  private val openApiDocs = OpenAPIDocsInterpreter().toOpenAPI(
    List(
      UserEndpoints.loginEndpoint,
      UserEndpoints.logoutEndpoint,
      UserEndpoints.currentUserEndpoint,
      UserEndpoints.listByPageEndpoint,
      UserEndpoints.deleteUserEndpoint,
      UserEndpoints.createUserEndpoint,
      UserEndpoints.changePwdEndpoint,
      FileEndpoints.uploadEndpoint
    ),
    openApiInfo
  )

  val openApiYaml: String = openApiDocs.toYaml

}
