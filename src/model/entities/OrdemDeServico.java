package model.entities;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrdemDeServico {

	private Integer id;
	private String observacao;
	private Double valor;
	private Cliente cliente;
	private Veiculo veiculo;
	private Date data;
	private LocalTime horario;
	
	
	List<String> opcoesPagamento = Arrays.asList("DINHEIRO", "DEBITO", "CREDITO", "PIX");

	public OrdemDeServico() {
	}

	public OrdemDeServico(Integer id, String observacao, Double valor, Cliente cliente, Veiculo veiculo, Agendamento agendamento) {

		this.id = id;
		this.observacao = observacao;
		this.valor = valor;
		this.cliente = cliente;
		this.veiculo = veiculo;
		this.data = agendamento.getData();
		this.horario = agendamento.getHorario();
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
		OrdemDeServico other = (OrdemDeServico) obj;
		return Objects.equals(id, other.id);
	}

}