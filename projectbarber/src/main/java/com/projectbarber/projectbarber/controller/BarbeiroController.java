package com.projectbarber.projectbarber.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectbarber.projectbarber.model.Barbeiro;
import com.projectbarber.projectbarber.service.BarbeiroService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {
    
    private final BarbeiroService barbeiroService;

    public BarbeiroController(BarbeiroService barbeiroService) {
        this.barbeiroService = barbeiroService;
    }


    // Criar barbeiros
    @PostMapping    
    public ResponseEntity<Barbeiro> criarBarbeiro(@RequestBody Barbeiro barbeiro) {
        return ResponseEntity.ok(barbeiroService.cadastrarBarbeiro(barbeiro));
    }
    

    //Listar todos barbeiros
    @GetMapping
    public ResponseEntity<List<Barbeiro>> listarTodos() {
        return ResponseEntity.ok(barbeiroService.listarTodos());
    }
    
    //Buscar barbeiro por id
    @GetMapping("/{id}")
    public ResponseEntity<Barbeiro> buscarPorId(@RequestParam Long id) {
        return ResponseEntity.ok(barbeiroService.buscarPorId(id));
    }

    //Atualizar barbeiro
    @PutMapping("/{id}")
    public ResponseEntity<Barbeiro> atualizarBarbeiro(@PathVariable Long id, @RequestBody Barbeiro barbeiro) {
        return ResponseEntity.ok(barbeiroService.atualizarBarbeiro(id, barbeiro));
    }

    //Deletar barbeiro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBarbeiro(@PathVariable Long id) {
        barbeiroService.deletarBarbeiro(id);
        return ResponseEntity.noContent().build();
}
}