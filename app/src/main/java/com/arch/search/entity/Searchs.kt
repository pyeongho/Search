package com.arch.search.entity

data class Searchs(
    val itemCount: Int,
    val control: Map<String, String>,
    val items: ArrayList<Search>
)
