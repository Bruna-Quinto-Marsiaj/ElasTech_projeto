package coding.dreams.dto;

import coding.dreams.model.Cliente;
import coding.dreams.model.ContaBancaria;
import coding.dreams.model.Endereco;
import coding.dreams.model.PessoaFisica;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor//cria construtor vazio

public class PessoaFisicaDto {


    private String cpf;
    private String rg;//somente os dados que queremos retornar, tirei a senha
    private String nome;
    private Endereco endereco;
    private ContaBancaria contaBancaria;
    private String email;
    private String telefone;
    private boolean statusCliente;

    /*public PessoaFisicaDto(PessoaFisica pessoaFisica) {
        this.rg = pessoaFisica.getRg();
        this.nome = pessoaFisica.getNome();
        this.endereco = pessoaFisica.getEndereco();
        this.contaBancaria = pessoaFisica.getContaBancaria();*/
    }
    /*public PessoaFisicaDto(Cliente pessoaFisica) {
        super.email = Cliente.getEmail();
        super.telefone = Cliente.getTelefone();
        super.statusCliente = Cliente.getStatusCliente();*/




