package com.ataglance.walletglance.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ataglance.walletglance.account.data.local.dao.AccountDao
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.appearanceSettings.data.local.dao.WidgetDao
import com.ataglance.walletglance.appearanceSettings.data.local.model.WidgetEntity
import com.ataglance.walletglance.budget.data.local.dao.BudgetAccountAssociationDao
import com.ataglance.walletglance.budget.data.local.dao.BudgetDao
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.category.data.local.dao.CategoryDao
import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.categoryCollection.data.local.dao.CategoryCollectionCategoryAssociationDao
import com.ataglance.walletglance.categoryCollection.data.local.dao.CategoryCollectionDao
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.navigation.data.local.dao.NavigationButtonDao
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.record.data.local.dao.RecordDao
import com.ataglance.walletglance.record.data.local.model.RecordEntity

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        CategoryCollectionEntity::class,
        CategoryCollectionCategoryAssociation::class,
        RecordEntity::class,
        BudgetEntity::class,
        BudgetAccountAssociation::class,
        NavigationButtonEntity::class,
        WidgetEntity::class
    ],
    version = 9,
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
    abstract val navigationButtonDao: NavigationButtonDao
    abstract val widgetDao: WidgetDao

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
                        MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9
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
