package model.entities;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Relatorio {

    private Integer id;
    private String observacao;
    private Double valor;
    private Cliente cliente;
    private Veiculo veiculo;
    private Date data;
    private LocalTime horario;
    private String pagamento;

    List<String> opcoesPagamento = Arrays.asList("DINHEIRO", "DEBITO", "CREDITO", "PIX");

    public Relatorio() {
    }

    public Relatorio(Integer id, String observacao, Double valor, Cliente cliente, Veiculo veiculo, Date data,
            LocalTime horario, String pagamento) {

        this.id = id;
        this.observacao = observacao;
        this.valor = valor;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.data = data;
        this.horario = horario;
        this.pagamento = pagamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public List<String> getOpcoesPagamento() {
        return opcoesPagamento;
    }

    public void setOpcoesPagamento(List<String> opcoesPagamento) {
        this.opcoesPagamento = opcoesPagamento;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Relatorio other = (Relatorio) obj;
        return Objects.equals(id, other.id);
    }

}
