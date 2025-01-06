package es.ua.eps.sqliteroom

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Room

@Entity(tableName = "user")
data class UserRoom(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "email")
    val email: String
)

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun loadAll(): List<UserRoom>

    @Query("SELECT * FROM user WHERE uid = :userIds")
    fun loadUserById(userIds: Int): UserRoom

    @Insert
    fun insertAll(vararg users: UserRoom)

    @Insert
    fun insert(user: UserRoom)

    @Update
    fun update(user: UserRoom)

    @Delete
    fun delete(user: UserRoom)

    @Query("DELETE FROM user WHERE uid = :userId")
    fun deleteById(userId: Int)

    @Query("SELECT uid FROM user WHERE username = :username AND  password = :password LIMIT 1")
    fun login(username: String, password: String): Int?
}


@Database(entities = [UserRoom::class], version = 1)
abstract class UserDatabaseRoom : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabaseRoom? = null

        fun getInstance(context: Context): UserDatabaseRoom {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabaseRoom::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}