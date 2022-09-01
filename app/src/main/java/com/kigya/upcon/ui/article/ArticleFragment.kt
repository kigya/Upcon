package com.kigya.upcon.ui.article

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kigya.upcon.ui.NewsViewModel
import com.kigya.upcon.databinding.FragmentArticleBinding
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.upcon.R
import com.kigya.upcon.utils.showToast

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private val viewModel: NewsViewModel by activityViewModels()
    private val viewBinding by viewBinding(FragmentArticleBinding::bind)
    private val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            setupWebView()
            fab.setOnClickListener { addToFavorites() }
        }
    }

    private fun addToFavorites() {
        viewModel.saveArticle(args.article)
        showToast(getString(R.string.article_saved))
    }

    private fun FragmentArticleBinding.setupWebView() {
        webView.apply {
            webViewClient = WebViewClient()
            args.article.url?.let { loadUrl(it) }
        }
    }
}
