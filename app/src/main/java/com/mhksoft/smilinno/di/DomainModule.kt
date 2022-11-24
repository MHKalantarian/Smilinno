package com.mhksoft.smilinno.di

import com.mhksoft.smilinno.data.remote.BlogService
import com.mhksoft.smilinno.data.remote.SliderService
import com.mhksoft.smilinno.data.repository.BlogRepository
import com.mhksoft.smilinno.data.repository.SliderRepository
import com.mhksoft.smilinno.domain.BlogUseCase
import com.mhksoft.smilinno.domain.HomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    @ViewModelScoped
    fun provideSliderRepository(
        sliderService: SliderService,
    ): SliderRepository = SliderRepository(sliderService)


    @Provides
    @ViewModelScoped
    fun provideBlogRepository(
        blogService: BlogService,
    ): BlogRepository = BlogRepository(blogService)


    @Provides
    @ViewModelScoped
    fun provideHomeUseCase(
        sliderRepository: SliderRepository,
        blogRepository: BlogRepository
    ): HomeUseCase = HomeUseCase(sliderRepository, blogRepository)

    @Provides
    @ViewModelScoped
    fun provideBlogUseCase(
        blogRepository: BlogRepository
    ): BlogUseCase = BlogUseCase(blogRepository)
}
