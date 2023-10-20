package com.sumin.readcontacts

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permission = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if (permission) {
            requestContacts()
        } else {
            Log.d("MainActivity", "Не выданы права, чтобы читать контакты!")
        }
    }

    private fun requestContacts() {
        thread {
            val cursor =
                contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )

            while (cursor?.moveToNext() == true) {
                with(cursor) {
                    val id = getInt(getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val name =
                        getString(getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))

                    val contact = Contact(id, name)

                    Log.d("MainActivity", contact.toString())
                }
            }
            cursor?.close()
        }
    }
}