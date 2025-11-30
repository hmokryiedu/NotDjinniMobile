package not.djinni.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [NetworkModule::class, DatastoreModule::class])
@ComponentScan("not.djinni")
class AppModule