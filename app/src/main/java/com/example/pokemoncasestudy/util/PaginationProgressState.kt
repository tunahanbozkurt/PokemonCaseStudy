package com.example.pokemoncasestudy.util

import androidx.annotation.StringRes

data class PaginationProgressState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
