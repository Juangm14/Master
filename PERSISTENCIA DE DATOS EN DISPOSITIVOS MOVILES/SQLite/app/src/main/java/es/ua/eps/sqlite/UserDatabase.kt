package es.ua.eps.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import es.ua.eps.sqlite.data.User

class UserDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "user_database.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "Usuarios"
        private const val COLUMN_ID = "ID"
        private const val COLUMN_NOMBRE_USUARIO = "nombre_usuario"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_NOMBRE_COMPLETO = "nombre_completo"
        private const val COLUMN_EMAIL = "email"
    }

    override fun getDatabaseName(): String {
        return DATABASE_NAME
    }

    override fun onCreate(db: SQLiteDatabase?) {
        if (!isTableExist(db, TABLE_USERS)) {
            val createTableQuery = """
                CREATE TABLE $TABLE_USERS (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_NOMBRE_USUARIO TEXT NOT NULL,
                    $COLUMN_PASSWORD TEXT NOT NULL,
                    $COLUMN_NOMBRE_COMPLETO TEXT NOT NULL,
                    $COLUMN_EMAIL TEXT NOT NULL
                );
            """
            db?.execSQL(createTableQuery)
        }
    }

    private fun isTableExist(db: SQLiteDatabase?, tableName: String): Boolean {
        val cursor: Cursor = db?.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = ?", arrayOf(tableName)) ?: return false
        val isExist = cursor.count > 0
        cursor.close()
        return isExist
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }

    fun createUser(userName: String, password: String, nombreComleto: String, email: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NOMBRE_USUARIO, userName)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_NOMBRE_COMPLETO, nombreComleto)
            put(COLUMN_EMAIL, email)
        }
        val result = db.insert(TABLE_USERS, null, contentValues)
        db.close()
        return result
    }

    fun updateUser(id: Int, nombreUsuario: String, password: String, nombreCompleto: String, email: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NOMBRE_USUARIO, nombreUsuario)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_NOMBRE_COMPLETO, nombreCompleto)
            put(COLUMN_EMAIL, email)
        }
        val result = db.update(TABLE_USERS, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun deleteUser(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun getAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USERS", null)

        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    username = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE_USUARIO)),
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)),
                    nombreCompleto = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE_COMPLETO)),
                    email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
                )
                userList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userList
    }

    fun getUserById(id: Int): User? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            // Comprobar que el índice de las columnas no sea -1
            val columnIdIndex = cursor.getColumnIndex(COLUMN_ID)
            val columnNombreUsuarioIndex = cursor.getColumnIndex(COLUMN_NOMBRE_USUARIO)
            val columnPasswordIndex = cursor.getColumnIndex(COLUMN_PASSWORD)
            val columnNombreCompletoIndex = cursor.getColumnIndex(COLUMN_NOMBRE_COMPLETO)
            val columnEmailIndex = cursor.getColumnIndex(COLUMN_EMAIL)

            // Solo obtener los valores si las columnas existen
            if (columnIdIndex != -1 && columnNombreUsuarioIndex != -1 && columnPasswordIndex != -1 &&
                columnNombreCompletoIndex != -1 && columnEmailIndex != -1) {

                user = User(
                    id = cursor.getInt(columnIdIndex),
                    username = cursor.getString(columnNombreUsuarioIndex),
                    password = cursor.getString(columnPasswordIndex),
                    nombreCompleto = cursor.getString(columnNombreCompletoIndex),
                    email = cursor.getString(columnEmailIndex)
                )
            } else {
                // Manejar el caso si alguna columna no se encuentra
                println("Error: Una o más columnas no existen en la tabla.")
            }
        }

        cursor.close()
        db.close()
        return user
    }

    fun login(username: String, password: String): Int {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_NOMBRE_USUARIO = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null,
            null,
            null
        )

        var result = -1

        if (cursor.moveToFirst()) {
            val idColumnIndex = cursor.getColumnIndex(COLUMN_ID)
            if (idColumnIndex >= 0) {
                result = cursor.getInt(idColumnIndex)
            }
        }

        cursor.close()
        db.close()

        return result
    }
}
