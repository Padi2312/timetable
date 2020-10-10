package de.parndt.mydos.general.tabs.settings

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

    fun getSettinForKey(filterKey: SettingsRepository.Filter) {
        GlobalScope.launch {
            settingsLiveData.postValue(useCase.getSettingForKey(filterKey))
        }
    }

    fun createSettingWithKey(filterKey: SettingsRepository.Filter, value: Boolean) {
        viewModelScope.launch {
            useCase.updateSettingWithKey(filterKey, value)
        }
    }

    fun updateSettingWithKey(filterKey: SettingsRepository.Filter, value: Boolean) {
        viewModelScope.launch {
            useCase.updateSettingWithKey(filterKey, value)
        }
    }
}