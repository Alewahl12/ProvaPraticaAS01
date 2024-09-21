package com.example.provapratica01

object Estoque {
    private val produtos = mutableListOf<Produto>()

    val listaProdutos: List<Produto> get() = produtos

    fun adicionarProduto(produto: Produto) {
        produtos.add(produto)
    }

    fun calcularValorTotalEstoque(): Double {
        return produtos.sumOf { it.preco * it.quantidade }
    }

    fun calcularQuantidadeTotalProdutos(): Int {
        return produtos.sumOf { it.quantidade }
    }

    fun encontrarProduto(nome: String): Produto? {
        return produtos.find { it.nome == nome }
    }

}