package com.example.io_motion.data

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * One-shot demo seeder. [onCreate] fires exactly once, when the database file is first created, so
 * this never re-seeds and never touches an existing (real) user database. Registered in
 * [DataModule] only for `BuildConfig.DEBUG` builds, so no demo data ships in release.
 *
 * Ids are hard-coded (1, 2, …) safely: the tables are guaranteed empty inside [onCreate], so
 * AUTOINCREMENT assigns them in order.
 */
internal class DebugSeedCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        val now = System.currentTimeMillis()

        // "Morning Burn" — Squat ×3, Plank ×3 (plank reps column stores seconds of hold).
        db.execSQL("INSERT INTO workout_entities (name, createdAt, sortOrder) VALUES ('Morning Burn', $now, 0)")
        db.execSQL("INSERT INTO workout_item_entities (workoutId, exerciseType, sets, reps, position) VALUES (1, 'SQUAT', 3, 10, 0)")
        db.execSQL("INSERT INTO workout_item_entities (workoutId, exerciseType, sets, reps, position) VALUES (1, 'PLANK', 3, 30, 1)")

        // "Core Focus" — Sit-up ×4, Plank ×3.
        db.execSQL("INSERT INTO workout_entities (name, createdAt, sortOrder) VALUES ('Core Focus', ${now + 1}, 1)")
        db.execSQL("INSERT INTO workout_item_entities (workoutId, exerciseType, sets, reps, position) VALUES (2, 'SIT_UP', 4, 10, 0)")
        db.execSQL("INSERT INTO workout_item_entities (workoutId, exerciseType, sets, reps, position) VALUES (2, 'PLANK', 3, 30, 1)")
    }
}
