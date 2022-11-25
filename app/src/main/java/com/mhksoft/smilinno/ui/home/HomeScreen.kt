package com.mhksoft.smilinno.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.mhksoft.smilinno.R
import com.mhksoft.smilinno.common.Formatters.Companion.formatDate
import com.mhksoft.smilinno.data.model.Blog
import com.mhksoft.smilinno.data.model.Slider
import com.mhksoft.smilinno.ui.component.ResourceContainer
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToBlogDetail: (id: Long) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ResourceContainer(
                resource = uiState.sliders,
                resourceMethod = { viewModel.getSliders() }) {
                it?.let { sliders ->
                    SliderView(coroutineScope, sliders)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            ResourceContainer(
                resource = uiState.popularBlogs,
                resourceMethod = { viewModel.getPopularBlogs() }) {
                it?.let { blogs ->
                    Text(
                        text = stringResource(id = R.string.home_screen_popular_blogs_title),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    BlogsRow(blogs, navigateToBlogDetail)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            ResourceContainer(
                resource = uiState.latestBlogs,
                resourceMethod = { viewModel.getLatestBlogs() }) {
                it?.let { blogs ->
                    Text(
                        text = stringResource(id = R.string.home_screen_latest_blogs_title),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    BlogsRow(blogs, navigateToBlogDetail)
                }
            }
        }
    }
}

@Composable
fun SliderView(
    coroutineScope: CoroutineScope,
    sliders: List<Slider>
) {
    var pagerKey by remember { mutableStateOf(false) } // pagerKey needs to be changed everytime to render scroll to properly
    val pagerState = rememberPagerState()
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState() // isDragged checks if the user is interacting with the component to stop the auto scroll
    if (!isDragged) {
        LaunchedEffect(key1 = pagerKey) {
            launch {
                delay(3000)
                with(pagerState) {
                    val target = if (currentPage < pageCount - 1) currentPage + 1 else 0
                    animateScrollToPage(page = target) //Broken
                    pagerKey = !pagerKey
                }
            }
        }
    }

    Card(shape = RoundedCornerShape(16.dp)) {
        Box(modifier = Modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { change, dragAmount ->
                change.consume()
                when {
                    dragAmount < 0 -> {
                        coroutineScope.launch { /* right */
                            if (pagerState.currentPage == sliders.lastIndex) {
                                pagerState.animateScrollToPage(0)
                            } else {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }
                    dragAmount > 0 -> { /* left */
                        coroutineScope.launch {
                            if (pagerState.currentPage == 0) {
                                pagerState.animateScrollToPage(sliders.lastIndex)
                            } else {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    }
                }
            }
        }) {
            HorizontalPager(
                sliders.size,
                state = pagerState
            ) { page ->
                CoilImage(
                    imageModel = { sliders[page].path },
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
                Text(
                    text = sliders[page].title ?: stringResource(id = R.string.blog_no_title_error),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = .75f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }
        }
    }
    HorizontalPagerIndicator(
        pagerState = pagerState,
        activeColor = MaterialTheme.colorScheme.primary,
        inactiveColor = MaterialTheme.colorScheme.primary.copy(.5f),
        modifier = Modifier
            .padding(top = 8.dp),
    )
}

@Composable
fun BlogsRow(
    blogs: List<Blog>,
    navigateToBlogDetail: (id: Long) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyRow(state = lazyListState) {
        items(blogs) {
            BlogItem(item = it, navigateToBlogDetail)
        }
    }
}

@Composable
fun BlogItem(
    item: Blog,
    navigateToBlogDetail: (id: Long) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(164.dp),
        shape = RoundedCornerShape(4.dp),
        onClick = { item.id?.let { navigateToBlogDetail(it) } }
    ) {
        ConstraintLayout {
            val (image, title, date) = createRefs()

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
                    .aspectRatio(3 / 2f)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            Text(
                text = item.title ?: stringResource(id = R.string.blog_no_title_error),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(image.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 4.dp)
                    end.linkTo(parent.end, margin = 4.dp)
                    width = Dimension.fillToConstraints
                })
            Text(
                text = item.date?.formatDate() ?: stringResource(id = R.string.blog_no_date_error),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.constrainAs(date) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    end.linkTo(image.end, margin = 4.dp)
                    bottom.linkTo(parent.bottom, 4.dp)
                })
        }
    }
}