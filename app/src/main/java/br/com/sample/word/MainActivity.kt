package br.com.sample.word

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.sample.word.data.entity.Word
import br.com.sample.word.presentation.WordViewModel
import br.com.sample.word.presentation.WordViewModelFactory
import br.com.sample.word.ui.recyclerview.adapter.WordListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordApplication).repository)
    }

    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //adicionei um observador para o LiveData retornado pelo getAlphabetizeWords
        // O onChanged() sera observado quando os dados mudarem e a activity em primeiro plano
        wordViewModel.allWords.observe(this, Observer { words ->
            //atualiza o cache e copia a lista de palavras para o adapter
            words?.let { adapter.submitList(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newWordActivityRequestCode &&
                resultCode == Activity.RESULT_OK){
            data?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let {
                val word = Word(word = it)
                wordViewModel.insert(word)
            }
        } else{
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }
}