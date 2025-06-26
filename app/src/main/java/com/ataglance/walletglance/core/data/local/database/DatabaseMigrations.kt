package com.ataglance.walletglance.core.data.local.database

import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.ExperimentalTime

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Category ADD COLUMN colorName TEXT NOT NULL DEFAULT 'GrayDefault'")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS CategoryCollection (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                orderNum INTEGER NOT NULL,
                type INTEGER NOT NULL,
                name TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS CategoryCollectionCategoryAssociation (
                categoryCollectionId INTEGER NOT NULL,
                categoryId INTEGER NOT NULL,
                PRIMARY KEY (categoryCollectionId, categoryId),
                FOREIGN KEY (categoryCollectionId) REFERENCES CategoryCollection(id) ON DELETE CASCADE,
                FOREIGN KEY (categoryId) REFERENCES Category(id) ON DELETE CASCADE
            )
        """.trimIndent())

    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("UPDATE Category SET parentCategoryId = NULL WHERE rank = 99")

        db.execSQL("ALTER TABLE Category RENAME TO Category_old")

        db.execSQL("""
            CREATE TABLE Category (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                type INTEGER NOT NULL,
                orderNum INTEGER NOT NULL,
                parentCategoryId INTEGER,
                name TEXT NOT NULL,
                iconName TEXT NOT NULL,
                colorName TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO Category (id, type, orderNum, parentCategoryId, name, iconName, colorName)
            SELECT id, type, orderNum, parentCategoryId, name, iconName, colorName
            FROM Category_old
        """.trimIndent())

        db.execSQL("DROP TABLE Category_old")

    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Record_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                recordNum INTEGER NOT NULL,
                date INTEGER NOT NULL,
                type INTEGER NOT NULL,
                accountId INTEGER NOT NULL,
                amount REAL NOT NULL,
                quantity INTEGER,
                categoryId INTEGER NOT NULL,
                subcategoryId INTEGER,
                note TEXT,
                FOREIGN KEY (accountId) REFERENCES Account(id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO Record_new (id, recordNum, date, type, accountId, amount, quantity, categoryId, subcategoryId, note)
            SELECT id, recordNum, date, type, accountId, amount, quantity, categoryId, subcategoryId, note
            FROM Record
        """.trimIndent())

        db.execSQL("DROP TABLE Record")

        db.execSQL("ALTER TABLE Record_new RENAME TO Record")

    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_CategoryCollectionCategoryAssociation_categoryId
            ON CategoryCollectionCategoryAssociation(categoryId)
        """.trimIndent())
    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("ALTER TABLE Record ADD COLUMN includeInBudgets INTEGER NOT NULL DEFAULT '1'")

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_Record_accountId
            ON Record(accountId)
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Budget (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                amountLimit REAL NOT NULL,
                categoryId INTEGER NOT NULL,
                name TEXT NOT NULL,
                repeatingPeriod TEXT NOT NULL,
                FOREIGN KEY (categoryId) REFERENCES Category(id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_Budget_categoryId
            ON Budget(categoryId)
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS BudgetAccountAssociation (
                budgetId INTEGER NOT NULL,
                accountId INTEGER NOT NULL,
                PRIMARY KEY (budgetId, accountId),
                FOREIGN KEY (budgetId) REFERENCES Budget(id) ON DELETE CASCADE,
                FOREIGN KEY (accountId) REFERENCES Account(id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_BudgetAccountAssociation_accountId
            ON BudgetAccountAssociation(accountId)
        """.trimIndent())

    }
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS NavigationButton (
                screenName TEXT PRIMARY KEY NOT NULL,
                orderNum INTEGER NOT NULL
            )
        """.trimIndent())

    }
}

val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Widget (
                name TEXT PRIMARY KEY NOT NULL,
                orderNum INTEGER NOT NULL
            )
        """.trimIndent())

    }
}

