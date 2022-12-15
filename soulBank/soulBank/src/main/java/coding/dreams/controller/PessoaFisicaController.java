package coding.dreams.controller;


import coding.dreams.dto.PessoaFisicaDto;
import coding.dreams.exceptions.VerificacaoSistemaException;
import coding.dreams.model.PessoaFisica;
import coding.dreams.service.PessoaFisicaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@AllArgsConstructor
@RestController
@RequestMapping("/pessoafisica") //apareceu um problema de ambiguidade e resolvemos colocando essa linha de código
public class PessoaFisicaController {

    @Autowired
    private PessoaFisicaService pessoaFisicaService;



    @GetMapping("/consultapf/{cpf}") // Decidimos fazer o cliente sem ID automático e declaramos o CPF como ID
    public ResponseEntity<?> realizarConsultaPF(@PathVariable String cpf){

        Optional<PessoaFisica> opcao = pessoaFisicaService.realizarConsultaPF(cpf);

        if(opcao.isPresent()){
            return ResponseEntity.ok(opcao.get());
        }
        return new ResponseEntity<>("Cliente não encontrado.",HttpStatus.NOT_FOUND);
    }
    
    @PostMapping //Cadastra e altera endereço e conta bancária junto
    public ResponseEntity<PessoaFisica> cadastrarPF(@RequestBody PessoaFisicaDto pessoaFisicaDto){
        return ResponseEntity.ok(pessoaFisicaService.cadastrarPF(PessoaFisicaDto));
    }
    @PutMapping
    public ResponseEntity realizarAlteracaoPF(@RequestBody PessoaFisicaDto pessoaFisicaDto){

        try{
            return ResponseEntity.ok(pessoaFisicaService.realizarAlteracaoPF(pessoaFisicaDto));
        } catch(VerificacaoSistemaException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}

