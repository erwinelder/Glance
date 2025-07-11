package com.ataglance.walletglance.core.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ataglance.walletglance.account.data.local.dao.AccountLocalDao
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.budget.data.local.dao.BudgetLocalDao
import com.ataglance.walletglance.budget.data.local.dao.BudgetOnWidgetLocalDao
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociationEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.category.data.local.dao.CategoryLocalDao
import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.categoryCollection.data.local.dao.CategoryCollectionLocalDao
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociationEntity
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.model.LocalUpdateTime
import com.ataglance.walletglance.navigation.data.local.dao.NavigationButtonLocalDao
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.personalization.data.local.dao.WidgetLocalDao
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import com.ataglance.walletglance.record.data.local.dao.RecordLocalDao
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.local.model.RecordItemEntity
import com.ataglance.walletglance.transfer.data.local.dao.TransferLocalDao
import com.ataglance.walletglance.transfer.data.local.model.TransferEntity

@Database(
    entities = [
        LocalUpdateTime::class,
        AccountEntity::class,
        CategoryEntity::class,
        CategoryCollectionEntity::class,
        CategoryCollectionCategoryAssociationEntity::class,
        RecordEntity::class,
        RecordItemEntity::class,
        TransferEntity::class,
        BudgetEntity::class,
        BudgetAccountAssociationEntity::class,
        NavigationButtonEntity::class,
        WidgetEntity::class,
        BudgetOnWidgetEntity::class
    ],
    version = 13,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract val localUpdateTimeDao: LocalUpdateTimeDao
    abstract val accountDao: AccountLocalDao
    abstract val categoryDao: CategoryLocalDao
    abstract val categoryCollectionDao: CategoryCollectionLocalDao
    abstract val recordDao: RecordLocalDao
    abstract val transferDao: TransferLocalDao
    abstract val budgetDao: BudgetLocalDao
    abstract val navigationButtonDao: NavigationButtonLocalDao
    abstract val widgetDao: WidgetLocalDao
    abstract val budgetOnWidgetDao: BudgetOnWidgetLocalDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {

                Room
                    .databaseBuilder(
                        context, AppDatabase::class.java, "app_data"
                    )
                    .addMigrations(
                        MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6,
                        MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11,
                        MIGRATION_11_12, MIGRATION_12_13
                    )
                    .build()
                    .also { Instance = it }

            }
        }
    }
}
