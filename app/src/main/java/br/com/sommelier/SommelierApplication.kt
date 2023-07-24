package br.com.sommelier

import android.app.Application
import br.com.sommelier.di.SommelierModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SommelierApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        inject()
    }

    private fun inject() {
        startKoin {
            androidLogger()
            androidContext(this@SommelierApplication)
            modules(SommelierModule.getModules())
        }
    }
}
