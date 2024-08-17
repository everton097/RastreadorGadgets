package com.example.gadgetmultitable.ui.views

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.gadgetmultitable.R

data class AppUiState(
    @StringRes val title: Int = R.string.gadget_list,
    @DrawableRes val fabIcon : Int = R.drawable.baseline_add_24,
    @StringRes val iconContentDescription: Int = R.string.insert_new_gadget,
    val optionsEnable: Boolean = false,
    val floatingActionButtonEnable: Boolean = true,
)
