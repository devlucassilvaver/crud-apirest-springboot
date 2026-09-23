package com.lucassilva.api_rest.service;

import com.lucassilva.api_rest.dto.request.AtualizacaoCategoriaDTO;
import com.lucassilva.api_rest.exception.CategoriaNaoEncontradaException;
import com.lucassilva.api_rest.model.Categoria;
import com.lucassilva.api_rest.repository.CategoriaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    CategoriaRepository categoriaRepository;

    @InjectMocks
    CategoriaService categoriaService;

    @Test
    void atualizarCategoria(){
        int id = 1;

        Categoria categoria = new Categoria();
        categoria.setNome("Eletrônicos");


        AtualizacaoCategoriaDTO atualizacaoCategoriaDTO = new AtualizacaoCategoriaDTO();
        atualizacaoCategoriaDTO.setNome("Periféricos");

        Mockito.when(categoriaRepository.findById(id))
                .thenReturn(Optional.of(categoria));

        var resultado = categoriaService.atualizarCategoria(id, atualizacaoCategoriaDTO);

        Assertions.assertEquals(
                atualizacaoCategoriaDTO.getNome(),
                resultado.getNome()
        );
    }

    @Test
    void atualizarCategoriaFail(){
        int id = 1;

        AtualizacaoCategoriaDTO atualizacaoCategoriaDTO = new AtualizacaoCategoriaDTO();
        atualizacaoCategoriaDTO.setNome("Periféricos");

        Mockito.when(categoriaRepository.findById(id))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                CategoriaNaoEncontradaException.class,
                () -> categoriaService.atualizarCategoria(id, atualizacaoCategoriaDTO)
        );
    }

    @Test
    void removerCategoria(){
        int id = 1;
        Categoria categoria = new Categoria();
        categoria.setProdutos(List.of());

        Mockito.when(categoriaRepository.findById(id))
                .thenReturn(Optional.of(categoria));

        categoriaService.removerCategoria(id);

        Mockito.verify(categoriaRepository).deleteById(id);

    }
}
