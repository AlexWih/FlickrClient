package baz.bar.foo.flickrclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import baz.bar.foo.flickrclient.overview.ui.PhotosOverviewFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            openInitialScreen()
        }
    }

    private fun openInitialScreen() {
        val initialScreen = PhotosOverviewFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, initialScreen)
            .commit()
    }
}
