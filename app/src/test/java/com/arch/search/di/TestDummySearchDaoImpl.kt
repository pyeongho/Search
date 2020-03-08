package com.arch.search.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arch.search.data.repo.database.SearchDao
import com.arch.search.entity.SearchEntity

class TestDummySearchDaoImpl : SearchDao {
    override fun insert(userEntity: SearchEntity) {

    }

    override fun insert(userEntity01: SearchEntity, userEntity02: SearchEntity) {

    }

    override fun insert(vararg userEntity: SearchEntity) {

    }

    override fun insert(userEntity: List<SearchEntity>) {

    }

    override fun update(userEntity: SearchEntity) {

    }

    override fun update(userEntity01: SearchEntity, userEntity02: SearchEntity) {

    }

    override fun update(vararg userEntity: SearchEntity) {

    }

    override fun update(userEntity: List<SearchEntity>) {

    }

    override fun delete(userEntity: SearchEntity) {

    }

    override fun delete(userEntity01: SearchEntity, userEntity02: SearchEntity) {

    }

    override fun delete(vararg userEntity: SearchEntity) {

    }

    override fun delete(userEntity: List<SearchEntity>) {

    }

    override fun getAll(): List<SearchEntity> {
        return ArrayList()
    }

    override fun getWithId(node_id: String): SearchEntity {
        return SearchEntity()
    }

    override fun getAllObservable(): LiveData<List<SearchEntity>> {
        return MutableLiveData<List<SearchEntity>>()
    }

    override fun getWithNameObservable(node_id: String): LiveData<SearchEntity> {
        return MutableLiveData<SearchEntity>()
    }
}
