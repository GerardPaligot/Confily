package com.paligot.confily.style.events.socials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.semantic_bluesky
import com.paligot.confily.resources.semantic_email
import com.paligot.confily.resources.semantic_facebook
import com.paligot.confily.resources.semantic_github
import com.paligot.confily.resources.semantic_instagram
import com.paligot.confily.resources.semantic_linkedin
import com.paligot.confily.resources.semantic_mastodon
import com.paligot.confily.resources.semantic_twitter
import com.paligot.confily.resources.semantic_website
import com.paligot.confily.resources.semantic_youtube
import com.paligot.confily.style.events.Res
import com.paligot.confily.style.events.ic_bluesky
import com.paligot.confily.style.events.ic_facebook
import com.paligot.confily.style.events.ic_github
import com.paligot.confily.style.events.ic_instagram
import com.paligot.confily.style.events.ic_linkedin
import com.paligot.confily.style.events.ic_mastodon
import com.paligot.confily.style.events.ic_x
import com.paligot.confily.style.events.ic_youtube
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.buttons.IconButton
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

object SocialIcons {
    @Composable
    fun X(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            imageVector = vectorResource(Res.drawable.ic_x),
            contentDescription = stringResource(Resource.string.semantic_twitter, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun Mastodon(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            imageVector = vectorResource(Res.drawable.ic_mastodon),
            contentDescription = stringResource(Resource.string.semantic_mastodon, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun GitHub(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            imageVector = vectorResource(Res.drawable.ic_github),
            contentDescription = stringResource(Resource.string.semantic_github, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun LinkedIn(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            imageVector = vectorResource(Res.drawable.ic_linkedin),
            contentDescription = stringResource(Resource.string.semantic_linkedin, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun Bluesky(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            imageVector = vectorResource(Res.drawable.ic_bluesky),
            contentDescription = stringResource(Resource.string.semantic_bluesky, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun Facebook(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            imageVector = vectorResource(Res.drawable.ic_facebook),
            contentDescription = stringResource(Resource.string.semantic_facebook, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun Instagram(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            imageVector = vectorResource(Res.drawable.ic_instagram),
            contentDescription = stringResource(Resource.string.semantic_instagram, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun YouTube(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            imageVector = vectorResource(Res.drawable.ic_youtube),
            contentDescription = stringResource(Resource.string.semantic_youtube, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun Website(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            imageVector = Icons.Outlined.Language,
            contentDescription = stringResource(Resource.string.semantic_website, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun Email(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            imageVector = Icons.Outlined.Mail,
            contentDescription = stringResource(Resource.string.semantic_email, text),
            modifier = modifier,
            onClick = onClick
        )
    }
}

@Preview
@Composable
private fun SocialItemPreview() {
    ConfilyTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SocialIcons.X(text = "", onClick = {})
            SocialIcons.Mastodon(text = "", onClick = {})
            SocialIcons.GitHub(text = "", onClick = {})
            SocialIcons.LinkedIn(text = "", onClick = {})
            SocialIcons.Website(text = "", onClick = {})
        }
    }
}
