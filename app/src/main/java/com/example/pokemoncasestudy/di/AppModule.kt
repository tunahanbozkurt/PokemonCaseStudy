package com.example.pokemoncasestudy.di

import android.content.Context
import com.example.pokemoncasestudy.data.remote.network.PokemonAPI
import com.example.pokemoncasestudy.data.remote.repository.PokemonDetailRepositoryImpl
import com.example.pokemoncasestudy.data.remote.repository.PokemonListRepositoryImpl
import com.example.pokemoncasestudy.domain.repository.PokemonDetailRepository
import com.example.pokemoncasestudy.domain.repository.PokemonListRepository
import com.example.pokemoncasestudy.util.BASE_URL
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokemonAPI(): PokemonAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonAPI::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonListRepository(api: PokemonAPI): PokemonListRepository {
        return PokemonListRepositoryImpl(
            api = api
        )
    }

    @Provides
    @Singleton
    fun providePokemonDetailRepository(api: PokemonAPI): PokemonDetailRepository {
        return PokemonDetailRepositoryImpl(
            api = api
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }
}