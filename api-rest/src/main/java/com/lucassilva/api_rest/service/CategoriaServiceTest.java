package com.lucassilva.api_rest.service;

import com.lucassilva.api_rest.dto.request.AtualizacaoCategoriaDTO;
import com.lucassilva.api_rest.dto.response.CategoriaResponseDTO;
import com.lucassilva.api_rest.model.Categoria;
import com.lucassilva.api_rest.repository.CategoriaRepository;
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
public class CategoriaServiceTest {

    @Mock
    CategoriaRepository categoriaRepository;
    ProdutoRepository produtoRepository;

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
}
