package com.kigya.upcon.di

import android.content.Context
import androidx.room.Room
import com.kigya.upcon.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArticleDatabaseModule {

    @Singleton
    @Provides
    fun provideArticleDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ArticleDatabase::class.java,
        "article_db.db"
    ).build()

    @Singleton
    @Provides
    fun provideArticleDao(
        db: ArticleDatabase
    ) = db.getArticleDao()
}
