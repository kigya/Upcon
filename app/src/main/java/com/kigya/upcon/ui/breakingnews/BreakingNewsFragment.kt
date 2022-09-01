package com.kigya.upcon.ui.breakingnews

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.upcon.R
import com.kigya.upcon.adapters.NewsAdapter
import com.kigya.upcon.databinding.FragmentBreakingNewsBinding
import com.kigya.upcon.models.Article
import com.kigya.upcon.models.NewsResponse
import com.kigya.upcon.ui.NewsViewModel
import com.kigya.upcon.utils.Resource
import com.kigya.upcon.utils.TAG
import com.kigya.upcon.utils.collectLatestLifecycleFlow
import com.kigya.upcon.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val viewModel: NewsViewModel by activityViewModels()
    private val viewBinding by viewBinding(FragmentBreakingNewsBinding::bind)
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewAdapter()

        newsAdapter.setOnItemClickListener(this::navigateUp)

        collectLatestLifecycleFlow(viewModel.breakingNews) { response ->
            when (response) {
                is Resource.Success -> handleSuccessfulResponse(response)
                is Resource.Error -> handleErrorResponse(response)
                is Resource.Loading -> handleLoading()
            }
        }
    }

    private fun navigateUp(it: Article) {
        val bundle = Bundle().apply { putSerializable("article", it) }
        Log.d(TAG, bundle.toString())
        findNavController().navigate(
            resId = R.id.action_breakingNewsFragment_to_articleFragment,
            args = bundle
        )
        Log.d(TAG, it.toString())
    }

    private fun handleSuccessfulResponse(response: Resource.Success<NewsResponse>) {
        hideProgressBar()
        response.data?.let {
            newsAdapter.differ.submitList(it.articles)
        }
    }

    private fun handleErrorResponse(response: Resource.Error<*>) {
        hideProgressBar()
        response.message?.let {
            Log.e(TAG, "Error occurred: $it")
            showToast(it)
        }
    }

    private fun handleLoading() {
        showProgressBar()
    }

    private fun hideProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerViewAdapter() {
        newsAdapter = NewsAdapter()
        viewBinding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
