package br.com.sample.word.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.sample.word.data.dao.WordDao
import br.com.sample.word.data.entity.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            wordDao.deleteAll()

            var word = Word(word = "Teste do app")
            wordDao.insert(word)
            word = Word(word = "Palavra")
            wordDao.insert(word)

        }
    }

        companion object {
            @Volatile // Singleton previne a abertura de multiplas instancias de base de dados ao mesmo tempo
            private var INSTANCE: WordRoomDatabase? = null

            fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
                //se a instancia nao for nula
                //criamos uma base
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordRoomDatabase::class.java,
                        "word_database"
                    ).addCallback(WordDatabaseCallback(scope))
                        .build()
                    INSTANCE = instance
                    instance
                }
            }
        }
}