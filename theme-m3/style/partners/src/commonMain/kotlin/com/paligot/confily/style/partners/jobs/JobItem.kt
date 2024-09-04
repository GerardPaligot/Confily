package com.paligot.confily.style.partners.jobs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_job_propulsed
import com.paligot.confily.resources.text_job_requirements_many
import com.paligot.confily.resources.text_job_requirements_one
import com.paligot.confily.resources.text_job_salary
import com.paligot.confily.style.theme.tags.MediumTag
import com.paligot.confily.style.theme.tags.TagDefaults
import com.paligot.confily.style.theme.toDp
import org.jetbrains.compose.resources.stringResource

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
                            Resource.string.text_job_salary,
                            salaryMin,
                            salaryMax,
                            salaryRecurrence
                        ),
                        icon = Icons.Outlined.Payments,
                        colors = TagDefaults.unStyledColors()
                    )
                }
                MediumTag(
                    text = if (requirements <= 1) {
                        stringResource(
                            Resource.string.text_job_requirements_one,
                            requirements
                        )
                    } else {
                        stringResource(Resource.string.text_job_requirements_many, requirements)
                    },
                    icon = Icons.Outlined.Work,
                    colors = TagDefaults.unStyledColors()
                )
            }
            MediumTag(
                text = stringResource(Resource.string.text_job_propulsed, propulsedBy),
                colors = TagDefaults.gravelColors(),
                modifier = Modifier.padding(
                    top = JobItemTokens.TagTopPadding.toDp(),
                    start = JobItemTokens.ContentStartPadding.toDp()
                )
            )
        }
    }
}
