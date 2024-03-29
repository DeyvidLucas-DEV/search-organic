package com.vemser.dbc.searchorganic.dto.empresa;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateEmpresaDTO {
    @NotBlank
    @Schema(description = "Nome da empresa", required = true, example = "Fazendo do Wlad")
    private String nomeFantasia;

    @CNPJ
    @Schema(description = "Cnpj da empresa", required = true, example = "50.271.776/0001-14")
    private String cnpj;

    @NotNull
    @Schema(description = "Razão Social", required = true, example = "Produzir legumes para sanar a nessecidade dos que precisam")
    private String razaoSocial;

    @NotNull
    @Schema(description = "Inscrição Social", required = true, example = "inscriçao social")
    private String inscricaoEstadual;

    @NotBlank
    @Schema(description = "setor da empresa", required = true, example = "Legumes")
    private String setor;

}
