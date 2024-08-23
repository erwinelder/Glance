package com.ataglance.walletglance.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ataglance.walletglance.data.local.dao.AccountDao
import com.ataglance.walletglance.data.local.dao.BudgetAccountAssociationDao
import com.ataglance.walletglance.data.local.dao.BudgetDao
import com.ataglance.walletglance.data.local.dao.CategoryCollectionCategoryAssociationDao
import com.ataglance.walletglance.data.local.dao.CategoryCollectionDao
import com.ataglance.walletglance.data.local.dao.CategoryDao
import com.ataglance.walletglance.data.local.dao.RecordDao
import com.ataglance.walletglance.data.local.entities.AccountEntity
import com.ataglance.walletglance.data.local.entities.BudgetAccountAssociation
import com.ataglance.walletglance.data.local.entities.BudgetEntity
import com.ataglance.walletglance.data.local.entities.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.data.local.entities.CategoryCollectionEntity
import com.ataglance.walletglance.data.local.entities.CategoryEntity
import com.ataglance.walletglance.data.local.entities.RecordEntity

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        CategoryCollectionEntity::class,
        CategoryCollectionCategoryAssociation::class,
        RecordEntity::class,
        BudgetEntity::class,
        BudgetAccountAssociation::class
    ],
    version = 7,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract val accountDao: AccountDao
    abstract val categoryDao: CategoryDao
    abstract val categoryCollectionDao: CategoryCollectionDao
    abstract val categoryCollectionCategoryAssociationDao: CategoryCollectionCategoryAssociationDao
    abstract val recordDao: RecordDao
    abstract val budgetDao: BudgetDao
    abstract val budgetAccountAssociationDao: BudgetAccountAssociationDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {

                Room.databaseBuilder(
                    context, AppDatabase::class.java, "app_data"
                )

                    .addMigrations(
                        MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6,
                        MIGRATION_6_7
                    )

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
