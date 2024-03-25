package com.ataglance.walletglance.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Account::class, Category::class, Record::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract val accountDao: AccountDao
    abstract val categoryDao: CategoryDao
    abstract val recordDao: RecordDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {

                Log.d("App database", "Got")

                Room.databaseBuilder(
                    context, AppDatabase::class.java, "app_data"
                )

                    .fallbackToDestructiveMigration()

//                    .addCallback(object : Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            Log.d("App database", "Created")
//
//                            super.onCreate(db)
//
//                            val daoAccount = Instance?.accountDao
//                            if (daoAccount != null) {
//                                AccountViewModel(
//                                    repository = AccountRepository(daoAccount),
//                                    dao = daoAccount
//                                )
//                                    .addFirstAccount()
//                            }
//                        }
//                    })

                    .build()
                    .also { Instance = it }
            }
        }
    }
}
