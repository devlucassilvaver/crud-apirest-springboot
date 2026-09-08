package com.lucassilva.api_rest.service;

import com.lucassilva.api_rest.dto.request.CadastroCategoriaDTO;
import com.lucassilva.api_rest.dto.request.CadastroProdutoDTO;
import com.lucassilva.api_rest.dto.response.ProdutoResponseDTO;
import com.lucassilva.api_rest.exception.ProdutoNaoEncontradoException;
import com.lucassilva.api_rest.model.Categoria;
import com.lucassilva.api_rest.model.Produto;
import com.lucassilva.api_rest.repository.CategoriaRepository;
import com.lucassilva.api_rest.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    CategoriaRepository categoriaRepository;

    @InjectMocks
    ProdutoService produtoService;

    @Test
    void testeBuscarProdutoPorIdFail(){
        int id = 1;
        Mockito.when(produtoRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> produtoService.buscarProdutoPorId(id)
        );
    }

    @Test
    void testeBuscarProdutoPorId(){
        int id = 1;
        Produto produtoTeste = new Produto();
        Mockito.when(produtoRepository.findById(id))
                .thenReturn(Optional.of(produtoTeste));
        Assertions.assertEquals(
                produtoTeste,
                produtoService.buscarProdutoPorId(id)
        );
    }

    @Test
    void testeAdicionarProduto() {
        int id = 1;

        CadastroProdutoDTO produto = new CadastroProdutoDTO();
        produto.setNome("Headset");
        produto.setPreco(270);
        produto.setCategoriaId(id);

        Categoria categoriaTeste = new Categoria();

        Mockito.when(categoriaRepository.findById(id))
                .thenReturn(Optional.of(categoriaTeste));

        Mockito.when(produtoRepository.save(Mockito.any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = produtoService.adicionarProduto(produto);

        Assertions.assertEquals(produto.getNome(), resultado.getNome());
    }
}
