package com.crypto.demo

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory(
            LocalDatabase.getDatabase(this).currencyInfoDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadButton: Button = findViewById(R.id.main_load)
        loadButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                viewModel.fetchCurrencies()
            }
        }

        val sortButton: Button = findViewById(R.id.main_sort)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    sortButton.isEnabled = state.currencyInfoList.isNotEmpty()
                    state.sortDrawableId?.let {
                        val drawable = AppCompatResources.getDrawable(this@MainActivity, it)
                        sortButton.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            drawable,
                            null
                        )
                    }
                }
            }
        }

        sortButton.setOnClickListener { viewModel.toggleSorting() }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frameLayout, CurrencyListFragment.newInstance(), "currencyList")
            .commit()
    }
}
