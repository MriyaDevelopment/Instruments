package com.decorator1889.instruments.LocalDb

import com.decorator1889.instruments.Models.InstrumentModel
import com.decorator1889.instruments.MyApplication
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults

object RealmDb {
    @JvmStatic
    fun init(app: MyApplication?) {
        Realm.init(app)
        val realmConfiguration = RealmConfiguration.Builder()
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
    val realm: Realm
        get() = Realm.getDefaultInstance()

    fun saveResult(results: List<InstrumentModel?>?) {
        val realm = realm
        realm.beginTransaction()
        realm.delete(InstrumentModel::class.java)
        realm.copyToRealmOrUpdate(results)
        realm.commitTransaction()
        realm.close()
    }


    fun countryCodeAndNameAsObservable (): Observable<RealmResults<InstrumentModel>>? {

        val realm = realm
        val results: RealmResults<InstrumentModel> =
            realm.where(InstrumentModel::class.java).findAll()
        return Observable.just(results)
    }
}