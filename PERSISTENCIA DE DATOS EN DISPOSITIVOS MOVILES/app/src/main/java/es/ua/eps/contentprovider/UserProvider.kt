package es.ua.eps.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns
import android.util.Log

class UserProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "es.ua.eps.contentprovider"
        const val PATH_USERS = "users"
        val CONTENT_URI: Uri = Uri.parse("content://es.ua.eps.contentprovider/users")

        const val USERS = 1
        const val USER_ID = 2
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH_USERS, USERS)
            addURI(AUTHORITY, "$PATH_USERS/#", USER_ID)
        }
    }

    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(): Boolean {
        Log.d("UserProvider", "ContentProvider onCreate")
        dbHelper = UserDatabaseHelper(context!!)

        // Crear la base de datos y borrar si existe
        val db = dbHelper.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS ${UserContract.TABLE_NAME}")
        //Crea e incluye un usuario.
        dbHelper.onCreate(db)

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d("UserProvider", "Se ha llamado a query con URI: $uri y selección: $selection")
        val db = dbHelper.readableDatabase
        return when (uriMatcher.match(uri)) {
            USERS -> db.query(
                UserContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )

            USER_ID -> {
                val id = uri.lastPathSegment ?: return null
                db.query(
                    UserContract.TABLE_NAME,
                    projection,
                    "${BaseColumns._ID}=?",
                    arrayOf(id),
                    null,
                    null,
                    sortOrder
                )
            }

            else -> throw IllegalArgumentException("URI no soportada")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val id = db.insert(UserContract.TABLE_NAME, null, values)
        return if (id != -1L) Uri.withAppendedPath(CONTENT_URI, id.toString()) else null
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val db = dbHelper.writableDatabase
        return when (uriMatcher.match(uri)) {
            USERS -> db.update(UserContract.TABLE_NAME, values, selection, selectionArgs)
            USER_ID -> {
                val id = uri.lastPathSegment ?: return 0
                db.update(UserContract.TABLE_NAME, values, "${BaseColumns._ID}=?", arrayOf(id))
            }

            else -> throw IllegalArgumentException("URI no soportada")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        return when (uriMatcher.match(uri)) {
            USERS -> db.delete(UserContract.TABLE_NAME, selection, selectionArgs)
            USER_ID -> {
                val id = uri.lastPathSegment ?: return 0
                db.delete(UserContract.TABLE_NAME, "${BaseColumns._ID}=?", arrayOf(id))
            }

            else -> throw IllegalArgumentException("URI no soportada")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            USERS -> "vnd.android.cursor.dir/$AUTHORITY.$PATH_USERS"
            USER_ID -> "vnd.android.cursor.item/$AUTHORITY.$PATH_USERS"
            else -> throw IllegalArgumentException("URI no soportada")
        }
    }
}

object UserContract : BaseColumns {
    const val TABLE_NAME = "users"
    const val COLUMN_USERNAME = "username"
    const val COLUMN_PASSWORD = "password"
}

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "users.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        Log.d("UserProvider", "ContentProvider onCreate")
             db.execSQL(
                 "CREATE TABLE ${UserContract.TABLE_NAME} (" +
                         "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "${UserContract.COLUMN_USERNAME} TEXT NOT NULL, " +
                         "${UserContract.COLUMN_PASSWORD} TEXT NOT NULL)"
             )

             val insertData = """INSERT INTO ${UserContract.TABLE_NAME} (${UserContract.COLUMN_USERNAME}, ${UserContract.COLUMN_PASSWORD}) VALUES ('a', 'a')""".trimIndent()
         db.execSQL(insertData)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}
