package com.oguzhancetin.goodpostureapp.di

import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class MainFragmentModule {

    @Provides
    @FragmentScoped
    fun provideAccuratePoseDetectorOptions(): AccuratePoseDetectorOptions {
        return AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()
    }

    @Provides
    @FragmentScoped
    fun providePoseDetector(options :AccuratePoseDetectorOptions): PoseDetector {
       return PoseDetection.getClient(options)
    }


}