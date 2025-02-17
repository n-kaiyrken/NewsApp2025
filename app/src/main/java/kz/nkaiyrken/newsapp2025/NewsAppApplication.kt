package kz.nkaiyrken.newsapp2025

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kz.nkaiyrken.newsapp2025.di.AppComponent
import kz.nkaiyrken.newsapp2025.di.DaggerAppComponent

class NewsAppApplication: Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

}

@Composable
fun getApplicationComponent(): AppComponent {
    return (LocalContext.current.applicationContext as NewsAppApplication).component
}