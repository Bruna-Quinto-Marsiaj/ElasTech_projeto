package coding.dreams.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor


public class PessoaFisica extends Cliente{

    @Column(unique=true)
    @Id
    private String cpf;
    @Column(unique=true)
    private String rg;
    private String nome;
    @ManyToOne @JoinColumn(name="idEndereco")
    private Endereco endereco; //preferimos fazer em classe separada para ficar melhor estruturado
    @OneToOne
    private ContaBancaria contaBancaria;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PessoaFisica pessoaFisica = (PessoaFisica) o;
        return Objects.equals(cpf, pessoaFisica.cpf);
    }
    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }


    public PessoaFisica(){
        //construtor vazio pra teste, ao criar objeto utilizar set
    }
    /*public PessoaFisica(String email, String telefone, boolean statusCliente,  String cpf, String rg, String nome, Endereco endereco,ContaBancaria contaBancaria) {
        super(email,telefone, statusCliente);
        this.cpf = cpf;
        this.rg = rg;
        this.nome = nome;
        this.endereco= endereco;
        this.contaBancaria=contaBancaria;
    }

    public String getCpf() {return cpf;}

    public void setCpf(String cpf) {this.cpf = cpf;}

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }*/

    public String consultarCliente(){
        return super.email+"/"+super.telefone+"/" + super.statusCliente + "/" +this.cpf+"/"+this.rg+"/"+this.nome+"/"+this.endereco.toString()+"/"+this.contaBancaria.toString();
    }

}
