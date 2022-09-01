package com.kigya.upcon.ui.savednews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kigya.upcon.R
import com.kigya.upcon.adapters.NewsAdapter
import com.kigya.upcon.databinding.FragmentSavedNewsBinding
import com.kigya.upcon.ui.NewsViewModel
import com.kigya.upcon.utils.collectLatestLifecycleFlow
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.upcon.models.Article
import com.kigya.upcon.utils.ui.SwipeHelper
import com.kigya.upcon.utils.showToast

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    private val viewModel: NewsViewModel by activityViewModels()
    private val viewBinding by viewBinding(FragmentSavedNewsBinding::bind)
    private lateinit var newsAdapter: NewsAdapter

    private val swipeHelper = object : SwipeHelper() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            removeArticle(viewHolder)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewAdapter()
        newsAdapter.setOnItemClickListener(this::navigateUp)
        attachHelper()
        collectLatestLifecycleFlow(viewModel.getSavedNewsFlow()) { articles ->
            newsAdapter.differ.submitList(articles)
        }
    }

    private fun attachHelper() {
        ItemTouchHelper(swipeHelper).apply {
            attachToRecyclerView(viewBinding.rvSavedNews)
        }
    }

    private fun navigateUp(it: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", it)
        }
        findNavController().navigate(
            R.id.action_savedNewsFragment_to_articleFragment,
            bundle
        )
    }

    private fun setupRecyclerViewAdapter() {
        newsAdapter = NewsAdapter()
        viewBinding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun removeArticle(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.absoluteAdapterPosition
        val article = newsAdapter.differ.currentList[position]
        viewModel.deleteArticle(article)
        showToast(getString(R.string.article_removed))
    }
}
