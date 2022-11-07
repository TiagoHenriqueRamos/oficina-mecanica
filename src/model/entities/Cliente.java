package model.entities;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("unused")
public class Cliente extends Veiculo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nome;
	private String cpf;
	private String endereco;
	private String telefone;
	private Integer id;
	private Veiculo veiculo;

	public Cliente() {
	}

	public Cliente(String nome, String cpf, String endereco, String telefone, Integer id, Veiculo veiculo) {

		this.nome = nome;
		this.cpf = cpf;
		this.endereco = endereco;
		this.telefone = telefone;
		this.id = id;
		this.veiculo = veiculo;
		this.placa = veiculo.getPlaca();
		this.marca = veiculo.getMarca();
		this.modelo = veiculo.getModelo();
		this.id = veiculo.getId();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	@Override
	public String toString() {
		return nome;
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
		Cliente other = (Cliente) obj;
		return Objects.equals(id, other.id);
	}
	

}
