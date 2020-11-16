package com.teamig.imgraphy.tool;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class GlideAppModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .setBitmapPoolScreens(3)
                .build();

        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 1024 * 1024 * 200)); // 200 MB
    }
}