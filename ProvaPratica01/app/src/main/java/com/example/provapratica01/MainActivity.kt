package com.example.provapratica01

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "telaCadastro") {
                composable("telaCadastro") { TelaCadastroProduto(navController) }
                composable("telaLista") {
                    TelaListaProdutos(
                        onDetalhesClick = { produto ->
                            navController.navigate("telaDetalhes/${produto.nome}")
                        },
                        onEstatisticasClick = {
                            navController.navigate("telaEstatisticas")
                        }
                    )
                }
                composable("telaDetalhes/{nome}") { backStackEntry ->
                    val nome = backStackEntry.arguments?.getString("nome") ?: ""
                    val produto = Estoque.encontrarProduto(nome)
                    if (produto != null) {
                        TelaDetalhesProduto(produto, onVoltarClick = { navController.popBackStack() })
                    } else {
                        Text("Produto não encontrado")
                    }
                }
                composable("telaEstatisticas") { TelaEstatisticas() }
            }
        }
    }
}

@Composable
fun TelaCadastroProduto(navController: NavHostController) {
    val context = LocalContext.current
    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color(0xFFF5F5F5))) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Produto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
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
                    val precoDouble = preco.toDoubleOrNull()
                    val quantidadeInt = quantidade.toIntOrNull()

                    when {
                        precoDouble == null || precoDouble < 0 -> Toast.makeText(context, "O preço não pode ser menor que zero!", Toast.LENGTH_SHORT).show()
                        quantidadeInt == null || quantidadeInt < 1 -> Toast.makeText(context, "A quantidade não pode ser menor que 1!", Toast.LENGTH_SHORT).show()
                        nome.isBlank() || categoria.isBlank() -> mensagemErro = "Todos os campos são obrigatórios!"
                        else -> {
                            val produto = Produto(nome, categoria, precoDouble, quantidadeInt)
                            Estoque.adicionarProduto(produto)
                            navController.navigate("telaLista")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape
            ) {
                Text("Cadastrar")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate("telaLista") }, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Estoque")
            }
            if (mensagemErro.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = mensagemErro, color = Color.Red)
            }
        }
    }
}

@Composable
fun TelaListaProdutos(onDetalhesClick: (Produto) -> Unit, onEstatisticasClick: () -> Unit) {
    val produtos = Estoque.listaProdutos

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color(0xFFF5F5F5))) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(produtos) { produto ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White)
                    .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${produto.nome} (${produto.quantidade} unidades)")
                    Button(onClick = { onDetalhesClick(produto) }) {
                        Text("Detalhes")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onEstatisticasClick, modifier = Modifier.fillMaxWidth()) {
            Text("Ver Estatísticas")
        }
    }
}

@Composable
fun TelaDetalhesProduto(produto: Produto, onVoltarClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color(0xFFF5F5F5))) {
        Text(text = "Nome: ${produto.nome}", modifier = Modifier.padding(8.dp))
        Text(text = "Categoria: ${produto.categoria}", modifier = Modifier.padding(8.dp))
        Text(text = "Preço: R$ ${produto.preco}", modifier = Modifier.padding(8.dp))
        Text(text = "Quantidade em estoque: ${produto.quantidade} unidades", modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onVoltarClick, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}

@Composable
fun TelaEstatisticas() {
    val valorTotalEstoque = Estoque.calcularValorTotalEstoque()
    val quantidadeTotalProdutos = Estoque.calcularQuantidadeTotalProdutos()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color(0xFFF5F5F5))) {
        Text(text = "Valor total do estoque: R$ ${"%.2f".format(valorTotalEstoque)}", modifier = Modifier.padding(8.dp))
        Text(text = "Quantidade total de produtos: $quantidadeTotalProdutos unidades", modifier = Modifier.padding(8.dp))
    }
}
