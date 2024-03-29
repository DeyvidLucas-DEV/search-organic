package com.vemser.dbc.searchorganic.dto.empresa;

import com.vemser.dbc.searchorganic.model.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    @Schema(description = "Id da empresa", required = true)
    private Integer idEmpresa;

    @Schema(description = "Id do usuário dono da empresa", required = true)
    private Integer idUsuario;

    @Schema(description = "Nome da empresa", required = true, example = "Fazendo do Wlad")
    private String nomeFantasia;


    @Schema(description = "Cnpj da empresa", required = true, example = "50.271.776/0001-14")
    private String cnpj;


    @Schema(description = "Razão Social", required = true, example = "Produzir legumes para sanar a nessecidade dos que precisam")
    private String razaoSocial;


    @Schema(description = "Inscrição Social", required = true, example = "inscriçao social")
    private String inscricaoEstadual;


    @Schema(description = "setor da empresa", required = true, example = "Legumes")
    private String setor;

    @Schema(description = "Produtos", required = true, example = "Produtos")
    private ArrayList<Produto> produtos = new ArrayList<>();

}