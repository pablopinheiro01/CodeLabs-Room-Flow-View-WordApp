package br.com.sample.word.presentation

import androidx.lifecycle.*
import br.com.sample.word.data.WordRepository
import br.com.sample.word.data.entity.Word
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    //usar o livedata para cachear todas as palavras tem varios beneficios:
    // Nós podemos inserir um observador dos dados(ao invés de um polling de mudanças)
    // E atualizar a UI com as mudanças feitas
    //O Repository esta separado da UI pelo ViewModel
    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    //Iniciamos uma Coroutine para inserir os dados no fluxo não bloqueante
    //inicializamos uma nova Coroutine chamando a inserção do repository, que é uma suspend fun
    fun insert(word:Word) = viewModelScope.launch {
        repository.insert(word)
    }

}

//Ao criar essa Factory eu garanto a sobrevivencia da View a mudanças de configurações
//E mesmo que a activity seja recriada eu recebo a instancia correta da View.
class WordViewModelFactory(private val repository: WordRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }

}