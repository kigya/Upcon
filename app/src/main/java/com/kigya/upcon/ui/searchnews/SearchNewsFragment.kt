package com.kigya.upcon.ui.searchnews

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kigya.upcon.R
import com.kigya.upcon.adapters.NewsAdapter
import com.kigya.upcon.databinding.FragmentSearchNewsBinding
import com.kigya.upcon.models.NewsResponse
import com.kigya.upcon.ui.NewsViewModel
import com.kigya.upcon.utils.Resource
import com.kigya.upcon.utils.TAG
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.upcon.models.Article
import com.kigya.upcon.utils.mainScopeDelayLaunch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private val viewModel: NewsViewModel by activityViewModels()
    private val viewBinding by viewBinding(FragmentSearchNewsBinding::bind)
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle? ) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            setupRecyclerViewAdapter()

            collectLatestLifecycleFlow(viewModel.searchNews) { response ->
                when (response) {
                    is Resource.Success -> handleSuccessfulResponse(response)
                    is Resource.Error -> handleErrorResponse(response)
                    is Resource.Loading -> handleLoading()
                }
            }
        }

        var job: Job? = null
        viewBinding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = search(editable)
        }

        newsAdapter.setOnItemClickListener(this::navigateUp)

    }

    private fun search(editable: Editable?) = mainScopeDelayLaunch {
        editable?.let { if (editable.isNotEmpty()) viewModel.searchNews(editable.toString()) }
    }

    private fun navigateUp(it: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", it)
        }

        findNavController().navigate(
            R.id.action_searchNewsFragment_to_articleFragment,
            bundle
        )
    }

    private fun FragmentSearchNewsBinding.setupRecyclerViewAdapter() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
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
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun FragmentSearchNewsBinding.handleLoading() {
        showProgressBar()
    }

    private fun hideProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun FragmentSearchNewsBinding.showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun <T> collectLatestLifecycleFlow(flow: Flow<T>, collector: suspend (T) -> Unit) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collector)
            }
        }
    }
}
