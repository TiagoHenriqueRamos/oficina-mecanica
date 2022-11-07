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
import model.entities.Cliente;
import model.entities.Veiculo;
import model.exceptions.ValidationException;
import model.services.AgendamentoService;
import model.services.ClienteService;
import model.services.RelatorioService;
import model.services.VeiculoService;

public class AgendamentoFinalizar implements Initializable {

    private Agendamento entidade;

    private AgendamentoService agendamentoService;

    private RelatorioService relatorioService;

    private VeiculoService veiculoService;

    private ClienteService clienteService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private Label labelErrorCliente;

    @FXML
    private Label labelErrorId;

    @FXML
    private Label labelErrorVeiculo;

    @FXML
    private Label labelErrorData;

    @FXML
    private Label labelErrorHorario;

    @FXML
    private Label labelErrorObservacao;

    @FXML
    private TextField txtId;

    @FXML
    private ComboBox<Cliente> comboBoxCliente;

    @FXML
    private ComboBox<Veiculo> comboBoxVeiculo;

    @FXML
    private ComboBox<LocalTime> comboBoxHorario;

    @FXML
    private ComboBox<String> comboBoxPagamento;

    @FXML
    private DatePicker dpData;

    @FXML
    private TextField txtObservacao;

    private ObservableList<Cliente> obsListCliente;

    private ObservableList<Veiculo> obsListVeiculo;

    private ObservableList<LocalTime> obsListHorario;

    private ObservableList<String> obsListPagamento;

    @FXML
    private Button btSalvar;

    @FXML
    private Button btCancelar;

    public void setService(VeiculoService veiculoService, ClienteService clienteService,
            AgendamentoService agendamentoService, RelatorioService relatorioService) {
        this.veiculoService = veiculoService;
        this.clienteService = clienteService;
        this.agendamentoService = agendamentoService;
        this.relatorioService = relatorioService;
    }

    public void setEntidade(Agendamento obj) {
        this.entidade = obj;

    }

    @FXML
    public void onBtSalvar(ActionEvent event) {

        if (entidade == null) {
            throw new IllegalStateException("Entidade nula!");
        }
        if (agendamentoService == null || agendamentoService == null) {
            throw new IllegalStateException("Service nulo!");
        }
        try {

            entidade = getFormData();
            agendamentoService.insert(entidade);
            agendamentoService.deleteById(entidade.getId());

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

    private Agendamento getFormData() {
        Agendamento agendamento = new Agendamento();

        ValidationException exception = new ValidationException("Erro de validação!");

        agendamento.setId(Utils.tryParseToInt(txtId.getText()));

        if (comboBoxCliente.getValue() == null) {
            exception.addError("Cliente", "Campo não pode ficar vazio!");
        }

        agendamento.setCliente(comboBoxCliente.getValue());

        if (comboBoxVeiculo.getValue() == null) {
            exception.addError("Veiculo", "Campo não pode ficar vazio!");
        }
        agendamento.setVeiculo(comboBoxVeiculo.getValue());

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

        if (comboBoxPagamento.getValue() == null) {
            exception.addError("Pagamento", "Campo não pode ficar vazio!");
        }
        agendamento.setPagamento(comboBoxPagamento.getValue());
        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return agendamento;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Utils.formatDatePicker(dpData, "dd/MM/yyyy");
        initializeComboBoxCliente();
        initializeComboBoxVeiculo();
        initializeComboBoxHorario();
        initializeComboBoxFormaPagamento();

    }

    public void updateFormData() {
        if (entidade == null) {
            throw new IllegalStateException("Entidade nula!");
        }

        txtId.setText(String.valueOf(entidade.getId()));

        if (entidade.getData() != null) {
            dpData.setValue(LocalDate.ofInstant(entidade.getData().toInstant(), ZoneId.systemDefault()));
        }

        if (comboBoxCliente == null) {
            comboBoxCliente.getSelectionModel().selectFirst();
        }
        comboBoxCliente.setValue(entidade.getCliente());

        if (comboBoxVeiculo == null) {
            comboBoxVeiculo.getSelectionModel().selectFirst();
        }
        comboBoxVeiculo.setValue(entidade.getVeiculo());

        if (entidade.getHorario() == null) {
            comboBoxHorario.getSelectionModel().selectFirst();
        }
        comboBoxHorario.setValue(entidade.getHorario());

        txtObservacao.setText(entidade.getObservacao());

        if (comboBoxPagamento == null) {
            comboBoxPagamento.getSelectionModel().selectFirst();
        }

        comboBoxPagamento.setValue(entidade.getPagamento());

    }

    public void loadAssociateObjects() {
        if (clienteService == null || veiculoService == null || agendamentoService == null) {
            throw new IllegalStateException("Service nulo!");
        }

        List<Cliente> clienteList = clienteService.findAll();
        List<Veiculo> veiculoList = veiculoService.findAll();
        List<LocalTime> horarioList = Utils.createHorario();
        List<String> list = entidade.getOpcoesPagamento();

        obsListCliente = FXCollections.observableArrayList(clienteList);
        comboBoxCliente.setItems(obsListCliente);

        obsListVeiculo = FXCollections.observableArrayList(veiculoList);
        comboBoxVeiculo.setItems(obsListVeiculo);

        obsListHorario = FXCollections.observableArrayList(horarioList);
        comboBoxHorario.setItems(obsListHorario);

        obsListPagamento = FXCollections.observableArrayList(list);
        comboBoxPagamento.setItems(obsListPagamento);

    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("Data")) {
            labelErrorData.setText(errors.get("Data"));

        } else {
            labelErrorData.setText("");

        }

        if (fields.contains("Cliente")) {
            labelErrorCliente.setText(errors.get("Cliente"));

        } else {
            labelErrorCliente.setText("");

        }

        if (fields.contains("Veiculo")) {
            labelErrorVeiculo.setText(errors.get("Veiculo"));

        } else {
            labelErrorVeiculo.setText("");

        }

        if (fields.contains("Horario")) {
            labelErrorHorario.setText(errors.get("Horario"));

        } else {
            labelErrorHorario.setText("");

        }

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

    private void initializeComboBoxVeiculo() {
        Callback<ListView<Veiculo>, ListCell<Veiculo>> factory = lv -> new ListCell<Veiculo>() {
            @Override
            protected void updateItem(Veiculo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getPlaca());
            }
        };
        comboBoxVeiculo.setCellFactory(factory);
        comboBoxVeiculo.setButtonCell(factory.call(null));
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

}
