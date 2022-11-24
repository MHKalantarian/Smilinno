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

package com.mhksoft.smilinno.data.remote

import com.mhksoft.smilinno.data.model.Blog
import com.mhksoft.smilinno.data.repository.BlogSortType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BlogService {
    @GET("blogs")
    suspend fun getBlogs(@Query("sortType") sortType: BlogSortType): Response<List<Blog>>

    @GET("blogs")
    suspend fun getBlogById(@Query("id") idn: Long): Response<Blog>
}
