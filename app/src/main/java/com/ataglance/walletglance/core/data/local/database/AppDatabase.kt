package com.ataglance.walletglance.core.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ataglance.walletglance.account.data.local.dao.AccountLocalDao
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.budget.data.local.dao.BudgetLocalDao
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.category.data.local.dao.CategoryLocalDao
import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.categoryCollection.data.local.dao.CategoryCollectionLocalDao
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.model.LocalUpdateTime
import com.ataglance.walletglance.navigation.data.local.NavigationButtonDao
import com.ataglance.walletglance.navigation.data.model.NavigationButtonEntity
import com.ataglance.walletglance.personalization.data.local.BudgetOnWidgetDao
import com.ataglance.walletglance.personalization.data.local.WidgetDao
import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.personalization.data.model.WidgetEntity
import com.ataglance.walletglance.record.data.local.dao.RecordLocalDao
import com.ataglance.walletglance.record.data.local.model.RecordEntity

@Database(
    entities = [
        LocalUpdateTime::class,
        AccountEntity::class,
        CategoryEntity::class,
        CategoryCollectionEntity::class,
        CategoryCollectionCategoryAssociation::class,
        RecordEntity::class,
        BudgetEntity::class,
        BudgetAccountAssociation::class,
        NavigationButtonEntity::class,
        WidgetEntity::class,
        BudgetOnWidgetEntity::class
    ],
    version = 11,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract val localUpdateTimeDao: LocalUpdateTimeDao
    abstract val accountDao: AccountLocalDao
    abstract val categoryDao: CategoryLocalDao
    abstract val categoryCollectionDao: CategoryCollectionLocalDao
    abstract val recordDao: RecordLocalDao
    abstract val budgetDao: BudgetLocalDao
    abstract val navigationButtonDao: NavigationButtonDao
    abstract val widgetDao: WidgetDao
    abstract val budgetOnWidgetDao: BudgetOnWidgetDao

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
                        MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11
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
