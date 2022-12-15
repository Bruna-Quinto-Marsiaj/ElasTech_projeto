package coding.dreams.service;

import coding.dreams.dto.PessoaFisicaDto;
import coding.dreams.model.ContaBancaria;
import coding.dreams.model.PessoaFisica;
import coding.dreams.repository.PessoaFisicaRepository;
import coding.dreams.exceptions.VerificacaoSistemaException;
import coding.dreams.model.Endereco;
import coding.dreams.repository.ContaBancariaRepository;
import coding.dreams.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PessoaFisicaService {
    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContaBancariaService contaBancariaService;

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    public Optional<PessoaFisicaDto> realizarConsultaPF(String cpf) {
        return pessoaFisicaRepository.findById(cpf);
    }

    public PessoaFisicaDto cadastrarPF(PessoaFisicaDto pessoaFisicaDto) {//vamos realizar o cadastro do banco juntamente com o cadastro da pessoa, por isso criamos o metodo cadastrarConta e chamamos conta em pessoa
        
        Endereco endereco = enderecoRepository.save(pessoaFisicaDto.getEndereco());
        pessoaFisicaDto.setEndereco(endereco);

        ContaBancaria conta= contaBancariaService.cadastrarConta(pessoaFisicaDto.getContaBancaria());
        int tipoChavePix =  conta.getTipoChavePix();//pega um int que difere qual tipo de chave pix será cadastrada
        
        switch (tipoChavePix) {
            case 1: //chave aleatória
                final String chaveAleatoria = UUID.randomUUID().toString().replace("-", "");
                conta.setChavePix(chaveAleatoria);
            break;

            case 2: //CPF / CNPJ
                String chaveCpf = pessoaFisicaDto.getCpf();
                conta.setChavePix(chaveCpf);
            break;

            case 3: //telefone
                String chaveTelefone = pessoaFisicaDto.getTelefone();
                conta.setChavePix(chaveTelefone);
            break;

            case 4: //email
                String chaveEmail = pessoaFisicaDto.getEmail();
                conta.setChavePix(chaveEmail);
            break;

            default:
                conta.setChavePix(null);
            break;
        }

        pessoaFisicaDto.setContaBancaria(conta);
        pessoaFisicaDto = pessoaFisicaRepository.save(pessoaFisicaDto);//ao cadastrar, pegar a alternativa do cliente de qual dado usar (cpf/cnpj, telefone, email), e cadastrar no banco de dados esse dado
                         
        return pessoaFisicaDto;
    }

    public PessoaFisicaDto realizarAlteracaoPF(PessoaFisicaDto pessoaFisicaDto) throws VerificacaoSistemaException {
        Endereco endereco = enderecoRepository.save(pessoaFisicaDto.getEndereco());
        pessoaFisicaDto.setEndereco(endereco);


        ContaBancaria conta= contaBancariaService.realizarAlteracaoConta(pessoaFisicaDto.getContaBancaria());
        //para a realização do soft delete será alterado o status da conta de ativa para inativa
        //ao inativar cliente, automaticamente inativa a conta
        if (pessoaFisicaDto.getStatusCliente() == false){
            double saldo = conta.getSaldo();

            if(saldo==0){
                //verificar se o saldo está zerado antes, se não ele não deixa cancelar
                conta.setStatusConta(false);
            }else{
                conta.setStatusConta(true);
                pessoaFisicaDto.setStatusCliente(true);

                throw new VerificacaoSistemaException("Não é possível cancelar a conta, pois ainda tem saldo.");
            }
        }

        //verificação de chave
        int tipoChavePix =  conta.getTipoChavePix();//pega um int que difere qual tipo de chave pix será cadastrada

        switch (tipoChavePix) {
            case 1: //chave aleatória
                final String chaveAleatoria = UUID.randomUUID().toString().replace("-", "");
                conta.setChavePix(chaveAleatoria);
                break;

            case 2: //CPF / CNPJ
                String chaveCpf = pessoaFisicaDto.getCpf();
                conta.setChavePix(chaveCpf);
                break;

            case 3: //telefone
                String chaveTelefone = pessoaFisicaDto.getTelefone();
                conta.setChavePix(chaveTelefone);
                break;

            case 4: //email
                String chaveEmail = pessoaFisicaDto.getEmail();
                conta.setChavePix(chaveEmail);
                break;

            default:
                conta.setChavePix(null);
                break;
        }

        pessoaFisicaDto.setContaBancaria(conta);
        return pessoaFisicaRepository.save(pessoaFisicaDto);
    }
}
