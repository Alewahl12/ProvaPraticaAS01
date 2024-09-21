package com.example.provapratica01

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "Cadastro") {
                composable("Cadastro") {
                    TelaCadastroProduto(onCadastrarProduto = { produto ->
                        Estoque().adicionarProduto(produto)
                        Toast.makeText(this@MainActivity, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        navController.navigate("Lista")
                    })
                }
                composable("Lista") {
                    TelaListaProdutos(onDetalhesClick = { produto ->
                        // Serializa o objeto Produto em JSON para passar via navegação
                        val produtoJson = Gson().toJson(produto)
                        navController.navigate("Detalhes/$produtoJson")
                    })
                }
                composable("Detalhes/{produtoJson}") { backStackEntry ->
                    // Recupera o objeto Produto passado via navegação
                    val produtoJson = backStackEntry.arguments?.getString("produtoJson")
                    val produto = Gson().fromJson(produtoJson, Produto::class.java)
                    TelaDetalhesProduto(produto = produto, onVoltarClick = {
                        navController.popBackStack()
                    })
                }
            }
        }
    }
}

@Composable
fun TelaCadastroProduto(onCadastrarProduto: (Produto) -> Unit) {
    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf("") }

    // Usamos Box para permitir alinhamento dentro de uma área completa
    Box(
        modifier = Modifier
            .fillMaxSize() // Preenche o tamanho total da tela
            .padding(16.dp)
    ) {
        // Column centralizada vertical e horizontalmente
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Alinha os itens da coluna no centro horizontalmente
        ) {
            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Produto") },
                modifier = Modifier.fillMaxWidth() // Faz com que o campo preencha toda a largura disponível
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espaço entre os campos
            TextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = preco,
                onValueChange = { preco = it },
                label = { Text("Preço") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = quantidade,
                onValueChange = { quantidade = it },
                label = { Text("Quantidade em Estoque") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (nome.isNotBlank() && categoria.isNotBlank() && preco.isNotBlank() && quantidade.isNotBlank()) {
                        val produto = Produto(nome, categoria, preco.toDouble(), quantidade.toInt())
                        onCadastrarProduto(produto)
                    } else {
                        mensagemErro = "Todos os campos são obrigatórios!"
                    }
                },
                modifier = Modifier.fillMaxWidth() // O botão preenche toda a largura disponível
            ) {
                Text("Cadastrar")
            }
            if (mensagemErro.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = mensagemErro, color = Color.Red)
            }
        }

    }
}


