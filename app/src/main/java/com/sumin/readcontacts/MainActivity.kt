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
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_CONTACTS),
            READ_CONTACTS_RC
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == READ_CONTACTS_RC && grantResults.isNotEmpty()) {
            val permission = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (permission) {
                requestContacts()
            } else {
                Log.d("MainActivity", "Запрос отклонен, доступ к контактам не получены!")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    companion object {
        private const val READ_CONTACTS_RC = 100
    }
}