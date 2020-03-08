package com.arch.search.data.repo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.arch.search.entity.SearchEntity

@Dao
interface SearchDao {
    //insert 구문들
    @Insert
    fun insert(searchEntity: SearchEntity)

    @Insert
    fun insert(searchEntity01: SearchEntity, searchEntity02: SearchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg userEntity: SearchEntity)

    @Insert
    fun insert(searchEntity: List<SearchEntity>)

    //update 구문들
    @Update
    fun update(searchEntity: SearchEntity)

    @Update
    fun update(searchEntity01: SearchEntity, searchEntity02: SearchEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg searchEntity: SearchEntity)

    @Update
    fun update(searchEntity: List<SearchEntity>)

    //delete 구문들
    @Delete
    fun delete(searchEntity: SearchEntity)

    @Delete
    fun delete(searchEntity01: SearchEntity, searchEntity02: SearchEntity)

    @Delete
    fun delete(vararg searchEntity: SearchEntity)

    @Delete
    fun delete(userEntity: List<SearchEntity>)


    //Simple Query
    @Query("SELECT * FROM FAVORITE_TABLE ORDER BY title ASC")
    fun getAll(): List<SearchEntity>

    @Query("SELECT * FROM FAVORITE_TABLE where slug = :slug")
    fun getWithId(slug: String): SearchEntity


    //Observable Query
    @Query("SELECT * FROM FAVORITE_TABLE ORDER BY title ASC")
    fun getAllObservable(): LiveData<List<SearchEntity>>

    @Query("SELECT * FROM FAVORITE_TABLE where slug = :slug")
    fun getWithNameObservable(slug: String): LiveData<SearchEntity>
}
