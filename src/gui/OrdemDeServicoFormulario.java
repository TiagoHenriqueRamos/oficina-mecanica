package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Agendamento;
import model.entities.OrdemDeServico;
import model.exceptions.ValidationException;
import model.services.ClienteService;
import model.services.OrdemDeServicoService;
import model.services.VeiculoService;

public class OrdemDeServicoFormulario implements Initializable {

	private OrdemDeServico ordem;

	private Agendamento entidade;

	private OrdemDeServicoService ordemDeServicoService;

	private VeiculoService veiculoService;

	private ClienteService clienteService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private Label labelErrorId;

	@FXML
	private Label labelErrorData;

	@FXML
	private Label labelErrorHorario;

	@FXML
	private Label labelErrorPagamento;
	
	@FXML
	private Label labelErrorObservacao;
	
	@FXML
	private Label labelErrorValor;

	@FXML
	private TextField txtId;

	@FXML
	private ComboBox<LocalTime> comboBoxHorario;

	@FXML
	private ComboBox<String> comboBoxPagamento;

	@FXML
	private DatePicker dpData;

	@FXML
	private TextField txtObservacao;

	private ObservableList<LocalTime> obsListHorario;

	private ObservableList<String> obsListPagamento;
	
	@FXML
	private TextField txtValor;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void setServices(VeiculoService veiculoService, ClienteService clienteService,
			OrdemDeServicoService ordemDeServicoService) {
		this.veiculoService = veiculoService;
		this.clienteService = clienteService;
		this.ordemDeServicoService = ordemDeServicoService;

	}

	public void setEntidade(OrdemDeServico obj) {
		this.ordem = obj;
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

			ordem = getFormData();
			ordemDeServicoService.saveOrUpdate(ordem);
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

	private OrdemDeServico getFormData() {
		OrdemDeServico ordemDeServico = new OrdemDeServico();
		Agendamento agendamento = new Agendamento();
		ValidationException exception = new ValidationException("Erro de validação!");

		ordemDeServico.setId(Utils.tryParseToInt(txtId.getText()));
		ordemDeServico.setObservacao(null);

		if (comboBoxPagamento.getValue() == null) {
			exception.addError("Pagamento", "Campo não pode ficar vazio!");
		}
		agendamento.setPagamento(comboBoxPagamento.getValue());

		if (dpData.getValue() == null) {
			exception.addError("Data", "Campo não pode ficar vazio!");

		} else {
			Instant instant = Instant.from(dpData.getValue().atStartOfDay(ZoneId.systemDefault()));
			agendamento.setData(Date.from(instant));
		}

		if (comboBoxHorario.getValue() == null) {
			exception.addError("Horario", "Campo não pode ficar vazio!");
		}

		agendamento.setHorario(comboBoxHorario.getValue());
		agendamento.setObservacao(txtObservacao.getText());
		ordemDeServico.setValor(Utils.tryParseToDouble(txtValor.getText()));

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return ordemDeServico;

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Utils.formatDatePicker(dpData, "dd/MM/yyyy");

		initializeComboBoxHorario();
		initializeComboBoxFormaPagamento();

	}

	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula!");
		}

		txtId.setText(String.valueOf(entidade.getId()));
		txtObservacao.setText(entidade.getObservacao());
		txtValor.setText(String.valueOf(entidade.getValor()));

		if (entidade.getData() != null) {
			dpData.setValue(LocalDate.ofInstant(entidade.getData().toInstant(), ZoneId.systemDefault()));
		}

		if (comboBoxHorario == null) {
			comboBoxHorario.getSelectionModel().selectFirst();
		}
		if (comboBoxPagamento == null) {
			comboBoxPagamento.getSelectionModel().selectFirst();
		}

		comboBoxHorario.setValue(entidade.getHorario());
		comboBoxPagamento.setValue(entidade.getPagamento());
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("Data")) {
			labelErrorData.setText(errors.get("Data"));

		} else {
			labelErrorData.setText("");

		}

		if (fields.contains("Horario")) {
			labelErrorHorario.setText(errors.get("Horario"));

		} else {
			labelErrorHorario.setText("");

		}

		if (fields.contains("Pagamento")) {
			labelErrorPagamento.setText(errors.get("Pagamento"));

		} else {
			labelErrorPagamento.setText("");

		}

	}

	private void initializeComboBoxHorario() {
		Callback<ListView<LocalTime>, ListCell<LocalTime>> factory = lv -> new ListCell<LocalTime>() {
			@Override

			protected void updateItem(LocalTime item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.toString());
			}
		};
		comboBoxHorario.setCellFactory(factory);
		comboBoxHorario.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxFormaPagamento() {
		Callback<ListView<String>, ListCell<String>> factory = lv -> new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.toString());
			}
		};
		comboBoxPagamento.setCellFactory(factory);
		comboBoxPagamento.setButtonCell(factory.call(null));
	}

	public void loadAssociateObjects() {
		if (clienteService == null || veiculoService == null || ordemDeServicoService == null) {
			throw new IllegalStateException("Service nulo!");
		}

		List<LocalTime> horarioList = Utils.createHorario();
		List<String> pagamentoList = ordem.getOpcoesPagamento();

		obsListHorario = FXCollections.observableArrayList(horarioList);
		comboBoxHorario.setItems(obsListHorario);

		obsListPagamento = FXCollections.observableArrayList(pagamentoList);
		comboBoxPagamento.setItems(obsListPagamento);
	}
}
