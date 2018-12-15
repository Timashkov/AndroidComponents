
@Module
class AndroidApplicationModule(context: Context) {

    private var mApplicationContext: Context = context.applicationContext

    private var mAuthHolder = AuthHolder(context)

    private var mSessionDataStorage = SessionDataStorage()

    private var mNotificationsDatabase: NotificationsDatabase = Room.databaseBuilder(context.applicationContext, NotificationsDatabase::class.java, "UserNotifications").build()

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = mApplicationContext


    @Provides
    @Singleton
    fun provideAuthHolder(): AuthHolder = mAuthHolder

    @Provides
    @Singleton
    fun provideSessionDataStorage(): SessionDataStorage = mSessionDataStorage

    @Provides
    @Singleton
    fun provideUserNotificationsDB(): NotificationsDatabase = mNotificationsDatabase
}
