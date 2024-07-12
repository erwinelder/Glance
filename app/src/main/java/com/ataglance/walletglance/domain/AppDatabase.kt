package com.ataglance.walletglance.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ataglance.walletglance.domain.dao.AccountDao
import com.ataglance.walletglance.domain.dao.CategoryCollectionCategoryAssociationDao
import com.ataglance.walletglance.domain.dao.CategoryCollectionDao
import com.ataglance.walletglance.domain.dao.CategoryDao
import com.ataglance.walletglance.domain.dao.RecordDao
import com.ataglance.walletglance.domain.entities.AccountEntity
import com.ataglance.walletglance.domain.entities.CategoryCollection
import com.ataglance.walletglance.domain.entities.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.domain.entities.CategoryEntity
import com.ataglance.walletglance.domain.entities.Record

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        CategoryCollection::class,
        CategoryCollectionCategoryAssociation::class,
        Record::class
    ],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract val accountDao: AccountDao
    abstract val categoryDao: CategoryDao
    abstract val categoryCollectionDao: CategoryCollectionDao
    abstract val categoryCollectionCategoryAssociationDao: CategoryCollectionCategoryAssociationDao
    abstract val recordDao: RecordDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {

                Room.databaseBuilder(
                    context, AppDatabase::class.java, "app_data"
                )

                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)

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
