package com.lucassilva.api_rest.service;


import com.lucassilva.api_rest.dto.request.AtualizacaoProdutoDTO;
import com.lucassilva.api_rest.dto.request.CadastroProdutoDTO;
import com.lucassilva.api_rest.dto.response.CategoriaResumoResponseDTO;
import com.lucassilva.api_rest.dto.response.ProdutoResponseDTO;
import com.lucassilva.api_rest.exception.CategoriaNaoEncontradaException;
import com.lucassilva.api_rest.exception.ProdutoNaoEncontradoException;
import com.lucassilva.api_rest.model.Categoria;
import com.lucassilva.api_rest.model.Produto;
import com.lucassilva.api_rest.repository.CategoriaRepository;
import com.lucassilva.api_rest.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    public ProdutoService(
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository){
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }

    public Produto buscarProdutoPorId(int id){
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(
                        "Produto não encontrado"
                ));
    }

    public ProdutoResponseDTO buscarProdutoResponsePorId(int id){
        Produto produto = buscarProdutoPorId(id);

        CategoriaResumoResponseDTO categoria = new CategoriaResumoResponseDTO(
                produto.getCategoria().getId(),
                produto.getCategoria().getNome()
        );

        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                categoria
        );
    }

    public void adicionarProduto(CadastroProdutoDTO cadastroProdutoDTO){
        Produto produto = new Produto(cadastroProdutoDTO);
        produtoRepository.save(produto);
    }

    public void atualizarProduto(int id, AtualizacaoProdutoDTO atualizacaoProdutoDTO){
        Produto produtoEncontrado = buscarProdutoPorId(id);

        produtoEncontrado.setNome(atualizacaoProdutoDTO.getNome());
        produtoEncontrado.setPreco(atualizacaoProdutoDTO.getPreco());

        Categoria categoria = categoriaRepository
                .findById(atualizacaoProdutoDTO.getCategoriaId())
                .orElseThrow(() -> new CategoriaNaoEncontradaException(
                        "Categoria não encontrada"
                ));
        produtoEncontrado.setCategoria(categoria);

        produtoRepository.save(produtoEncontrado);

    }

    public void removerProduto(int id){
        buscarProdutoPorId(id);
        produtoRepository.deleteById(id);
    }

}
