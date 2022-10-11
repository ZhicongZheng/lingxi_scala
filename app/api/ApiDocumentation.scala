package api

import api.endpoints.UserEndpoints
import sttp.apispec.openapi.Info
import sttp.apispec.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter

import javax.inject.{Inject, Singleton}

@Singleton
class ApiDocumentation @Inject() (userEndpoints: UserEndpoints) {

  import userEndpoints._

  private val openApiInfo = Info("Tapir By LingXi", "1.0.0")

  private val openApiDocs = OpenAPIDocsInterpreter().toOpenAPI(List(loginEndpoint), openApiInfo)

  val openApiYaml: String = openApiDocs.toYaml

}
