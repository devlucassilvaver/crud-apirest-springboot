package com.lucassilva.api_rest.service;


import com.lucassilva.api_rest.model.Produto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProdutoService {

    private List<Produto> produtos = new ArrayList<>();
}
