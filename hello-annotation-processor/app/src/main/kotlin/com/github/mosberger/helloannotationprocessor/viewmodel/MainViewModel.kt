package com.github.mosberger.helloannotationprocessor.viewmodel

import android.databinding.BaseObservable
import com.github.mosberger.helloannotationprocessor.model.MainModel

/**
 * Created by domi on 07.02.16.
 */
class MainViewModel(val mainModel: MainModel = MainModel()): BaseObservable() {
    fun getMessage() = mainModel.message
}