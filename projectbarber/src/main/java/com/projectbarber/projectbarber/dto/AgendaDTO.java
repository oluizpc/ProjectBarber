package com.projectbarber.projectbarber.dto;

public class AgendaDTO {
    private Integer id;
    private String dataHora;
    private String status;
    private Integer clienteId;
    private Integer barbeiroId;
    private Integer servicoId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataHora() {
        return dataHora;
    }
    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getClienteId() {
        return clienteId;
    }
    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getBarbeiroId() {
        return barbeiroId;
    }
    public void setBarbeiroId(Integer barbeiroId) {
        this.barbeiroId = barbeiroId;
    }

    public Integer getServicoId() {
        return servicoId;
    }
    public void setServicoId(Integer servicoId) {
        this.servicoId = servicoId;
    }
}
