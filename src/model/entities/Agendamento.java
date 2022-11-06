package model.entities;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Agendamento {

	private Integer id;
	private Cliente cliente;
	private Date data;
	private LocalTime horario;
	private String observacao;
	private Relatorio relatorio;
	private Veiculo veiculo;
	private String pagamento;

	List<String> opcoesPagamento = Arrays.asList("DINHEIRO", "DEBITO", "CREDITO", "PIX");

	public Agendamento() {
	}

	public Agendamento(Integer id, Cliente cliente, Date data, LocalTime horario, String observacao,
			Relatorio relatorio, Veiculo veiculo, String pagamento) {

		this.id = id;
		this.cliente = cliente;
		this.data = data;
		this.horario = horario;
		this.observacao = observacao;
		this.relatorio = relatorio;
		this.veiculo = veiculo;
		this.pagamento = pagamento;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String obervacao) {
		this.observacao = obervacao;
	}

	public Relatorio getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(Relatorio relatorio) {
		this.relatorio = relatorio;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public String getPagamento() {
		return pagamento;
	}

	public void setPagamento(String pagamento) {
		this.pagamento = pagamento;
	}

	public List<String> getOpcoesPagamento() {
		return opcoesPagamento;
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
		Agendamento other = (Agendamento) obj;
		return Objects.equals(id, other.id);
	}

}
