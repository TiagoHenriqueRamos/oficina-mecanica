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
import model.dao.RelatorioDao;
import model.entities.Cliente;
import model.entities.Relatorio;
import model.entities.Veiculo;

public class RelatorioDaoJDBC implements RelatorioDao {

	private Connection conn;

	public RelatorioDaoJDBC(Connection conn) {
		this.conn = conn;
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

	private Relatorio instantiateRelatorio(ResultSet rs, Veiculo vei, Cliente cliente) throws SQLException {
		Relatorio relatorio = new Relatorio();
		relatorio.setId(rs.getInt("id"));
		relatorio.setHorario(rs.getTime("relatorio.horario").toLocalTime());
		relatorio.setData(new java.util.Date(rs.getDate("relatorio.data").getTime()));
		relatorio.setObservacao(rs.getString("observacao"));
		relatorio.setCliente(cliente);
		relatorio.setVeiculo(vei);
		return relatorio;
	}

	@Override
	public List<Relatorio> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT relatorio.id, cliente.nome, veiculo.placa, relatorio.data, relatorio.horario, relatorio.valor, relatorio.pagamento, relatorio.observacao "
							+ "FROM relatorio JOIN cliente  ON relatorio.clienteId = cliente.id "
							+ "JOIN veiculo ON relatorio.veiculoId = veiculo.id");
			rs = st.executeQuery();

			List<Relatorio> list = new ArrayList<>();
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
				Relatorio relatorio = instantiateRelatorio(rs, vei, cliente);
				list.add(relatorio);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void insert(Relatorio obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO oficina.relatorio "
					+ "( ClienteId, VeiculoId, Valor, Pagamento, Data, Observacao, Horario)" + "VALUES"
					+ "(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

			
			st.setInt(1, obj.getCliente().getId());
			st.setInt(2, obj.getVeiculo().getId());
			st.setDouble(3, obj.getValor());
			st.setString(4, obj.getPagamento());
			st.setDate(5, new java.sql.Date(obj.getData().getTime()));
			st.setString(6, obj.getObservacao());
			st.setTime(7, Time.valueOf(obj.getHorario()));

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);

			} else {
				throw new DbException("Erro inesperado! sem linhas aetadas!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Relatorio obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE relatorio " +  "SET id = ?, descricao = ?, valor = ?, clienteId = ?" + "WHERE id = ?");
			st.setInt(1, obj.getId());
			st.setString(2, obj.getObservacao());
			st.setDouble(3, obj.getValor());
			st.setInt(4, obj.getCliente().getId());
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
			st = conn.prepareStatement("DELETE FROM relatorio WHERE id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

}
