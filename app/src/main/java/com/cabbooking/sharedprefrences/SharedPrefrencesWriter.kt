package com.cabbooking.sharedprefrences

import android.content.Context
import android.content.SharedPreferences
import com.cabbooking.R

class SharedPreferencesWriter private constructor() {
    companion object {
        private var sharePref: SharedPreferencesWriter? = null
        var mPrefs: SharedPreferences? = null
        var prefEditor: SharedPreferences.Editor? = null

        fun getInstance(context: Context): SharedPreferencesWriter {
            if (null == sharePref) {
                sharePref = SharedPreferencesWriter()
                mPrefs = context.getSharedPreferences(
                    context.getString(R.string.app_name),
                    Context.MODE_PRIVATE
                )
                prefEditor = mPrefs?.edit()
            }
            return sharePref as SharedPreferencesWriter
        }

        const val PREFERENCE_NAME: String = "app_pref"

    }
    /*  private val sharedPreferences: SharedPreferences =
          context?.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE)!!
      private val editor: SharedPreferences.Editor = sharedPreferences.edit()


      var isLoggedIn: Boolean
          get() = sharedPreferences["is_logged_in", false]!!
          set(value) = sharedPreferences.set("is_logged_in", value)
  */


    fun writeStringValue(key: String, value: String?) {
        prefEditor!!.putString(key, value)
        prefEditor!!.commit()
    }

    fun getString(key: String): String? {
        return mPrefs!!.getString(key, "")

    }

    fun writeIntValue(key: String, value: Int) {
        prefEditor!!.putInt(key, value)
        prefEditor!!.commit()

    }

    fun getInt(key: String): Int {
        return mPrefs!!.getInt(key, 0)
    }

    fun writeLongValue(key: String, value: Long) {
        prefEditor!!.putLong(key, value)
        prefEditor!!.commit()

    }

    fun getLong(key: String): Long {
        return mPrefs!!.getLong(key, 0)
    }

    fun writeBooleanInt(key: String, value: Boolean) {
        prefEditor?.putBoolean(key, value)
        prefEditor?.commit()
    }

    fun getBoolean(key: String): Boolean? {
        return mPrefs?.getBoolean(key, false)
    }

    fun clearPreferenceValues() {
        prefEditor!!.clear()
        prefEditor!!.commit()

    }
//    fun clearParticularPreferenceValues() {
//        prefEditor!!.clear()
//        prefEditor!!.commit()
//
//    }
    /* fun deletePreferences() {
         editor.clear()
         editor.apply()
     }*/


    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    /*  operator fun SharedPreferences.set(key: String, value: Any?) {
          when (value) {
              is String? -> edit({ it.putString(key, value) })
              is Int -> edit({ it.putInt(key, value) })
              is Boolean -> edit({ it.putBoolean(key, value) })
              is Float -> edit({ it.putFloat(key, value) })
              is Long -> edit({ it.putLong(key, value) })
              else -> Log.e(TAG, "Setting shared pref failed for key: $key and value: $value ")
          }
      }

      private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
          val editor = this.edit()
          operation(editor)
          editor.apply()
      }

      */
    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     *//*
    inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
*/


}


