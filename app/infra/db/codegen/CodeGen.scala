package infra.db.codegen
object CodeGen {
  def main(args: Array[String]): Unit =
    slick.codegen.SourceCodeGenerator.main(
      Array(
        "slick.jdbc.PostgresProfile",
        "org.postgresql.Driver",
        "jdbc:postgresql://43.143.185.11:5454/lingxi",
        "/Users/moka/lingxi/lingxi_scala/app/infra/db/codegen",
        "infra.db.codegen",
        "root",
        "i1gcoyCMzkPYNCKHIOwL"
      )
    )

}
