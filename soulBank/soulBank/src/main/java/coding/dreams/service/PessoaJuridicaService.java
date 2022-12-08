package coding.dreams.service;

import coding.dreams.repository.PessoaJuridicaRepository;
import coding.dreams.exceptions.VerificacaoSistemaException;
import coding.dreams.model.ContaBancaria;
import coding.dreams.model.Endereco;
import coding.dreams.model.PessoaJuridica;
import coding.dreams.repository.ContaBancariaRepository;
import coding.dreams.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PessoaJuridicaService {

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContaBancariaService contaBancariaService;

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Autowired
    private ViaCepService viaCepService;

    public Optional<PessoaJuridica> realizarConsultaPJ(String cnpj) {
        return pessoaJuridicaRepository.findById(cnpj);
    }

    public PessoaJuridica cadastrarPJ(PessoaJuridica pessoaJuridica) {

        //Endereco endereco = enderecoRepository.save(pessoaJuridica.getEndereco());
        //pessoaJuridica.setEndereco(endereco);

        Long IdEndereco = pessoaJuridica.getEndereco().getIdEndereco();
        String cep = pessoaJuridica.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(IdEndereco).orElseGet(() -> {

            //Caso não exista, integrar com o ViaCep e persistir o retorno.

            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        pessoaJuridica.setEndereco(endereco);//*
        // Inserir Cliente, vinculando o Endereco (novo ou existente).
        pessoaJuridicaRepository.save(pessoaJuridica);

        ContaBancaria conta= contaBancariaService.cadastrarConta(pessoaJuridica.getContaBancaria());//cofirmar como fazer associação com a camis
        int tipoChavePix =  conta.getTipoChavePix();//pega um int que difere qual tipo de chave pix será cadastrada

        switch (tipoChavePix) {
            case 1: //chave aleatória
                final String chaveAleatoria = UUID.randomUUID().toString().replace("-", "");
                conta.setChavePix(chaveAleatoria);
                break;

            case 2: //CPF / CNPJ
                String chaveCpf = pessoaJuridica.getCnpj();
                conta.setChavePix(chaveCpf);
                break;

            case 3: //telefone
                String chaveTelefone = pessoaJuridica.getTelefone();
                conta.setChavePix(chaveTelefone);
                break;

            case 4: //email
                String chaveEmail = pessoaJuridica.getEmail();
                conta.setChavePix(chaveEmail);
                break;

            default:
                conta.setChavePix(null);
                break;
        }
        pessoaJuridica.setContaBancaria(conta);

        pessoaJuridica = pessoaJuridicaRepository.save(pessoaJuridica);

        return pessoaJuridica;
    }

    public PessoaJuridica realizarAlteracaoPJ(PessoaJuridica pessoaJuridica) throws VerificacaoSistemaException{

        Endereco endereco = enderecoRepository.save(pessoaJuridica.getEndereco());
        pessoaJuridica.setEndereco(endereco);

        ContaBancaria conta= contaBancariaService.realizarAlteracaoConta(pessoaJuridica.getContaBancaria());
       //para a realização do soft delete será alterado o status da conta de ativa para inativa.
       //ao inativar cliente, automaticamente inativa a conta
        if (pessoaJuridica.getStatusCliente() == false){
            //verificar se o saldo está zerado antes
            double saldo = conta.getSaldo();

            if(saldo==0){
                //verificar se o saldo está zerado antes, se não ele não deixa cancelar
                conta.setStatusConta(false);
            }else{
                conta.setStatusConta(true);
                pessoaJuridica.setStatusCliente(true);

                throw new VerificacaoSistemaException("Não é possível cancelar a conta, pois ainda tem saldo.");
            }
        }

        int tipoChavePix =  conta.getTipoChavePix();//pega um int que difere qual tipo de chave pix será cadastrada

        switch (tipoChavePix) {
            case 1: //chave aleatória
                final String chaveAleatoria = UUID.randomUUID().toString().replace("-", "");
                conta.setChavePix(chaveAleatoria);
                break;

            case 2: //CPF / CNPJ
                String chaveCpf = pessoaJuridica.getCnpj();
                conta.setChavePix(chaveCpf);
                break;

            case 3: //telefone
                String chaveTelefone = pessoaJuridica.getTelefone();
                conta.setChavePix(chaveTelefone);
                break;

            case 4: //email
                String chaveEmail = pessoaJuridica.getEmail();
                conta.setChavePix(chaveEmail);
                break;

            default:
                conta.setChavePix(null);
                break;
        }

        pessoaJuridica.setContaBancaria(conta);

        return pessoaJuridicaRepository.save(pessoaJuridica);
    }
}
