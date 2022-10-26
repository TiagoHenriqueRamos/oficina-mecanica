package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ClienteDao;
import model.entities.Cliente;

public class ClienteDaoJDBC implements ClienteDao {

	private Connection conn;

	public ClienteDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	
	public void insert(Cliente obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO oficina.cliente " + "(id, cpf, nome, endereco, telefone)"
					+ "VALUES " + "(DEFAULT, ?, ?, ?, ? )", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getCPF());
			st.setString(2, obj.getNome());
			st.setString(3, obj.getEndereco());
			st.setString(4, obj.getTelefone());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}

			}

			else {
				throw new DbException("Erro inesperado! Nenhuma linha foi afetada!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
		}
	}

	public void update(Cliente obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE oficina.cliente " + "SET Nome = ?, Cpf = ?, Endereco = ?, Telefone = ? " + "WHERE Id = ? ");

			st.setString(1, obj.getNome());
			st.setString(2, obj.getCPF());
			st.setString(3, obj.getEndereco());
			st.setString(4, obj.getTelefone());
			st.setInt(5, obj.getId());
			st.executeUpdate();
			

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM cliente WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Cliente findByCpf(String cpf) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM cliente WHERE cpf = ?");
			st.setString(1, cpf);
			rs = st.executeQuery();
			if (rs.next()) {
				Cliente obj = new Cliente();
				obj.setNome(rs.getString("Nome"));
				obj.setCpf(rs.getString("Cpf"));
				obj.setEndereco(rs.getString("Endereço"));
				obj.setTelefone(rs.getString("Telefone"));
				obj.setId(rs.getInt("Id"));
				return obj;
			}

			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public List<Cliente> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM oficina.cliente");
			rs = st.executeQuery();

			List<Cliente> list = new ArrayList<>();

			while (rs.next()) {
				Cliente obj = new Cliente();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome"));
				obj.setCpf(rs.getString("Cpf"));
				obj.setEndereco(rs.getString("Endereco"));
				obj.setTelefone(rs.getString("Telefone"));
				
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

//	private Cliente instantiateCliente(ResultSet rs) throws SQLException {
//		Cliente obj = new Cliente();
//		obj.setId(rs.getInt("Id"));
//		obj.setNome(rs.getString("Nome"));
//		obj.setCpf(rs.getString("Cpf"));
//		obj.setEndereco(rs.getString("Endereco"));
//		obj.setTelefone(rs.getString("Telefone"));
//		
//
//		return obj;
//	}
//
//	private Veiculo instantiateVeiculo(ResultSet rs) throws SQLException {
//		Veiculo obj = new Veiculo();
//		obj.setId(rs.getInt("veiculo.Id"));
//		obj.setPlaca(rs.getString("veiculo.Placa"));
//		obj.setMarca(rs.getString("veiculo.Marca"));
//		obj.setModelo(rs.getString("veiculo.Modelo"));
//		obj.setId(rs.getInt("ClienteId"));
//		
//
//		return obj;
//	}

}
