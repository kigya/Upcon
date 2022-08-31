package com.kigya.upcon.ui.article

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import com.kigya.upcon.ui.NewsViewModel
import com.kigya.upcon.utils.viewbinding.ViewBindingFragment
import com.google.android.material.snackbar.Snackbar
import com.kigya.upcon.databinding.FragmentArticleBinding
import androidx.navigation.fragment.navArgs
import com.kigya.upcon.models.Article

class ArticleFragment :
    ViewBindingFragment<FragmentArticleBinding>(FragmentArticleBinding::inflate) {

    private val viewModel: NewsViewModel by activityViewModels()
    private val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article: Article = args.article

        binding.apply {

            webView.apply {
                webViewClient = WebViewClient()
                article.url?.let { loadUrl(it) }
            }

            fab.setOnClickListener {
                viewModel.saveArticle(article)
                Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
