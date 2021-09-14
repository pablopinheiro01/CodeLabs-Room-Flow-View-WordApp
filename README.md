# CodeLabs-Room-Flow-View : Aplicativo Word

Repositorio para o aplicativo desenvolvido no Google CodeLabss com o tutorial atualizado para o uso do Room com View.

Este tutorial se encontra no link:

https://developer.android.com/codelabs/android-room-with-a-view-kotlin


Os componentes do app são os seguintes:
<ul>
<li><b>MainActivity</b>: exibe palavras em uma lista usando uma <b>RecyclerView</b> e o <b>WordListAdapter</b>. 
Na MainActivity, há um <b>Observer</b> que observa as palavras do banco de dados e é <b>notificado</b> quando elas mudam.

<li><b>NewWordActivity</b>: adiciona uma nova palavra à lista.</li>

<li>
  <b>WordViewModel</b>: fornece métodos para acessar a camada de dados e retorna <b>LiveData</b> para que a <b>MainActivity</b> possa configurar o relacionamento do <b>observer</b>.*
</li>
<li>
  <b>LiveData<List<Word>></b>: possibilita atualizações automáticas nos componentes da UI. É possível converter o <b>Flow</b> para <b>LiveData</b> chamando o método <b>flow.toLiveData()</b>.
</li>
<li>
  <b>Repository</b>: gerencia uma ou mais fontes de dados. O <b>Repository</b> expõe métodos para que o <b>ViewModel</b> interaja com o provedor de dados. Neste app, 
  o Repository trata como repositorio o banco de dados <b>Room</b>.
</li>
<li>
  <b>Room</b>: é um <b>wrapper</b> e implementa um banco de dados <b>SQLite</b>. O <b>Room</b> automatiza muitas tarefas manuais e facilita nossa vida.
</li>
<li>
  <b>DAO</b>: mapeia chamadas de método para consultas ao banco de dados, de modo que, quando o <b>repository</b> chamar um método, como <b>getAlphabetizedWords()</b>, o <b>Room</b>
  poderá executar <b>SELECT * FROM word_table ORDER BY word ASC</b>.
  O DAO pode expor consultas <b>suspend fun</b> para solicitações únicas e consultas do Flow quando quiser receber notificações de mudanças no banco de dados.
</li>
<li>
  <b>Word</b>: é a classe de entidade que contém uma única palavra.
</li>
<li>
  <b>Views e Activitys (e Fragments)</b> somente interagem com os dados usando o <b>ViewModel</b>. Dessa forma, não importa de onde vêm os dados.
</li>
</ul>

<h2>Fluxo de dados para atualizações automáticas da UI (UI reativa)</h2>

A atualização automática é possível porque você está usando o <b>LiveData</b>. Na <b>MainActivity</b>, há um <b>Observer</b> que observa o <b>LiveData</b> das palavras do 
banco de dados e é notificado quando elas mudam. Quando há uma mudança, o método <b>onChange()</b> do Observer é executado e atualiza o <b>Words</b> no <b>WordListAdapter</b>.

Os dados podem ser observados porque são <b>LiveData</b>. <b>LiveData<List<Word>></b> é o resultado observado retornado pela propriedade <b>allWords do WordViewModel</b>.

O <b>WordViewModel</b> oculta tudo sobre o back-end da camada de UI. Ele fornece métodos para acessar a camada de dados e retorna <b>LiveData</b> para que <b>MainActivity</b> 
possa configurar a relação do observador. <b>Views e Activities</b> (e <b>Fragments</b>) somente interagem com os dados usando o <b>ViewModel</b>.
Dessa forma, não importa de onde vêm os dados.

Nesse caso, eles são provenientes de um <b>Repository</b>. O <b>ViewModel</b> não precisa saber com o que o repositório interage.
Ele só precisa saber <b>como interagir com o Repository</b>, usando os <b>métodos expostos pelo Repository</b>.

O repositório gerencia uma ou mais fontes de dados. No app WordListSample, o gerenciamento é de um banco de dados do <b>Room</b>. 
O <b>Room</b> é um wrapper e implementa um banco de dados <b>SQLite</b>. O Room realiza muitas tarefas por você.
Por exemplo, o Room faz tudo o que você precisava fazer com uma classe <b>SQLiteOpenHelper</b>.

O <b>DAO</b> mapeia chamadas de método para consultas do banco de dados, de modo que, quando o repositório chamar um método como <b>getAllWords()</b>, o <b>Room</b> poderá
executar <b>SELECT * FROM word_table ORDER BY word ASC</b>.

Como o resultado retornado da consulta é observado em LiveData, toda vez que os dados no Room mudam, o método <b>onChanged()</b> da interface <b>Observer</b> é executado e a IU
é atualizada.

