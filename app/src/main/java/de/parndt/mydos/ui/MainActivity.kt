package de.parndt.mydos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import de.parndt.mydos.R
import de.parndt.mydos.repository.SettingsRepository
import de.parndt.mydos.views.TabsFragment
import de.parndt.mydos.views.tabs.settings.SettingsUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var settingsUseCase: SettingsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)
        init()
    }

    fun init() {
        navigateToFragment(TabsFragment())

        if (!settingsUseCase.settingsInitialized()) {
            initSettings()
        }
    }

    fun navigateToFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.drawerLayout, fragment)
        fragmentTransaction.commit()
    }

    fun navigateToTab(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navigation_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun initSettings() {
        GlobalScope.launch {
            settingsUseCase.createSettingWithKey(
                SettingsRepository.Settings.FILTER_ONLY_CHECKED,
                false
            )
        }
    }
}