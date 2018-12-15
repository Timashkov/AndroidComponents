
object ComponentHolder {

    internal var sApplicationComponents: ApplicationComponents? = null

    fun applicationComponents(context: Context): ApplicationComponents {
        if (sApplicationComponents == null) {
            synchronized(ComponentHolder::class.java) {
                if (sApplicationComponents == null) {
                    sApplicationComponents = DaggerApplicationComponents.builder()
                            .androidApplicationModule(AndroidApplicationModule(context)).build()
                }
            }
        }
        return sApplicationComponents!!
    }
}
