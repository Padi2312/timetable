package de.parndt.mydos.general.tabs.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.parndt.mydos.database.models.settings.SettingsEntity
import de.parndt.mydos.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val useCase: SettingsUseCase) : ViewModel() {


    private var settingsLiveData: MutableLiveData<SettingsEntity> = MutableLiveData()

    fun getSettingsLiveData() = settingsLiveData

    fun getSettingForKey(
        settingsKey: SettingsRepository.Settings,
        callback: (SettingsEntity) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            callback(useCase.getSettingForKey(settingsKey))
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