package com.mhksoft.smilinno.ui.blog

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.mhksoft.smilinno.R
import com.mhksoft.smilinno.common.Formatters.Companion.formatDate
import com.mhksoft.smilinno.data.model.Blog
import com.mhksoft.smilinno.ui.component.ResourceContainer
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun BlogDetailScreen(
    viewModel: BlogDetailViewModel,
    navigateUp: () -> Unit,
    blogId: Long?
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var initialApiCalled by rememberSaveable { mutableStateOf(false) } // Prevents api call on configuration change
    if (!initialApiCalled) {
        LaunchedEffect(Unit) {
            blogId?.let { viewModel.getBlogDetail(it) } ?: run { navigateUp }
            initialApiCalled = false
        }
    }
    Log.e("State", uiState.blogDetail.state.toString())

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ResourceContainer(
                resource = uiState.blogDetail,
                resourceMethod = { viewModel.getBlogDetail(blogId!!) }) {
                it?.let { blog ->
                    BlogDetail(item = blog)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        uiState.blogDetail.data?.comments?.let { comments ->
            item {
                Text(
                    text = stringResource(id = R.string.blog_screen_comments_title),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(comments) { comment ->
                comment?.let { item -> CommentItem(item) }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun BlogDetail(
    item: Blog
) {
    Card(shape = RoundedCornerShape(16.dp)) {
        CoilImage(
            imageModel = { item.path },
            failure = {
                Text(text = stringResource(R.string.error), color = Color.Red)
            },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            modifier = Modifier
                .aspectRatio(2f)
                .fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = item.title ?: stringResource(id = R.string.blog_no_title_error),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Start
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = item.date?.formatDate() ?: stringResource(id = R.string.no_date_error),
        style = MaterialTheme.typography.labelSmall,
        textAlign = TextAlign.End
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = item.body?.let {
            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
        }
            ?: stringResource(id = R.string.no_data_error),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.fillMaxWidth()
    )
    Text(
        text = item.author ?: stringResource(id = R.string.blog_no_author_error),
        style = MaterialTheme.typography.labelSmall,
        textAlign = TextAlign.End,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CommentItem(
    item: Blog.Comment
) {
    Card(shape = RoundedCornerShape(4.dp), modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoilImage(
                imageModel = { item.avatar },
                failure = {
                    Text(text = stringResource(R.string.error), color = Color.Red)
                },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .size(48.dp)
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = item.username ?: stringResource(id = R.string.user_no_name_error),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = item.createdOn?.formatDate() ?: stringResource(id = R.string.no_date_error),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        Text(
            text = item.body ?: stringResource(id = R.string.no_data_error),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp)
        )
    }
}