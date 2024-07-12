package com.ataglance.walletglance.domain

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