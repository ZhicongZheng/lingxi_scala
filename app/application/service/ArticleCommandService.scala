package application.service

import com.google.inject.Inject
import domain.article.ArticleRepository

import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ArticleCommandService @Inject() (articleRepository: ArticleRepository) {}
