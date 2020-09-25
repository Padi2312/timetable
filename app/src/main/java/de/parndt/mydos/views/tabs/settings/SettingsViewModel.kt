package de.parndt.mydos.views.tabs.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.parndt.mydos.database.models.settings.SettingsEntity
import de.parndt.mydos.repository.SettingsRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val useCase: SettingsUseCase) : ViewModel() {


    private var settingsLiveData: MutableLiveData<SettingsEntity> = MutableLiveData()

    fun getSettingsLiveData() = settingsLiveData

    fun getSettinForKey(settingsKey: SettingsRepository.Settings) {
        GlobalScope.launch {
            settingsLiveData.postValue(useCase.getSettingForKey(settingsKey))
        }
    }

    fun createSettingWithKey(settingsKey: SettingsRepository.Settings, value: Boolean) {
        viewModelScope.launch {
            useCase.updateSettingWithKey(settingsKey, value)
        }
    }

    fun updateSettingWithKey(settingsKey: SettingsRepository.Settings, value: Boolean) {
        viewModelScope.launch {
            useCase.updateSettingWithKey(settingsKey, value)
        }
    }
}