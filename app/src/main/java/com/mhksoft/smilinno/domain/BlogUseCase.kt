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
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class BlogUseCase @Inject constructor(
    private val blogRepository: BlogRepository
) {
    suspend fun getBlogDetail(id: Long) = blogRepository.getBlogById(id).onEach { response ->
        Formatters.formatDate(response.data?.date)
        response.data?.comments?.onEach { comment ->
            Formatters.formatDate(comment?.createdOn)
        }
    }
}
