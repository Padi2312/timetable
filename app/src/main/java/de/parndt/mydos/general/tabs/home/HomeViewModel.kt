package de.parndt.mydos.general.tabs.home

import androidx.lifecycle.ViewModel
import javax.inject.Inject


class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var homeUsecase: HomeUseCase

}