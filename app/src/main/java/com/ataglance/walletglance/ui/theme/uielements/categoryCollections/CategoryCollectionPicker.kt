package com.ataglance.walletglance.ui.theme.uielements.categoryCollections

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithIds
import com.ataglance.walletglance.ui.theme.uielements.containers.PreviewContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCollectionPicker(
    collectionList: List<CategoryCollectionWithIds>,
    selectedCollection: CategoryCollectionWithIds,
    onCollectionSelect: (CategoryCollectionWithIds) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedCollection.name,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            collectionList.forEach { collection ->
                DropdownMenuItem(
                    text = {
                        Text(text = collection.name)
                    },
                    onClick = {
                        expanded = false
                        onCollectionSelect(collection)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val collectionList = listOf(
        CategoryCollectionWithIds(
            id = 1,
            orderNum = 1,
            type = CategoryCollectionType.Expense,
            name = "Collection 1",
            categoriesIds = listOf(1, 2, 3)
        ),
        CategoryCollectionWithIds(
            id = 2,
            orderNum = 2,
            type = CategoryCollectionType.Expense,
            name = "Collection 2",
            categoriesIds = listOf(1, 2, 3)
        ),
        CategoryCollectionWithIds(
            id = 3,
            orderNum = 3,
            type = CategoryCollectionType.Expense,
            name = "Collection 3",
            categoriesIds = listOf(1, 2, 3)
        ),
    )

    PreviewContainer {
        CategoryCollectionPicker(
            collectionList = collectionList,
            selectedCollection = collectionList[0],
            onCollectionSelect = {}
        )
    }
}