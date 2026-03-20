package com.example.litecartesnative.features.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.litecartesnative.data.remote.dto.SchoolDto
import com.example.litecartesnative.features.auth.presentation.viewmodel.AuthViewModel
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun SchoolPickerDialog(
    onDismiss: () -> Unit,
    onSchoolSelected: (SchoolDto) -> Unit,
    selectedSchoolId: Int?,
    viewModel: AuthViewModel
) {
    val searchState by viewModel.schoolSearchState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Load first page on open
    LaunchedEffect(Unit) {
        viewModel.searchSchools("")
    }

    // Debounced search
    LaunchedEffect(Unit) {
        snapshotFlow { searchText }
            .debounce(300)
            .collectLatest { query ->
                viewModel.searchSchools(query)
            }
    }

    // Load more when reaching bottom
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItem >= totalItems - 3
        }.collectLatest { shouldLoadMore ->
            if (shouldLoadMore && searchState.hasNextPage && !searchState.isLoadingMore) {
                viewModel.loadMoreSchools()
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = LitecartesColor.Surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Pilih Sekolah",
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LitecartesColor.Secondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        Text(
                            text = "Cari sekolah...",
                            fontFamily = nunitosFontFamily,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = LitecartesColor.Secondary.copy(alpha = 0.5f)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LitecartesColor.Primary,
                        unfocusedBorderColor = LitecartesColor.Secondary.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (searchState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = LitecartesColor.Primary
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        items(
                            items = searchState.schools,
                            key = { it.id }
                        ) { school ->
                            val isSelected = school.id == selectedSchoolId

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSchoolSelected(school)
                                        onDismiss()
                                    }
                                    .background(
                                        if (isSelected) LitecartesColor.Primary.copy(alpha = 0.1f)
                                        else LitecartesColor.Surface
                                    )
                                    .padding(horizontal = 12.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = school.name,
                                    fontFamily = nunitosFontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) LitecartesColor.Primary else LitecartesColor.Secondary,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = LitecartesColor.Primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            HorizontalDivider(
                                color = LitecartesColor.Secondary.copy(alpha = 0.1f)
                            )
                        }

                        if (searchState.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = LitecartesColor.Primary
                                    )
                                }
                            }
                        }

                        if (searchState.schools.isEmpty() && !searchState.isLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Tidak ada sekolah ditemukan",
                                        fontFamily = nunitosFontFamily,
                                        fontSize = 14.sp,
                                        color = LitecartesColor.Secondary.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Batal",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = LitecartesColor.Secondary
                    )
                }
            }
        }
    }
}
