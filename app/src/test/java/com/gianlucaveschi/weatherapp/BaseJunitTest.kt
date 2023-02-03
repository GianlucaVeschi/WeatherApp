package com.gianlucaveschi.weatherapp

import androidx.annotation.CallSuper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before

abstract class BaseJunitTest<T : Any> {

    protected lateinit var systemUnderTest: T

    @Before
    @CallSuper
    @ExperimentalCoroutinesApi
    open fun setUp() {
        systemUnderTest = initSelf()
    }

    @ExperimentalCoroutinesApi
    protected abstract fun initSelf(): T
}