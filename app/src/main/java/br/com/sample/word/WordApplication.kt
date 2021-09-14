package br.com.sample.word

import android.app.Application
import br.com.sample.word.base.WordRoomDatabase
import br.com.sample.word.data.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordApplication: Application() {
    //Este escopo nao tem a necessidade de ser cancelado, pois ele será destruido com o processo da app
    val applicationScope = CoroutineScope(SupervisorJob())

    //usamos o carregamento lento no caso de criação quando for necessario
    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }
}