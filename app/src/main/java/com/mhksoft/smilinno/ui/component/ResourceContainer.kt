package com.mhksoft.smilinno.ui.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mhksoft.smilinno.R
import com.mhksoft.smilinno.data.model.Resource
import com.mhksoft.smilinno.data.model.ResourceState

@Composable
inline fun <reified T> ResourceContainer(
    resource: Resource<T>,
    crossinline resourceMethod: () -> Unit,
    content: @Composable (T) -> Unit
) {
    when (resource.state) {
        ResourceState.LOADING -> CircularProgressIndicator()
        ResourceState.ERROR -> OutlinedButton(onClick = { resourceMethod() }) {
            Text(
                text = resource.errorMessage ?: stringResource(id = R.string.error),
                color = Color.Red
            )
        }
        ResourceState.SUCCESS -> {
            if (resource.data is List<*>)
                if (resource.data.isEmpty())
                    Text(
                        text = resource.errorMessage ?: stringResource(id = R.string.no_data_error),
                        color = Color.Yellow
                    )
                else
                    content(resource.data)
            else
                resource.data?.let { content(it) }
        }
    }
}
