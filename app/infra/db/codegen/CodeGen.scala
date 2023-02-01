package infra.db.codegen
object CodeGen {
  def main(args: Array[String]): Unit =
    slick.codegen.SourceCodeGenerator.main(
      Array(
        "slick.jdbc.PostgresProfile",
        "org.postgresql.Driver",
        "",
        "/Users/moka/lingxi/lingxi_scala/app/infra/db/codegen",
        "infra.db.codegen",
        "root",
        ""
      )
    )

}
