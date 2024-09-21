package com.ataglance.walletglance.core.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

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
