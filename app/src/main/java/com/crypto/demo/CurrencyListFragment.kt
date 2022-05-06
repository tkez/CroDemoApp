package com.crypto.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CurrencyListFragment : Fragment() {

    companion object {
        fun newInstance() = CurrencyListFragment()
    }

    private val viewModel: MainActivityViewModel by activityViewModels {
        MainActivityViewModelFactory(
            LocalDatabase.getDatabase(requireActivity().applicationContext).currencyInfoDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.currency_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CurrencyListAdapter()

        val recyclerView: RecyclerView = view.findViewById(R.id.currency_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    adapter.update(it.currencyInfoList)
                }
            }
        }
    }
}