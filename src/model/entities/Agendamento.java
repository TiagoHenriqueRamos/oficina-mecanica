package model.entities;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Agendamento {

	private Integer id;
	private Cliente cliente;
	private Date data;
	private LocalTime horario;
	private String observacao;
	private OrdemDeServico ordemDeServico;
	private Veiculo veiculo;
	private String pagamento;
	private Double valor;

	List<String> opcoesPagamento = Arrays.asList("DINHEIRO", "DEBITO", "CREDITO", "PIX");

	public Agendamento() {
	}

	public Agendamento(Integer id, Cliente cliente, Date data, LocalTime horario, String observacao,
			OrdemDeServico ordemDeServico, Veiculo veiculo, String pagamento, Double valor) {
		this.id = id;
		this.cliente = cliente;
		this.data = data;
		this.horario = horario;
		this.observacao = observacao;
		this.ordemDeServico = ordemDeServico;
		this.veiculo = veiculo;
		this.pagamento = pagamento;
		this.valor = valor;
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

	public OrdemDeServico getOrdemDeServico() {
		return ordemDeServico;
	}

	public void setOrdemDeServico(OrdemDeServico ordemDeServico) {
		this.ordemDeServico = ordemDeServico;
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
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	

}
