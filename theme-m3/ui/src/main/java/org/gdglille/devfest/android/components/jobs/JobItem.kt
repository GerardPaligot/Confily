package org.gdglille.devfest.android.components.jobs

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.tags.Tag
import org.gdglille.devfest.android.components.tags.TagDefaults
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.placeholder
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.JobUi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun JobItem(
    jobUi: JobUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onClick: (url: String) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.onSurface),
        modifier = modifier.placeholder(visible = isLoading),
        onClick = { onClick(jobUi.url) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = jobUi.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = "${jobUi.companyName} - ${jobUi.location}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            Row(
                modifier = Modifier.padding(top = 4.dp)
            ) {
                jobUi.salary?.let { salaryUi ->
                    Tag(
                        text = stringResource(
                            id = R.string.text_job_salary,
                            salaryUi.min,
                            salaryUi.max,
                            salaryUi.recurrence
                        ),
                        icon = Icons.Outlined.Payments,
                        colors = TagDefaults.unStyledColors()
                    )
                }
                Tag(
                    text = pluralStringResource(
                        id = R.plurals.text_job_requirements,
                        count = jobUi.requirements,
                        jobUi.requirements
                    ),
                    icon = Icons.Outlined.Work,
                    colors = TagDefaults.unStyledColors()
                )
            }
            Tag(
                text = stringResource(id = R.string.text_job_propulsed, jobUi.propulsed),
                colors = TagDefaults.gravelColors(),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun JobItemPreview() {
    Conferences4HallTheme {
        JobItem(
            jobUi = JobUi.fake,
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        )
    }
}
