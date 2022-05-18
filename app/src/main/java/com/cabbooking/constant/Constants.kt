package com.cabbooking.constant

object Constants {

    const val SPLASH_TIME_OUT: Long = 2000
    const val SWIPE_TIME: Long = 2000
    const val ONE = 1
    const val TEN = 10
    const val TOKEN_AUTH = "Bearer"

    //Confirm,Pending,Not Confirm,Cancel,Driver,Cab,All
    const val CONFIRMED = "Confirm"
    const val PENDING = "Pending"
    const val NOT_CONFIRMED = "Not Confirm"
    const val CANCEL = "Cancel"
    const val ALL = "All"
    const val BOOKING_FRAGMENT = "Booking Fragment"
    const val FILTER_FRAGMENT = "Filter Fragment"
    const val DRIVER_FRAGMENT = "Driver Fragment"
    const val ROUTE_FRAGMENT = "Route Fragment"
    const val CAB_FRAGMENT = "Cab Fragment"
    const val CREATE = "CREATE"
    const val CREATE_BOOKING = "Create_Booking"
    const val CREATE_DRIVER = "Create_Driver"
    const val CREATE_CAB = "Create_Cab"
    const val PROFILE_FRAGMENT = "ProfileFragment"
    const val CAB_LIST = "Cabs list"
    const val CAB_DATA = "Cabs Data"
    const val DRIVER_LIST = "Drivers list"
    const val TYPE = "type"
    const val ONE_WAY = "One Way"
    const val ROUND_WAY = "Round Trip"
    const val RENTAL = "Rental"
    const val SHARING = "Sharing"
    const val PICK_UP = "PICK_UP"
    const val DROP = "DROP"
    const val LAST_UPCOMING_DAY = "lastOrUpcomingDate"
    const val FILTER_REQUEST_CODE = 20


    const val BACK_PRESS_TIME_INTERVAL: Long = 2000

    // Formats
    const val EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    const val NUMBER_PATTERN = "[0-9]+"

    const val PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[A-Z]).{8,255}\$"
    const val NAME_PATTERN = "^[ A-Za-z_-]+$"
    const val MOBILE_PATTERN = "0-9"
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val DEVICE_TYPE = "0"


    const val SETTING_PROFILE = 202
    const val UPDATE_NUMBER = 203
    const val VERIFYIMAGE = 204
    const val FEED_BACK = 205
    const val WEB_VIEWS = 206
    const val CHANGE_SETTING = 207
    const val PERSON_INFORMATION = 208
    const val CHANGE_LOCATION = 209

    const val MatisseLib = 1040
    const val CAMERA_CONSTANT = 1041
    const val GALLERY_CONSTANT = 1042
    const val MatisseLibImages = 1043
    const val MatisseLibVideo = 1044
    const val VIDEO_CONSTANT = 1045


    const val NOTIFICATION_FRIEND_REQUEST = 3000
    const val NOTIFICATION_CHAT = 3001
    const val NOTIFICATION_TEXT = 3002

    const val SOLID_COLOR = 4001
    const val FIRST_LOVE_LIBRARY = 4002

    const val MONTH_CONSTANT = 5001
    const val DATE_CONSTANT = 5002
    const val BOOKING_DATA = 5003

    const val RECENT_CHAT = 5004
    const val DIRECT_NOTIFICATION_CHAT = 5005

    const val NEW_MATCH = 5006
    const val FRIEND_LIST = 5007
    const val ITS_MATCH = 5008

    const val INSTAGRAM = 7000


}