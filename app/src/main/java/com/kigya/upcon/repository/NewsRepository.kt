package com.kigya.upcon.repository

import com.kigya.upcon.api.NewsAPI
import com.kigya.upcon.db.ArticleDao
import com.kigya.upcon.models.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsAPI,
    private val newsDao: ArticleDao
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        newsApi.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(query: String, pageNumber: Int) =
        newsApi.searchForNews(query, pageNumber)

    suspend fun upsertArticle(article: Article) = newsDao.upsertArticle(article)

    suspend fun deleteArticle(article: Article) = newsDao.deleteArticle(article)

    fun getSavedNews() = newsDao.getAllArticles()

}
