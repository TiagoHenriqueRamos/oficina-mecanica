package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.VeiculoDao;
import model.entities.Cliente;
import model.entities.Veiculo;

public class VeiculoDaoJDBC implements VeiculoDao {

	private Connection conn;

	public VeiculoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Veiculo obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO oficina.veiculo " + "(id,  placa, modelo, marca, ClienteId) " + "VALUES " + "( DEFAULT,  ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getPlaca());
			st.setString(2, obj.getMarca());
			st.setString(3, obj.getModelo());
			st.setInt(4, obj.getCliente().getId());
			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Erro inesperado! sem linhas alteradas!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Veiculo obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE oficina.veiculo " 
		+ "SET Placa = ?, Modelo = ?, Marca = ?, ClienteId = ? "
				+"WHERE Id = ?"	);

			st.setString(1, obj.getPlaca());
			st.setString(2, obj.getModelo());
			st.setString(3, obj.getMarca());
			st.setInt(4, obj.getCliente().getId());
			st.setInt(5, obj.getId());
			
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteByPlaca(String placa) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM veiculo WHERE placa = ?");
			st.setString(1, placa);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Veiculo findByPlaca(String placa) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM veiculo WHERE palca = ?");
			st.setString(1, placa);
			rs = st.executeQuery();
			if(rs.next()) {
				Veiculo obj = new Veiculo();
				obj.setPlaca(rs.getString("Placa"));
				obj.setModelo(rs.getString("Modelo"));
				obj.setMarca(rs.getString("Marca"));
				return obj;
			}
			return null;
		}catch(SQLException e ) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
		
	}
	private Cliente instantiateCliente(ResultSet rs) throws SQLException {
		Cliente obj = new Cliente();
		obj.setId(rs.getInt("ClienteId"));
		obj.setNome(rs.getString("CliNome"));
		

		return obj;
	}

	private Veiculo instantiateVeiculo(ResultSet rs, Cliente cliente) throws SQLException {
		Veiculo obj = new Veiculo();
		obj.setId(rs.getInt("veiculo.Id"));
		obj.setPlaca(rs.getString("veiculo.Placa"));
		obj.setMarca(rs.getString("veiculo.Marca"));
		obj.setModelo(rs.getString("veiculo.Modelo"));
		obj.setCliente(cliente);	

		return obj;
	}

	@Override
	public List<Veiculo> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(" SELECT veiculo.*, cliente.nome as CliNome " 
					+ "FROM veiculo INNER JOIN cliente "
					+ "ON veiculo.ClienteId = cliente.Id " 
					+ "ORDER BY Nome" );
			rs = st.executeQuery();

			List<Veiculo> list = new ArrayList<>();
			Map<Integer, Cliente> map = new HashMap<>();
			
			
			while (rs.next()) {
				
				Cliente clie = map.get(rs.getInt("ClienteId"));
				if(clie == null) {
					clie = instantiateCliente(rs);
					map.put(rs.getInt("ClienteId"), clie);
				}
				
				Veiculo obj = instantiateVeiculo(rs,clie);
				
	
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

}
