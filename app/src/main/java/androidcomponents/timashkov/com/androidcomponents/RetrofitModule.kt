

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(authHolder: AuthHolder): Retrofit =
            RetrofitCreator.createRetrofit(authHolder)

    @Provides
    fun provideApiRetrofit(retrofit: Retrofit): ApiRetrofit =
            retrofit.create(ApiRetrofit::class.java)

}
