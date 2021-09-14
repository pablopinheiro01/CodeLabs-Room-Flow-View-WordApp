package br.com.sample.word.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.sample.word.data.entity.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM word_table ORDER BY word ASC")
//    fun getAlphabetizeWords(): List<Word>
    //vamos alterar a assinatura para permitir uma sequencia de chamadas assincronas no banco de dados
    //utilizando o Flow do Kotlin
    fun getAlphabetizeWords(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word:Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll(): Int
}