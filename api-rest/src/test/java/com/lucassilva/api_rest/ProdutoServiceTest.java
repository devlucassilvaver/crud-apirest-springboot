package com.lucassilva.api_rest;

import com.lucassilva.api_rest.dto.request.CadastroProdutoDTO;
import com.lucassilva.api_rest.exception.CategoriaNaoEncontradaException;
import com.lucassilva.api_rest.exception.ProdutoNaoEncontradoException;
import com.lucassilva.api_rest.model.Categoria;
import com.lucassilva.api_rest.model.Produto;
import com.lucassilva.api_rest.repository.CategoriaRepository;
import com.lucassilva.api_rest.repository.ProdutoRepository;
import com.lucassilva.api_rest.service.ProdutoService;
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

        var produto = new CadastroProdutoDTO();
        produto.setNome("Headset");
        produto.setPreco(200);
        produto.setCategoriaId(id);

        Categoria categoriaTeste = new Categoria();
        categoriaTeste.setNome("Periféricos");

        Categoria categoriaTesteFail = new Categoria();
        categoriaTesteFail.setNome(null);

        Mockito.when(categoriaRepository.findById(id))
                .thenReturn(Optional.of(categoriaTeste));

        Mockito.when(produtoRepository.save(Mockito.any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = produtoService.adicionarProduto(produto);

        Assertions.assertEquals(
                produto.getNome(),
                resultado.getNome()
        );

        Assertions.assertEquals(
                produto.getPreco(),
                resultado.getPreco()
        );

        Assertions.assertEquals(
                categoriaTeste.getId(),
                resultado.getCategoria().getId()
        );

        Assertions.assertEquals(
                categoriaTeste.getNome(),
                resultado.getCategoria().getNome()
        );
    }

    @Test
    void testarAdicionarProdutoFail(){
        int id = 1;

        var produto = new CadastroProdutoDTO();
        produto.setCategoriaId(id);

        Mockito.when(categoriaRepository.findById(id))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                CategoriaNaoEncontradaException.class,
                () -> produtoService.adicionarProduto(produto)
        );
    }
}
