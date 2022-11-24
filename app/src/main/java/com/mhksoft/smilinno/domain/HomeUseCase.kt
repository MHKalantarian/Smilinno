/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mhksoft.smilinno.domain

import com.mhksoft.smilinno.common.Formatters
import com.mhksoft.smilinno.data.repository.BlogRepository
import com.mhksoft.smilinno.data.repository.BlogSortType
import com.mhksoft.smilinno.data.repository.SliderRepository
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val sliderRepository: SliderRepository, private val blogRepository: BlogRepository
) {
    suspend fun getSliders() = sliderRepository.getSliders()

    suspend fun getLatestBlogs() =
        blogRepository.getBlogs(BlogSortType.Latest).onEach { response ->
            response.data?.onEach { blog ->
                Formatters.formatDate(blog.date)
            }
        }

    suspend fun getPopularBlogs() =
        blogRepository.getBlogs(BlogSortType.Popular).onEach { response ->
            response.data?.onEach { blog ->
                Formatters.formatDate(blog.date)
            }
        }
}
