package com.lucassilva.api_rest.service;


import com.lucassilva.api_rest.dto.request.AtualizacaoProdutoDTO;
import com.lucassilva.api_rest.dto.request.CadastroProdutoDTO;
import com.lucassilva.api_rest.exception.ProdutoNaoEncontradoException;
import com.lucassilva.api_rest.model.Produto;
import com.lucassilva.api_rest.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
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

    public void adicionarProduto(CadastroProdutoDTO cadastroProdutoDTO){
        Produto produto = new Produto(cadastroProdutoDTO);
        produtoRepository.save(produto);
    }

    public void atualizarProduto(int id, AtualizacaoProdutoDTO atualizacaoProdutoDTO){
        Produto produtoEncontrado = buscarProdutoPorId(id);

        produtoEncontrado.setNome(atualizacaoProdutoDTO.getNome());
        produtoEncontrado.setPreco(atualizacaoProdutoDTO.getPreco());

        produtoRepository.save(produtoEncontrado);
    }

    public void removerProduto(int id){
        buscarProdutoPorId(id);
        produtoRepository.deleteById(id);
    }
}
