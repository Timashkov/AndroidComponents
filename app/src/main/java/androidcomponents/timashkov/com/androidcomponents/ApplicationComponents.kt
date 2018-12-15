@Singleton
@Component(modules = arrayOf(RetrofitModule::class, AndroidApplicationModule::class, UserDataModule::class, DataServicesModule::class))
interface ApplicationComponents {
}
