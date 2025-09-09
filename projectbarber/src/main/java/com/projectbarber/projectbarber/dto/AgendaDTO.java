package com.projectbarber.projectbarber.dto;

public class AgendaDTO {
    private long id;
    private String dataHora;
    private String status;
    private long clienteId;
    private long barbeiroId;
    private long servicoId;

    public long getId() {
        return id;
    }
    public void setId(long id) {
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

    public long getClienteId() {
        return clienteId;
    }
    public void setClienteId(long clienteId) {
        this.clienteId = clienteId;
    }

    public long getBarbeiroId() {
        return barbeiroId;
    }
    public void setBarbeiroId(long barbeiroId) {
        this.barbeiroId = barbeiroId;
    }

    public long getServicoId() {
        return servicoId;
    }
    public void setServicoId(long servicoId) {
        this.servicoId = servicoId;
    }
}
