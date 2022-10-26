package model.entities;

import java.io.Serializable;

public class Veiculo implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String marca;
	protected String modelo;
	protected String placa;
	private Integer id;
	private Cliente cliente;

	public Veiculo() {
	}

	public Veiculo(String marca, String modelo, String placa, Integer id, Cliente cliente) {
		this.marca = marca;
		this.modelo = modelo;
		this.placa = placa;
		this.id = id;
		this.cliente = cliente;
			}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id= id;
	}

	
	@Override
	public String toString() {
		return  placa ;
	}

	

	

}
