package com.kiryltkach.staggeredgridlayoutmanagerbug

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Sample activity that has viewpager with three fragments. Each fragment has different layout manager
 * attached to its [RecyclerView].
 */
class MainActivity : AppCompatActivity() {

    class LinearNumbersFragment : NumbersFragment() {
        override fun createLayoutManager() = LinearLayoutManager(requireContext())
    }
    class GridNumbersFragment : NumbersFragment() {
        override fun createLayoutManager() = GridLayoutManager(requireContext(), 3)
    }
    class StaggeredGridNumbersFragment : NumbersFragment() {
        override fun createLayoutManager() = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    }

    private class Adapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            when(position) {
                0 -> return LinearNumbersFragment()
                1 -> return GridNumbersFragment()
                2 -> return StaggeredGridNumbersFragment()
            }
            throw RuntimeException()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pager = findViewById<ViewPager2>(R.id.view_pager)
        val adapter = Adapter(this)
        pager.adapter = adapter
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        TabLayoutMediator(tabLayout, pager) {tab, position ->
            tab.text = when(position) {
                0 -> "LinearLayoutManager"
                1 -> "GridLayoutManager"
                2 -> "StaggeredGridLayoutManager"
                else -> null
            }
        }.attach()
    }
}
