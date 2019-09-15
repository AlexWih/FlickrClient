package baz.bar.foo.flickrclient

import android.app.Application
import baz.bar.foo.flickrclient.overview.ImageOverviewViewModelImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

class FlickrClientApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val networkingModule = module {

            single {
                ObjectMapper().registerModule(KotlinModule())
            }

            single {
                val okHttpClient = OkHttpClient.Builder().build()

                Retrofit.Builder()
                    .baseUrl("https://api.flickr.com/services/rest/")
                    .client(okHttpClient)
                    .addConverterFactory(JacksonConverterFactory.create(get()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()
            }
            single {
                ImageOverviewViewModelImpl(

                )
            }
        }

        // start Koin!
        startKoin {
            // Android context
            androidContext(this@FlickrClientApplication)
            // modules
            modules(networkingModule)
        }
    }
}
