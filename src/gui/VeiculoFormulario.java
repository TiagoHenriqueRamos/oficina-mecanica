package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listener.DataChangeListener;
import gui.utils.Alerts;
import gui.utils.Constraints;
import gui.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Cliente;
import model.entities.Veiculo;
import model.exceptions.ValidationException;
import model.services.ClienteService;
import model.services.VeiculoService;

public class VeiculoFormulario implements Initializable {
	
		
	Veiculo entidade;

	VeiculoService veiculoService;

	ClienteService clienteService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private Label labelErrorPlaca;

	@FXML
	private Label labelErrorMarca;

	@FXML
	private Label labelErrorModelo;

	@FXML
	private Label labelErrorId;

	@FXML
	private Label labelErrorCliente;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtPlaca;

	@FXML
	private TextField txtMarca;

	@FXML
	private TextField txtModelo;
	@FXML
	private ComboBox<Cliente> comboBoxCliente;

	private ObservableList<Cliente> obsList;
	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void setService(VeiculoService veiculoService, ClienteService clienteService) {
		this.veiculoService = veiculoService;
		this.clienteService = clienteService;
	}

	public void setEntidade(Veiculo veiculo) {
		this.entidade = veiculo;
	}

	@FXML
	public void onBtSalvar(ActionEvent event) {

		if (entidade == null) {
			throw new IllegalStateException("Entidade nula!");
		}
		if (veiculoService == null) {
			throw new IllegalStateException("Service nulo!");
		}
		try {

			entidade = getFormData();
			veiculoService.saveOrUpdate(entidade);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();

		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void subscribeDataChengeListener(DataChangeListener listner) {
		dataChangeListeners.add(listner);
	}

	private Veiculo getFormData() {
		Veiculo veiculo = new Veiculo();
		

		ValidationException exception = new ValidationException("Erro de validação!");

		veiculo.setId(Utils.tryParseToInt(txtId.getText()));

		
		if (txtPlaca.getText() == null || txtPlaca.getText().trim().equals("")) {
			exception.addError("Placa", "O campo não pode ser vazio!");
		} else if (txtPlaca.getText() != null && !txtPlaca.getText().matches("[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}")) {
			exception.addError("Placa", "Padrão inválido! Padrão ABC1D23");

		}
		veiculo.setPlaca(txtPlaca.getText());

		if (txtMarca.getText() == null || txtMarca.getText().trim().equals("")) {
			exception.addError("Marca", "O campo não pode ser vazio!");

		}
		veiculo.setMarca((txtMarca.getText()));

		if (txtModelo.getText() == null || txtModelo.getText().trim().equals("")) {
			exception.addError("Modelo", "O campo não pode ser vazio!");

		}

		veiculo.setModelo(txtModelo.getText());

		
		veiculo.setCliente(comboBoxCliente.getValue());
		

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return veiculo;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldPlaca(txtPlaca);
		Constraints.setTextFieldMaxLength(txtMarca, 20);
		Constraints.setTextFieldMaxLength(txtModelo, 20);
		Constraints.setTextFieldMaxLength(txtPlaca, 10);
		initializeComboBoxCliente();

	}

	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula!");
		}

		txtId.setText(String.valueOf(entidade.getId()));
		txtPlaca.setText(entidade.getPlaca());
		txtMarca.setText(entidade.getMarca());
		txtModelo.setText(entidade.getModelo());

		if (entidade.getCliente() == null) {
			comboBoxCliente.getSelectionModel().selectFirst();
		}
		comboBoxCliente.setValue(entidade.getCliente());
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("Placa")) {
			labelErrorPlaca.setText(errors.get("Placa"));

		} else {
			labelErrorPlaca.setText("");
		}

		if (fields.contains("Marca")) {
			labelErrorMarca.setText(errors.get("Marca"));

		} else {
			labelErrorMarca.setText("");
		}

		if (fields.contains("Modelo")) {
			labelErrorModelo.setText(errors.get("Modelo"));

		} else {
			labelErrorModelo.setText("");
		}
		if (fields.contains("Cliente")) {
			labelErrorCliente.setText(errors.get("Cliente"));

		} else {
			labelErrorCliente.setText("");

		}

	}

	public void loadAssociateObjects() {
		if (clienteService == null) {
			throw new IllegalStateException("Service nulo!");
		}

		List<Cliente> clienteList = clienteService.findAll();
		obsList = FXCollections.observableArrayList(clienteList);
		comboBoxCliente.setItems(obsList);
	}

	private void initializeComboBoxCliente() {
		Callback<ListView<Cliente>, ListCell<Cliente>> factory = lv -> new ListCell<Cliente>() {
			@Override
			protected void updateItem(Cliente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxCliente.setCellFactory(factory);
		comboBoxCliente.setButtonCell(factory.call(null));
	}
}
