package br.com.cdb.MeuBancoDigitalCompleto.dto;

public class AlterarStatusDTO {
    private String cpf;
    private boolean status;

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
}