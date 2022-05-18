package com.cabbooking.roomDB.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cabbooking.models.CommonResponse
import com.cabbooking.roomDB.MyRoomDatabase
import com.cabbooking.roomDB.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private var userDao: UserDao
    private var db: MyRoomDatabase = MyRoomDatabase.getDatabase(application)

    init {
        userDao = db.userDao()
    }

    fun insertUserData(data: CommonResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertUserData(data)
        }
    }

    fun deleteUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.deleteUserData()
        }
    }

    fun getUserData(): CommonResponse? {
        return userDao.getUserData
    }

    fun updateUserData(data: CommonResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.updateUserData(data)
        }
    }

//    fun updateFriendsList(friendsId: ArrayList<Int>) {
//        val userData = getUserData()
//        if(userData != null){
//            userData.friends = friendsId
//            updateUserData(userData)
//        }
//    }

//    fun updateBlockedUsersList(blockedUsersId: ArrayList<Int>) {
//        val userData = getUserData()
//        if(userData != null){
//            userData.blockedUser = blockedUsersId
//            updateUserData(userData)
//        }
//    }
}