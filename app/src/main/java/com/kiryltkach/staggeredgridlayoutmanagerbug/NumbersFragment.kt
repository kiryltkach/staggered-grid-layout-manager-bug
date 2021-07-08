package com.kiryltkach.staggeredgridlayoutmanagerbug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Sample fragment that shows numbers
 */
abstract class NumbersFragment : Fragment() {

    private class Adapter : PagingDataAdapter<Int, ViewHolder>(IntDiffer()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = getItem(position).toString()
        }

        override fun onViewRecycled(holder: ViewHolder) {

        }
    }

    private lateinit var adapter: Adapter
    private var adapterSubmissionJob: Job? = null

    private fun startCollecting(data: Flow<PagingData<Int>>) {
        adapterSubmissionJob?.cancel()
        adapterSubmissionJob = viewLifecycleOwner.lifecycleScope.launch {
            data.collectLatest { adapter.submitData(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_numbers, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recycler_view)
        adapter = Adapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = createLayoutManager()

        rootView.findViewById<Button>(R.id.button_numbers).setOnClickListener {
            startCollecting(Repository.getNumbers())
        }

        rootView.findViewById<Button>(R.id.button_empty).setOnClickListener {
            startCollecting(Repository.getEmptyNumbers())
        }

        startCollecting(Repository.getNumbers())

        return rootView
    }

    abstract fun createLayoutManager(): RecyclerView.LayoutManager
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = itemView.findViewById(android.R.id.text1)
}

private class IntDiffer : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem
}
