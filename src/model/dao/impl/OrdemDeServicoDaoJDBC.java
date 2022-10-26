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
import model.dao.OrdemDeServicoDao;
import model.entities.Cliente;
import model.entities.OrdemDeServico;
import model.entities.Veiculo;

public class OrdemDeServicoDaoJDBC implements OrdemDeServicoDao {

	private Connection conn;

	public OrdemDeServicoDaoJDBC(Connection conn) {
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

	private OrdemDeServico instantiateOrdemDeServico(ResultSet rs, Veiculo vei, Cliente cliente) throws SQLException {
		OrdemDeServico ordemDeServico = new OrdemDeServico();
		ordemDeServico.setId(rs.getInt("id"));
		ordemDeServico.setHorario(rs.getTime("ordem_de_servico.horario").toLocalTime());
		ordemDeServico.setData(new java.util.Date(rs.getDate("ordem_de_servico.data").getTime()));
		ordemDeServico.setObservacao(rs.getString("observacao"));
		ordemDeServico.setCliente(cliente);
		ordemDeServico.setVeiculo(vei);
		return ordemDeServico;
	}

	@Override
	public List<OrdemDeServico> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT ordem_de_servico.id, ordem_de_servico.data, ordem_de_servico.observacao, ordem_de_servico.horario, cliente.nome, veiculo.placa "
							+ "FROM ordem_de_servico " + "JOIN cliente  ON ordem_de_servico.clienteId = cliente.id "
							+ "JOIN veiculo ON ordem_de_servico.veiculoId = veiculo.id ");
			rs = st.executeQuery();

			List<OrdemDeServico> list = new ArrayList<>();
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
				OrdemDeServico ordemDeServico = instantiateOrdemDeServico(rs, vei, cliente);
				list.add(ordemDeServico);
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
	public void insert(OrdemDeServico obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO ordemdeservico " + "(id, descricao, valor, clienteId)" + "VALUES" + "(?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, obj.getId());
			st.setString(2, obj.getObservacao());
			st.setDouble(3, obj.getValor());
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
				throw new DbException("Erro inesperado! sem linhas aetadas!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(OrdemDeServico obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE ordemdeservico " + "SET id = ?, descricao = ?, valor = ?, clienteId = ?" + "WHERE id = ?");
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
			st = conn.prepareStatement("DELETE FROM ordemdeservico WHERE id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

}
