package com.kigya.upcon.db

import androidx.room.*
import com.kigya.upcon.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Upsert
    suspend fun upsertArticle(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}
