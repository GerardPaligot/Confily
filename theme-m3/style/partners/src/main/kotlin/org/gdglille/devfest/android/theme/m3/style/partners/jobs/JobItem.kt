package org.gdglille.devfest.android.theme.m3.style.partners.jobs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.previews.ThemedPreviews
import org.gdglille.devfest.android.theme.m3.style.tags.MediumTag
import org.gdglille.devfest.android.theme.m3.style.tags.TagDefaults
import org.gdglille.devfest.android.theme.m3.style.toDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobItem(
    title: String,
    description: String,
    requirements: Int,
    propulsedBy: String,
    salaryMin: Int?,
    salaryMax: Int?,
    salaryRecurrence: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = JobItemDefaults.containerColor,
    shape: Shape = JobItemDefaults.shape,
    titleTextStyle: TextStyle = JobItemDefaults.titleTextStyle,
    descriptionTextStyle: TextStyle = JobItemDefaults.descriptionTextStyle
) {
    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(JobItemTokens.ContainerPadding.toDp())) {
            Text(
                text = title,
                style = titleTextStyle,
                modifier = Modifier.padding(start = JobItemTokens.ContentStartPadding.toDp())
            )
            Text(
                text = description,
                style = descriptionTextStyle,
                modifier = Modifier.padding(start = JobItemTokens.ContentStartPadding.toDp())
            )
            Row(
                modifier = Modifier.padding(top = JobItemTokens.TagTopPadding.toDp())
            ) {
                if (salaryMin != null && salaryMax != null && salaryRecurrence != null) {
                    MediumTag(
                        text = stringResource(
                            id = R.string.text_job_salary,
                            salaryMin,
                            salaryMax,
                            salaryRecurrence
                        ),
                        icon = Icons.Outlined.Payments,
                        colors = TagDefaults.unStyledColors()
                    )
                }
                MediumTag(
                    text = pluralStringResource(
                        id = R.plurals.text_job_requirements,
                        count = requirements,
                        requirements
                    ),
                    icon = Icons.Outlined.Work,
                    colors = TagDefaults.unStyledColors()
                )
            }
            MediumTag(
                text = stringResource(id = R.string.text_job_propulsed, propulsedBy),
                colors = TagDefaults.gravelColors(),
                modifier = Modifier.padding(
                    top = JobItemTokens.TagTopPadding.toDp(),
                    start = JobItemTokens.ContentStartPadding.toDp()
                )
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@ThemedPreviews
@Composable
private fun JobItemPreview() {
    Conferences4HallTheme {
        JobItem(
            title = "Mobile Staff Engineer",
            description = "Google - Paris, France",
            requirements = 5,
            propulsedBy = "WeLoveDevs",
            salaryMin = 55,
            salaryMax = 75,
            salaryRecurrence = "year",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
