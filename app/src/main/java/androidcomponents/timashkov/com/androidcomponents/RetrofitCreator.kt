
internal object RetrofitCreator {

    private val TAG = RetrofitCreator::class.java.simpleName
    private val mCredentials = "my-trusted-client:secret"
    private val mCookieGetHeaderKey = "Set-Cookie"
    private val mCookieSetHeaderKey = "Cookie"

    val API_URL = BuildConfig.BaseURL

    fun createRetrofit(authHolder: AuthHolder): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_URL)

        val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        okHttpBuilder.addInterceptor(Interceptor { chain ->
            var request: Request = chain.request()

            val builder: Request.Builder = request.newBuilder()
            if (authHolder.isAuthenticated() && !request.url().encodedPath().endsWith("token")) {
                builder.addHeader("Authorization", "Bearer ${authHolder.getAuth()!!.getToken()}")
            } else {
                builder.addHeader("Authorization", "Basic ${Base64.encodeToString(mCredentials.toByteArray(), Base64.NO_WRAP)}")
            }
            if (authHolder.getCookie() != null) {
                builder.addHeader(mCookieSetHeaderKey, authHolder.getCookie()!!)
            }

            request = builder.build()
            val response = chain.proceed(request)
            authHolder.setSessionCookie(response.header(mCookieGetHeaderKey))
            response
        })
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder.addInterceptor(loggingInterceptor)

        retrofitBuilder.client(okHttpBuilder.readTimeout(10, TimeUnit.SECONDS).build())
        return retrofitBuilder.build()
    }
}