package com.example.provapratica01

class Estoque {
    companion object {
        val listaProdutos = mutableListOf<Produto>()
    }

    fun adicionarProduto(produto: Produto) {
        listaProdutos.add(produto)
    }
}