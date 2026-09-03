package com.lucassilva.api_rest.service;

import com.lucassilva.api_rest.exception.ProdutoNaoEncontradoException;
import com.lucassilva.api_rest.model.Produto;
import com.lucassilva.api_rest.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    ProdutoRepository produtoRepository;

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
}