val MIGRATION_9_10 = object : Migration(9, 10) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS BudgetOnWidget (
                budgetId INTEGER PRIMARY KEY NOT NULL,
                FOREIGN KEY (budgetId) REFERENCES Budget(id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_BudgetOnWidget_budgetId
            ON BudgetOnWidget(budgetId)
        """.trimIndent())

    }
}

val MIGRATION_10_11 = object : Migration(10, 11) {

    val timestamp: Long = getCurrentTimestamp()
    val tableNames: List<String> = listOf(
        TableName.Account.name,
        TableName.Category.name,
        TableName.CategoryCollection.name,
        TableName.CategoryCollectionCategoryAssociation.name,
        TableName.Record.name,
        TableName.Budget.name,
        TableName.BudgetAccountAssociation.name,
        TableName.NavigationButton.name,
        TableName.Widget.name,
        TableName.BudgetOnWidget.name
    )

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS local_update_time (
                tableName TEXT PRIMARY KEY NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())

        tableNames.forEach { tableName ->
            db.execSQL("""
                INSERT INTO local_update_time (tableName, timestamp)
                VALUES ('$tableName', $timestamp)
            """.trimIndent())
        }

    }
}

val MIGRATION_11_12 = object : Migration(11, 12) {

    val timestamp: Long = getCurrentTimestamp()
    val tableNames: List<String> = listOf(
        TableName.Account.name,
        TableName.Category.name,
        TableName.CategoryCollection.name,
        TableName.CategoryCollectionCategoryAssociation.name,
        TableName.Record.name,
        TableName.Budget.name,
        TableName.BudgetAccountAssociation.name,
        TableName.NavigationButton.name,
        TableName.Widget.name,
        TableName.BudgetOnWidget.name
    )

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            DROP TABLE IF EXISTS TableUpdateTime
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS local_update_time (
                tableName TEXT PRIMARY KEY NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())

        tableNames.forEach { tableName ->
            db.execSQL("""
                REPLACE INTO local_update_time (tableName, timestamp)
                VALUES ('$tableName', $timestamp)
            """.trimIndent())
        }

    }
}

val MIGRATION_12_13 = object : Migration(12, 13) {

    val timestamp = getCurrentTimestamp()

    @OptIn(ExperimentalTime::class)
    val readableTimestampToEpochTimestamp: (Long) -> Long = { readableTimestamp ->
        val year = (readableTimestamp / 100000000).toInt()
        val month = (readableTimestamp / 1000000 - year * 100).toInt()
        val day = (readableTimestamp / 10000 - year * 10000 - month * 100).toInt()
        val hour = (readableTimestamp / 100 - year * 1000000 - month * 10000 - day * 100).toInt()
        val minute = (readableTimestamp - year * 100000000 - month * 1000000 - day * 10000 - hour * 100).toInt()
        LocalDateTime(year, month, day, hour, minute).toInstant(TimeZone.UTC).toEpochMilliseconds()
    }

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS account (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                orderNum INTEGER NOT NULL,
                name TEXT NOT NULL,
                currency TEXT NOT NULL,
                balance REAL NOT NULL,
                color TEXT NOT NULL,
                hide INTEGER NOT NULL,
                hideBalance INTEGER NOT NULL,
                withoutBalance INTEGER NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            INSERT INTO account (id, orderNum, name, currency, balance, color, hide, hideBalance, withoutBalance, timestamp)
            SELECT id, orderNum, name, currency, balance, color, hide, hideBalance, withoutBalance, $timestamp
            FROM Account
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS category (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                orderNum INTEGER NOT NULL,
                parentCategoryId INTEGER,
                name TEXT NOT NULL,
                iconName TEXT NOT NULL,
                colorName TEXT NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            INSERT INTO category (id, type, orderNum, parentCategoryId, name, iconName, colorName, timestamp)
            SELECT id, type, orderNum, parentCategoryId, name, iconName, colorName, $timestamp
            FROM Category
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS record (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                recordNum INTEGER NOT NULL,
                date INTEGER NOT NULL,
                type TEXT NOT NULL,
                accountId INTEGER NOT NULL REFERENCES account(id) ON DELETE CASCADE,
                amount REAL NOT NULL,
                quantity INTEGER,
                categoryId INTEGER NOT NULL,
                subcategoryId INTEGER,
                note TEXT,
                includeInBudgets INTEGER NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_record_accountId ON record(accountId)
        """.trimIndent())
        val cursor = db.query("SELECT id, recordNum, date, type, accountId, amount, quantity, categoryId, subcategoryId, note, includeInBudgets FROM Record")
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val recordNum = cursor.getInt(1)
            val date = readableTimestampToEpochTimestamp(cursor.getLong(2))
            val type = cursor.getString(3)[0]
            val accountId = cursor.getInt(4)
            val amount = cursor.getDouble(5)
            val quantity = cursor.getIntOrNull(6)
            val categoryId = cursor.getInt(7)
            val subcategoryId = cursor.getIntOrNull(8)
            val note = cursor.getStringOrNull(9)
            val includeInBudgets = cursor.getInt(10)
            db.execSQL(
                "INSERT INTO record (id, recordNum, date, type, accountId, amount, quantity, categoryId, subcategoryId, note, includeInBudgets, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                arrayOf<Any?>(id, recordNum, date, type, accountId, amount, quantity, categoryId, subcategoryId, note, includeInBudgets, timestamp)
            )
        }
        cursor.close()

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS category_collection (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                orderNum INTEGER NOT NULL,
                type TEXT NOT NULL,
                name TEXT NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            INSERT INTO category_collection (id, orderNum, type, name, timestamp)
            SELECT id, orderNum, type, name, $timestamp
            FROM CategoryCollection
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS category_collection_category_association (
                categoryCollectionId INTEGER NOT NULL REFERENCES category_collection(id) ON DELETE CASCADE,
                categoryId INTEGER NOT NULL REFERENCES category(id) ON DELETE CASCADE,
                timestamp INTEGER NOT NULL,
                PRIMARY KEY (categoryCollectionId, categoryId)
            )
        """.trimIndent())
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_category_collection_category_association_categoryCollectionId ON category_collection_category_association(categoryCollectionId)
        """.trimIndent())
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_category_collection_category_association_categoryId ON category_collection_category_association(categoryId)
        """.trimIndent())
        db.execSQL("""
            INSERT INTO category_collection_category_association (categoryCollectionId, categoryId, timestamp)
            SELECT categoryCollectionId, categoryId, $timestamp
            FROM CategoryCollectionCategoryAssociation
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS budget (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                amountLimit REAL NOT NULL,
                categoryId INTEGER NOT NULL REFERENCES category(id) ON DELETE CASCADE,
                name TEXT NOT NULL,
                repeatingPeriod TEXT NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_budget_categoryId ON budget(categoryId)
        """.trimIndent())
        db.execSQL("""
            INSERT INTO budget (id, amountLimit, categoryId, name, repeatingPeriod, timestamp)
            SELECT id, amountLimit, categoryId, name, repeatingPeriod, $timestamp
            FROM Budget
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS budget_account_association (
                budgetId INTEGER NOT NULL REFERENCES budget(id) ON DELETE CASCADE,
                accountId INTEGER NOT NULL REFERENCES account(id) ON DELETE CASCADE,
                timestamp INTEGER NOT NULL,
                PRIMARY KEY (budgetId, accountId)
            )
        """.trimIndent())
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_budget_account_association_budgetId ON budget_account_association(budgetId)
        """.trimIndent())
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_budget_account_association_accountId ON budget_account_association(accountId)
        """.trimIndent())
        db.execSQL("""
            INSERT INTO budget_account_association (budgetId, accountId, timestamp)
            SELECT budgetId, accountId, $timestamp
            FROM BudgetAccountAssociation
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS budget_on_widget (
                budgetId INTEGER PRIMARY KEY REFERENCES budget(id) ON DELETE CASCADE,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS index_budget_on_widget_budgetId ON budget_on_widget(budgetId)
        """.trimIndent())
        db.execSQL("""
            INSERT INTO budget_on_widget (budgetId, timestamp)
            SELECT budgetId, $timestamp
            FROM BudgetOnWidget
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS widget (
                name TEXT PRIMARY KEY,
                orderNum INTEGER NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            INSERT INTO widget (name, orderNum, timestamp)
            SELECT name, orderNum, $timestamp
            FROM Widget
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS navigation_button (
                screenName TEXT PRIMARY KEY,
                orderNum INTEGER NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            INSERT INTO navigation_button (screenName, orderNum, timestamp)
            SELECT screenName, orderNum, $timestamp
            FROM NavigationButton
        """.trimIndent())

        db.execSQL("""
            DROP TABLE IF EXISTS NavigationButton
        """.trimIndent())
        db.execSQL("""
            DROP TABLE IF EXISTS Widget
        """.trimIndent())
        db.execSQL("""
            DROP TABLE IF EXISTS BudgetOnWidget
        """.trimIndent())
        db.execSQL("""
            DROP TABLE IF EXISTS BudgetAccountAssociation
        """.trimIndent())
        db.execSQL("""
            DROP TABLE IF EXISTS Budget
        """.trimIndent())
        db.execSQL("""
            DROP TABLE IF EXISTS CategoryCollectionCategoryAssociation
        """.trimIndent())
        db.execSQL("""
            DROP TABLE IF EXISTS CategoryCollection
        """.trimIndent())
        db.execSQL("""
            DROP TABLE IF EXISTS Record
        """.trimIndent())
        db.execSQL("""
            DROP TABLE IF EXISTS Category
        """.trimIndent())
        db.execSQL("""
            DROP TABLE IF EXISTS Account
        """.trimIndent())

    }

}
