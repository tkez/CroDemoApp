package com.crypto.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CurrencyListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val currencies: MutableList<CurrencyInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.currency_list_item, parent, false)
        return CurrencyListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CurrencyListViewHolder).bind(currencies[position])
    }

    override fun getItemCount(): Int = currencies.size

    fun update(currencies: List<CurrencyInfo>) {
        val diff = DiffUtil.calculateDiff(CurrencyListDiffCallback(this.currencies, currencies))
        this.currencies.clear()
        this.currencies.addAll(currencies)
        diff.dispatchUpdatesTo(this)
    }

    class CurrencyListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textIcon: TextView = view.findViewById(R.id.currency_icon)
        private val name: TextView = view.findViewById(R.id.currency_name)
        private val symbol: TextView = view.findViewById(R.id.currency_symbol)

        fun bind(currency: CurrencyInfo) {
            textIcon.text = currency.name[0].toString()
            name.text = currency.name
            symbol.text = currency.symbol
        }
    }

    class CurrencyListDiffCallback(
        private val old: List<CurrencyInfo>,
        private val new: List<CurrencyInfo>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].id == new[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].name == new[newItemPosition].name &&
            old[oldItemPosition].symbol == new[newItemPosition].symbol
    }
}