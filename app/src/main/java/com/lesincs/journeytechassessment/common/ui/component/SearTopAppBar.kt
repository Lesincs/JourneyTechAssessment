package com.lesincs.journeytechassessment.common.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import com.lesincs.journeytechassessment.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchTopAppBar(
    onSearchQueryChange: (String) -> Unit,
    onHideSearchBar: () -> Unit,
    searchQuery: String,
    placeHolderText: String,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focusRequester) { focusRequester.requestFocus() }
    SearchBar(
        modifier = modifier.focusRequester(focusRequester),
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = { onSearchQueryChange(it) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isNotEmpty()) {
                            onSearchQueryChange("")
                        } else {
                            onHideSearchBar()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.search_close_button_content_description),
                        )
                    }
                },
                placeholder = { Text(placeHolderText) },
                onSearch = {},
                expanded = false,
                onExpandedChange = {},
            )
        },
        expanded = false,
        onExpandedChange = {},
        content = {},
    )
}
