package com.decorator1889.instruments.LocalDb

import com.decorator1889.instruments.Models.SurgeryInstrumentModel
import com.decorator1889.instruments.MyApplication
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults

object RealmDbSurgery {
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

    fun saveResult(results: List<SurgeryInstrumentModel?>?) {
        val realm = realm
        realm.beginTransaction()
        realm.delete(SurgeryInstrumentModel::class.java)
        realm.copyToRealmOrUpdate(results)
        realm.commitTransaction()
        realm.close()
    }


    fun countryCodeAndNameAsObservable (): Observable<RealmResults<SurgeryInstrumentModel>>? {

        val realm = realm
        val results: RealmResults<SurgeryInstrumentModel> =
            realm.where(SurgeryInstrumentModel::class.java).findAll()
        return Observable.just(results)
    }
}