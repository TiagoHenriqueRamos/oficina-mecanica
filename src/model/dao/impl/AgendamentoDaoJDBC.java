package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.AgendamentoDao;
import model.entities.Agendamento;
import model.entities.Cliente;
import model.entities.Veiculo;

public class AgendamentoDaoJDBC implements AgendamentoDao {

	private Connection conn;

	public AgendamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Agendamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO oficina.agendamento "
					+ "(data, horario, observacao, clienteId, veiculoId  ) " + "VALUES " + "( ?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getData().getTime()));
			st.setTime(2, Time.valueOf(obj.getHorario()));
			st.setString(3, obj.getObservacao());
			st.setInt(4, obj.getCliente().getId());
			st.setInt(5, obj.getVeiculo().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Agendamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE oficina.agendamento "
					+ "SET ClienteId = ?, VeiculoId = ?, Data = ?, Horario = ?, observacao = ? " + "WHERE id = ?");

			st.setInt(1, obj.getCliente().getId());
			st.setInt(2, obj.getVeiculo().getId());
			st.setDate(3, new java.sql.Date(obj.getData().getTime()));
			st.setTime(4, Time.valueOf(obj.getHorario()));
			st.setString(5, obj.getObservacao());
			st.setInt(6, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM oficina.agendamento WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	private Cliente instantiateCliente(ResultSet rs) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.setNome(rs.getString("cliente.nome"));
		return cliente;

	}

	private Veiculo instantiateVeiculo(ResultSet rs) throws SQLException {
		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca(rs.getString("veiculo.placa"));
		return veiculo;
	}

	private Agendamento instantiateAgendamento(ResultSet rs, Veiculo vei, Cliente cliente) throws SQLException {
		Agendamento agendamento = new Agendamento();
		agendamento.setId(rs.getInt("Id"));
		agendamento.setHorario(rs.getTime("agendamento.horario").toLocalTime());
		agendamento.setData(new java.util.Date(rs.getDate("agendamento.data").getTime()));
		agendamento.setObservacao(rs.getString("observacao"));
		agendamento.setVeiculo(vei);
		agendamento.setCliente(cliente);
		return agendamento;
	}

	@Override
	public List<Agendamento> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT agendamento.id, agendamento.data, agendamento.observacao, agendamento.horario, cliente.nome, veiculo.placa "
							+ "FROM agendamento " + "JOIN cliente  ON agendamento.clienteId = cliente.id "
							+ "JOIN veiculo ON agendamento.veiculoId = veiculo.id ");

			rs = st.executeQuery();

			List<Agendamento> list = new ArrayList<>();
			Map<String, Veiculo> mapVeiculo = new HashMap<>();
			Map<String, Cliente> mapCliente = new HashMap<>();

			while (rs.next()) {
				Veiculo vei = mapVeiculo.get(rs.getString("veiculo.placa"));
				if (vei == null) {
					vei = instantiateVeiculo(rs);
					mapVeiculo.put(rs.getString("veiculo.placa"), vei);
				}
				Cliente cliente = mapCliente.get(rs.getString("cliente.nome"));
				if (cliente == null) {
					cliente = instantiateCliente(rs);
					mapCliente.put(rs.getString("cliente.nome"), cliente);
				}
				Agendamento agendamento = instantiateAgendamento(rs, vei, cliente);
				list.add(agendamento);
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
