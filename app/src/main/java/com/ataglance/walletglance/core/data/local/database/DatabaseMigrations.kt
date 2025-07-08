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
        TableName.Record.name,
        TableName.Budget.name,
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
        TableName.Record.name,
        TableName.Budget.name,
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

        /* Rename tables to have "legacy" prefix */

        db.execSQL("ALTER TABLE NavigationButton RENAME TO legacy_navigation_button")

        db.execSQL("ALTER TABLE Widget RENAME TO legacy_widget")

        db.execSQL("ALTER TABLE BudgetOnWidget RENAME TO legacy_budget_on_widget")
        db.execSQL("DROP INDEX IF EXISTS index_BudgetOnWidget_budgetId")

        db.execSQL("ALTER TABLE BudgetAccountAssociation RENAME TO legacy_budget_account_association")
        db.execSQL("DROP INDEX IF EXISTS index_BudgetAccountAssociation_budgetId")
        db.execSQL("DROP INDEX IF EXISTS index_BudgetAccountAssociation_accountId")

        db.execSQL("ALTER TABLE Budget RENAME TO legacy_budget")
        db.execSQL("DROP INDEX IF EXISTS index_Budget_categoryId")

        db.execSQL("ALTER TABLE CategoryCollectionCategoryAssociation RENAME TO legacy_category_collection_category_association")
        db.execSQL("DROP INDEX IF EXISTS index_CategoryCollectionCategoryAssociation_collectionId")
        db.execSQL("DROP INDEX IF EXISTS index_CategoryCollectionCategoryAssociation_categoryId")

        db.execSQL("ALTER TABLE CategoryCollection RENAME TO legacy_category_collection")

        db.execSQL("ALTER TABLE Record RENAME TO legacy_record")
        db.execSQL("DROP INDEX IF EXISTS index_Record_accountId")

        db.execSQL("ALTER TABLE Category RENAME TO legacy_category")

        db.execSQL("ALTER TABLE Account RENAME TO legacy_account")


        /* Migrate Account table */

        db.execSQL("""
            CREATE TABLE account (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                orderNum INTEGER NOT NULL,
                name TEXT NOT NULL,
                currency TEXT NOT NULL,
                balance REAL NOT NULL,
                color TEXT NOT NULL,
                hide INTEGER NOT NULL,
                hideBalance INTEGER NOT NULL,
                withoutBalance INTEGER NOT NULL,
                timestamp INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO account (id, orderNum, name, currency, balance, color, hide, hideBalance, withoutBalance, timestamp, deleted)
            SELECT id, orderNum, name, currency, balance, color, hide, hideBalance, withoutBalance, $timestamp, ${false}
            FROM legacy_account
        """.trimIndent())


        /* Migrate Category table */

        db.execSQL("""
            CREATE TABLE category (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                type INTEGER NOT NULL,
                orderNum INTEGER NOT NULL,
                parentCategoryId INTEGER,
                name TEXT NOT NULL,
                iconName TEXT NOT NULL,
                colorName TEXT NOT NULL,
                timestamp INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO category (id, type, orderNum, parentCategoryId, name, iconName, colorName, timestamp, deleted)
            SELECT id, type, orderNum, parentCategoryId, name, iconName, colorName, $timestamp, ${false}
            FROM legacy_category
        """.trimIndent())


        /* Migrate Record and RecordItem table */

        db.execSQL("""
            CREATE TABLE record (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                date INTEGER NOT NULL,
                type INTEGER NOT NULL,
                accountId INTEGER NOT NULL REFERENCES account(id) ON DELETE CASCADE,
                includeInBudgets INTEGER NOT NULL,
                timestamp INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX index_record_accountId ON record(accountId)")

        db.execSQL("""
            CREATE TABLE record_item (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                recordId INTEGER NOT NULL REFERENCES record(id) ON DELETE CASCADE,
                totalAmount REAL NOT NULL,
                quantity INTEGER,
                categoryId INTEGER NOT NULL,
                subcategoryId INTEGER,
                note TEXT
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX index_record_item_recordId ON record_item(recordId)")

        val rCursor = db.query("""
            SELECT recordNum, date, type, accountId, amount, quantity, categoryId, subcategoryId, note, includeInBudgets FROM legacy_record
            WHERE type IN (45, 43)
        """.trimIndent())
        while (rCursor.moveToNext()) {
            val recordNum = rCursor.getInt(0)
            val date = readableTimestampToEpochTimestamp(rCursor.getLong(1))
            val type = rCursor.getString(2)[0]
            val accountId = rCursor.getInt(3)
            val amount = rCursor.getDouble(4)
            val quantity = rCursor.getIntOrNull(5)
            val categoryId = rCursor.getInt(6)
            val subcategoryId = rCursor.getIntOrNull(7)
            val note = rCursor.getStringOrNull(8)
            val includeInBudgets = rCursor.getInt(9) == 1
            db.execSQL(
                "INSERT INTO record (id, date, type, accountId, includeInBudgets, timestamp, deleted) VALUES (?, ?, ?, ?, ?, ?, ?)",
                arrayOf<Any?>(recordNum + 1, date, type, accountId, includeInBudgets, timestamp, false)
            )
            db.execSQL(
                "INSERT INTO record_item (recordId, totalAmount, quantity, categoryId, subcategoryId, note) VALUES (?, ?, ?, ?, ?, ?)",
                arrayOf<Any?>(recordNum + 1, amount, quantity, categoryId, subcategoryId, note)
            )
        }
        rCursor.close()


        /* Migrate Transfer table */

        db.execSQL("""
            CREATE TABLE transfer (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                date INTEGER NOT NULL,
                senderAccountId INTEGER NOT NULL REFERENCES account(id) ON DELETE CASCADE,
                receiverAccountId INTEGER NOT NULL REFERENCES account(id) ON DELETE CASCADE,
                senderAmount REAL NOT NULL,
                receiverAmount REAL NOT NULL,
                senderRate REAL NOT NULL,
                receiverRate REAL NOT NULL,
                includeInBudgets INTEGER NOT NULL,
                timestamp INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX index_transfer_senderAccountId ON transfer(senderAccountId)")
        db.execSQL("CREATE INDEX index_transfer_receiverAccountId ON transfer(receiverAccountId)")

        val transferNums = mutableListOf<Int>()
        val tnCursor = db.query("""
            SELECT recordNum FROM legacy_record
            WHERE type IN (60, 62)
            GROUP BY recordNum
            HAVING COUNT(*) = 2
        """.trimIndent())
        while (tnCursor.moveToNext()) {
            transferNums.add(tnCursor.getInt(0))
        }
        tnCursor.close()

        for (recordNum in transferNums) {

            val rCursor = db.query(
                """
                    SELECT id, date, type, accountId, amount, includeInBudgets
                    FROM legacy_record
                    WHERE recordNum = ?
                    ORDER BY type DESC
                """.trimIndent(),
                arrayOf<Any?>(recordNum)
            )
            val records = mutableListOf<MutableMap<String, Any?>>()
            while (rCursor.moveToNext()) {
                records.add(
                    mutableMapOf(
                        "id" to rCursor.getInt(0),
                        "date" to rCursor.getLong(1),
                        "type" to rCursor.getInt(2),
                        "accountId" to rCursor.getInt(3),
                        "amount" to rCursor.getDouble(4),
                        "includeInBudgets" to rCursor.getInt(5)
                    )
                )
            }
            rCursor.close()

            if (records.size != 2) continue

            val sender = records[0]
            val receiver = records[1]
            sender.put("rate", 1.0)
            receiver.put(
                "rate",
                receiver["amount"].toString().toDouble() / sender["amount"].toString().toDouble()
            )
            db.execSQL(
                """
                    INSERT INTO transfer (
                        date,
                        senderAccountId, receiverAccountId,
                        senderAmount, receiverAmount,
                        senderRate, receiverRate,
                        includeInBudgets, timestamp, deleted
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                arrayOf(
                    sender["date"],
                    sender["accountId"],
                    receiver["accountId"],
                    sender["amount"],
                    receiver["amount"],
                    sender["rate"],
                    receiver["rate"],
                    sender["includeInBudgets"],
                    timestamp,
                    false
                )
            )
        }


        /* Migrate CategoryCollection table */

        db.execSQL("""
            CREATE TABLE category_collection (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                orderNum INTEGER NOT NULL,
                type INTEGER NOT NULL,
                name TEXT NOT NULL,
                timestamp INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO category_collection (id, orderNum, type, name, timestamp, deleted)
            SELECT id, orderNum, type, name, $timestamp, ${false}
            FROM legacy_category_collection
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE category_collection_category_association (
                collectionId INTEGER NOT NULL REFERENCES category_collection(id) ON DELETE CASCADE,
                categoryId INTEGER NOT NULL REFERENCES category(id) ON DELETE CASCADE,
                PRIMARY KEY (collectionId, categoryId)
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX index_category_collection_category_association_collectionId ON category_collection_category_association(collectionId)")
        db.execSQL("CREATE INDEX index_category_collection_category_association_categoryId ON category_collection_category_association(categoryId)")

        db.execSQL("""
            CREATE TRIGGER delete_orphan_category_collections
            AFTER DELETE ON category_collection_category_association
            BEGIN
                DELETE FROM category_collection
                WHERE id NOT IN (
                    SELECT collectionId FROM category_collection_category_association
                );
            END;
        """.trimIndent())

        db.execSQL("""
            INSERT INTO category_collection_category_association (collectionId, categoryId)
            SELECT categoryCollectionId, categoryId
            FROM legacy_category_collection_category_association
        """.trimIndent())


        /* Migrate Budget table */

        db.execSQL("""
            CREATE TABLE budget (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                amountLimit REAL NOT NULL,
                categoryId INTEGER NOT NULL REFERENCES category(id) ON DELETE CASCADE,
                name TEXT NOT NULL,
                repeatingPeriod TEXT NOT NULL,
                timestamp INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX index_budget_categoryId ON budget(categoryId)")

        db.execSQL("""
            INSERT INTO budget (id, amountLimit, categoryId, name, repeatingPeriod, timestamp, deleted)
            SELECT id, amountLimit, categoryId, name, repeatingPeriod, $timestamp, ${false}
            FROM legacy_budget
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE budget_account_association (
                budgetId INTEGER NOT NULL REFERENCES budget(id) ON DELETE CASCADE,
                accountId INTEGER NOT NULL REFERENCES account(id) ON DELETE CASCADE,
                PRIMARY KEY (budgetId, accountId)
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX index_budget_account_association_budgetId ON budget_account_association(budgetId)")
        db.execSQL("CREATE INDEX index_budget_account_association_accountId ON budget_account_association(accountId)")

        db.execSQL("""
            CREATE TRIGGER delete_orphan_budgets
            AFTER DELETE ON budget_account_association
            BEGIN
                DELETE FROM budget
                WHERE id NOT IN (
                    SELECT budgetId FROM budget_account_association
                );
            END;
        """.trimIndent())

        db.execSQL("""
            INSERT INTO budget_account_association (budgetId, accountId)
            SELECT budgetId, accountId
            FROM legacy_budget_account_association
        """.trimIndent())


        /* Migrate BudgetOnWidget table */

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS budget_on_widget (
                budgetId INTEGER PRIMARY KEY NOT NULL REFERENCES budget(id) ON DELETE CASCADE,
                timestamp INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX IF NOT EXISTS index_budget_on_widget_budgetId ON budget_on_widget(budgetId)")

        db.execSQL("""
            INSERT INTO budget_on_widget (budgetId, timestamp, deleted)
            SELECT budgetId, $timestamp, ${false}
            FROM legacy_budget_on_widget
        """.trimIndent())


        /* Migrate Widget table */

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS widget (
                name TEXT PRIMARY KEY NOT NULL,
                orderNum INTEGER NOT NULL,
                timestamp INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO widget (name, orderNum, timestamp, deleted)
            SELECT name, orderNum, $timestamp, ${false}
            FROM legacy_widget
        """.trimIndent())


        /* Migrate NavigationButton table */

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS navigation_button (
                screenName TEXT PRIMARY KEY NOT NULL,
                orderNum INTEGER NOT NULL,
                timestamp INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO navigation_button (screenName, orderNum, timestamp, deleted)
            SELECT screenName, orderNum, $timestamp, ${false}
            FROM legacy_navigation_button
        """.trimIndent())


        /* Drop legacy tables */

        db.execSQL("DROP TABLE IF EXISTS legacy_navigation_button")
        db.execSQL("DROP TABLE IF EXISTS legacy_widget")
        db.execSQL("DROP TABLE IF EXISTS legacy_budget_on_widget")
        db.execSQL("DROP TABLE IF EXISTS legacy_budget_account_association")
        db.execSQL("DROP TABLE IF EXISTS legacy_budget")
        db.execSQL("DROP TABLE IF EXISTS legacy_category_collection_category_association")
        db.execSQL("DROP TABLE IF EXISTS legacy_category_collection")
        db.execSQL("DROP TABLE IF EXISTS legacy_record")
        db.execSQL("DROP TABLE IF EXISTS legacy_category")
        db.execSQL("DROP TABLE IF EXISTS legacy_account")

        db.execSQL("""
            DELETE FROM local_update_time
        """.trimIndent())

    }

}
