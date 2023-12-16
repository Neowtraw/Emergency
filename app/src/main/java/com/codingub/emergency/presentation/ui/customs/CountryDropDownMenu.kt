package com.codingub.emergency.presentation.ui.customs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.codingub.emergency.R
import com.codingub.emergency.common.Country
import com.codingub.emergency.presentation.ui.theme.monFamily
import com.codingub.emergency.presentation.ui.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropDownMenu(
    code: Country,
    modifier: Modifier = Modifier,
    isLabelVisible: Boolean = true,
    value: String = code.code,
    onCountryPicked: (Country) -> Unit
) {
    var isExpended by rememberSaveable {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(expanded = isExpended,
        onExpandedChange = {
            isExpended = it
        }) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpended)
            },
            textStyle = TextStyle(fontFamily = monFamily),
            leadingIcon = {
                Image(
                    painter = painterResource(id = code.flag),
                    contentDescription = "Country Flag"
                )
            },
            label = {
                if(isLabelVisible) {
                    Text(
                        stringResource(id = R.string.country_code),
                        fontFamily = monFamily,
                        color = colorResource(id = R.color.contrast)
                    )
                }
            },
            modifier = modifier.menuAnchor(),
            shape = RoundedCornerShape(Constants.MAIN_CORNER.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.navbar_selected),
                unfocusedBorderColor = colorResource(id = R.color.contrast),
                textColor = colorResource(id = R.color.main_text)
            )
        )

        ExposedDropdownMenu(expanded = isExpended,
            modifier = modifier
                .background(color = colorResource(id = R.color.background)),
            onDismissRequest = {
                isExpended = false
            }) {
            Country.values().forEach { country ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = country.flag),
                                contentDescription = "Country Flag"
                            )
                            Text(text = country.name, fontFamily = monFamily)
                        }

                    },
                    onClick = {
                        onCountryPicked(country)
                        isExpended = false
                    }
                )
            }
        }
    }
}