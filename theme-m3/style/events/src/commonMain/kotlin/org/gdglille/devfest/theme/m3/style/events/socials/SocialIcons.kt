package org.gdglille.devfest.theme.m3.style.events.socials

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.semantic_github
import com.paligot.confily.resources.semantic_linkedin
import com.paligot.confily.resources.semantic_mastodon
import com.paligot.confily.resources.semantic_twitter
import com.paligot.confily.resources.semantic_website
import com.paligot.confily.style.theme.buttons.IconButton
import org.gdglille.devfest.theme.m3.style.events.Res
import org.gdglille.devfest.theme.m3.style.events.ic_github
import org.gdglille.devfest.theme.m3.style.events.ic_linkedin
import org.gdglille.devfest.theme.m3.style.events.ic_mastodon
import org.gdglille.devfest.theme.m3.style.events.ic_x
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

object SocialIcons {
    @Composable
    fun Twitter(
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
}
