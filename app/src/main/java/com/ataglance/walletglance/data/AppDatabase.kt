package com.ataglance.walletglance.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ataglance.walletglance.domain.dao.AccountDao
import com.ataglance.walletglance.domain.dao.CategoryDao
import com.ataglance.walletglance.domain.dao.RecordDao
import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.domain.entities.Record

@Database(
    entities = [Account::class, Category::class, Record::class],
    version = 2,
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

                Room.databaseBuilder(
                    context, AppDatabase::class.java, "app_data"
                )

                    .addMigrations(MIGRATION_1_2)

                    /*.addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            Log.d("App database", "Created")

                            super.onCreate(db)

                            val daoAccount = Instance?.accountDao
                            if (daoAccount != null) {
                                AccountViewModel(
                                    repository = AccountRepository(daoAccount),
                                    dao = daoAccount
                                )
                                    .addFirstAccount()
                            }
                        }
                    })*/

                    .build()
                    .also { Instance = it }

            }
        }
    }
}
