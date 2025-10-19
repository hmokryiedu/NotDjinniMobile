package not.djinni.presentation

import android.app.Application
import not.djinni.BuildConfig
import not.djinni.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.core.context.startKoin
import timber.log.Timber

@KoinApplication(modules = [AppModule::class])
class NotDjinniApplication : Application() {

    override fun onCreate() {
        plantTimber()
        super.onCreate()
        configureKoin()
    }

    private fun plantTimber() {
        if (!BuildConfig.DEBUG) return
        Timber.plant(Timber.DebugTree())
    }

    private fun configureKoin() {
        startKoin {
            androidContext(this@NotDjinniApplication)
            androidLogger()
        }
    }
}