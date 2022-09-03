package com.kigya.upcon.ui

import android.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kigya.upcon.models.Article
import com.kigya.upcon.models.NewsResponse
import com.kigya.upcon.repository.NewsRepository
import com.kigya.upcon.utils.Resource
import com.kigya.upcon.utils.mainScopeDelayLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

typealias NewsResourceFlow = MutableStateFlow<Resource<NewsResponse>>

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    private val _breakingNews: NewsResourceFlow =
        MutableStateFlow(Resource.Loading())
    val breakingNews = _breakingNews.asStateFlow()

    private val _searchNews: NewsResourceFlow =
        MutableStateFlow(Resource.Loading())
    val searchNews = _searchNews.asStateFlow()

    private var _breakingNewsPage = 1
    private var _searchNewsPage = 1
    private var job: Job? = null

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            _breakingNews.value = Resource.Loading()
            val response = newsRepository.getBreakingNews(countryCode, _breakingNewsPage)
            _breakingNews.value = handleNewsResponse(response)
        }
    }

    private fun searchNews(query: String) {
        viewModelScope.launch {
            _searchNews.value = Resource.Loading()
            val response = newsRepository.searchNews(query, _searchNewsPage)
            _searchNews.value = handleNewsResponse(response)
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.upsertArticle(article)
        }
    }

    fun getSavedNewsFlow() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
        }
    }

    private fun handleNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun search(string: String?) = mainScopeDelayLaunch {
        string?.let { if (string.isNotEmpty()) searchNews(string) }
    }

    val listener = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            job?.cancel()
            job = search(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            job?.cancel()
            job = search(newText)
            return true
        }
    }
}
