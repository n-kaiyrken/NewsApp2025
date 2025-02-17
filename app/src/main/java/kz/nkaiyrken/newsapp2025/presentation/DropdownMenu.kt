package kz.nkaiyrken.newsapp2025.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    items: List<String>,
    modifier: Modifier = Modifier,
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Text(
            text = selectedItem,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .menuAnchor()
                .clickable { expanded = true }
                .padding(8.dp)
        )
        ExposedDropdownMenu(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .background(MaterialTheme.colorScheme.surface),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { language ->
                DropdownMenuItem(
                    modifier = Modifier.widthIn(max = 46.dp),
                    text = {
                        Text(text = language)
                    },
                    enabled = language != selectedItem,
                    onClick = {
                        onItemSelected(language)
                        expanded = false
                    },
                )
            }
        }
    }
}