package com.test.listgituser

import android.app.Application
import com.test.listgituser.data.network.ApiClient
import com.test.listgituser.data.network.NetworkConnectionInterceptor
import com.test.listgituser.data.repositories.SearchRepository
import com.test.listgituser.ui.search.SearchViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MVVMApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiClient(instance()) }
        bind() from singleton { SearchRepository(instance()) }
        bind() from provider { SearchViewModelFactory(instance()) }

    }

}