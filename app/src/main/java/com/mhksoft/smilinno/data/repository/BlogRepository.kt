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

package com.mhksoft.smilinno.data.repository

import com.mhksoft.smilinno.data.remote.BlogService
import javax.inject.Inject

class BlogRepository @Inject constructor(
    private val api: BlogService
) : BaseRepository() {
     suspend fun getBlogs(sortType: BlogSortType) = getResult { api.getBlogs(sortType) }

    suspend fun getBlogById(id: Long) = getResult { api.getBlogById(id) }
}

enum class BlogSortType {
    Latest, Popular
}
