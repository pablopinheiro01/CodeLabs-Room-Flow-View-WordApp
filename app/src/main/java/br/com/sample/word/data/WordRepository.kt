package br.com.sample.word.data

import androidx.annotation.WorkerThread
import br.com.sample.word.data.dao.WordDao
import br.com.sample.word.data.entity.Word
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {

    //O Room vai executar todas as querys em uma thread separada
    //O Flow será observado e notificado quando os dados forem alterados
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizeWords()

    //Por default o Room suspende as queries fora da main thread.
    //portanto nós não precisamos implementar nada se nao estamos fazendo algo de longa duração no banco de dados
    //fora da main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word){
        wordDao.insert(word)
    }
}