package com.raul

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raul.adapter.PokemonAdapter
import com.raul.api.RetrofitClient
import com.raul.response.PokemonDetailResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var edtSearch: EditText
    private lateinit var txtEmptyMessage: android.widget.TextView
    private lateinit var adapter: PokemonAdapter
    private val pokemonList = mutableListOf<PokemonDetailResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerViewPokemons)
        edtSearch = findViewById(R.id.editSearch)
        txtEmptyMessage = findViewById(R.id.txtEmptyMessage)

        adapter = PokemonAdapter(pokemonList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchInitialPokemons()

        setupSearchBar()
    }

    private var searchJob: kotlinx.coroutines.Job? = null

    private fun setupSearchBar(){
        edtSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(text: android.text.Editable?) {
                val query = text.toString().trim().lowercase()
                searchJob?.cancel()
                
                searchJob = lifecycleScope.launch {
                    kotlinx.coroutines.delay(500.milliseconds)
                    
                    if (query.isNotEmpty()) {
                        searchPokemon(query)
                    } else {
                        fetchInitialPokemons()
                    }
                }
            }
        })
    }

    private fun updateScreenList(list: List<PokemonDetailResponse>) {
        adapter.updateList(list)
        if (list.isEmpty()){
            recyclerView.visibility = android.view.View.GONE
            txtEmptyMessage.visibility = android.view.View.VISIBLE
        } else {
            recyclerView.visibility = android.view.View.VISIBLE
            txtEmptyMessage.visibility = android.view.View.GONE
        }
    }

    private fun searchPokemon(query: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getPokemonDetail(query)
                if (response.isSuccessful) {
                    val pokemon = response.body()
                    if (pokemon != null) {
                        updateScreenList(listOf(pokemon))
                        return@launch
                    }
                }
                
                val typeResponse = RetrofitClient.apiService.getTypeDetail(query)
                if(typeResponse.isSuccessful){
                    val typeResult = typeResponse.body()?.pokemon ?: emptyList()
                    val pokemonsOfType = typeResult.take(50).map { it.pokemon }
                    
                    val detailsDeferred = pokemonsOfType.map { item ->
                        val id = item.url.trimEnd('/').split("/").last()
                        async { RetrofitClient.apiService.getPokemonDetail(id) }
                    }
                    
                    val detailsResponses = detailsDeferred.awaitAll()
                    val validPokemons = detailsResponses.mapNotNull { it.body() }
                    
                    updateScreenList(validPokemons)
                }
                else{
                    updateScreenList(emptyList())
                }
            }
            catch (e: Exception) {
                Log.e("MainActivity", "Erro na pesquisa: ${e.message}")
                updateScreenList(emptyList())
            }
        }
    }

    private fun fetchInitialPokemons() {
        lifecycleScope.launch {
            try {
                val listResponse = RetrofitClient.apiService.getPokemons(limit = 30, offset = 0)
                
                if (listResponse.isSuccessful) {
                    val results = listResponse.body()?.results ?: emptyList()
                    
                    val detailsDeferred = results.map { item ->
                        val id = item.url.trimEnd('/').split("/").last()
                        async { RetrofitClient.apiService.getPokemonDetail(id) }
                    }
                    
                    val detailsResponses = detailsDeferred.awaitAll() 
                    val validPokemons = detailsResponses.mapNotNull { it.body() }
                    
                    updateScreenList(validPokemons) // Usa a nova função aqui
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Erro ao buscar Pokémons: ${e.message}")
            }
        }
    }
}