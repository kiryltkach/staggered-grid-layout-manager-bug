# staggered-grid-layout-manager-bug

Steps for reproduction:
1. Create RecyclerView with paging adapter and staggered grid layout manager
2. Submit data with some non-empty pages to the adapter
3. Scroll RecyclerView for couple of pages
4. Submit new paging data to the adapter with empty pages
5. Submit data with some non-empty pages to the adapter again

Expected: the data is set and recyclerview is scrolled to the top  
Actual: the data is set and recyclerview scroll position has some offset from the top

Reproduces on AndroidX RecyclerView version 1.2.0 and Paging version 3.0.0

[Video](https://drive.google.com/file/d/18NYm4Z4AzatIdxJGXGiA2h0B9bZkbklK/view?usp=sharing)  
[Issue tracker](https://issuetracker.google.com/issues/193051321)
