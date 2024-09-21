package com.example.provapratica01

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TelaDetalhesProduto(produto: Produto, onVoltarClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Nome: ${produto.nome}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Categoria: ${produto.categoria}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Preço: R$ ${produto.preco}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Quantidade em estoque: ${produto.quantidade} unidades")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onVoltarClick, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}
