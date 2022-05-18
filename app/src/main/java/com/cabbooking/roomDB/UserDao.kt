package com.cabbooking.roomDB

import androidx.room.*
import com.cabbooking.models.CommonResponse

@Dao
interface UserDao {
    @get:Query("SELECT * FROM UserTable")
    val getUserData: CommonResponse?

    @Insert()
    suspend fun insertUserData(vararg users: CommonResponse)

    @Query("DELETE FROM UserTable")
    suspend fun deleteUserData()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserData(userData: CommonResponse)
}