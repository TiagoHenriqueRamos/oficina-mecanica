package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listener.DataChangeListener;
import gui.utils.Alerts;
import gui.utils.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Cliente;
import model.entities.Veiculo;
import model.services.ClienteService;
import model.services.VeiculoService;

public class VeiculoController implements Initializable, DataChangeListener {

	private VeiculoService veiculoService;

	@FXML
	private TableView<Veiculo> tableViewVeiculo;

	@FXML
	private TableColumn<Veiculo, Integer> tableColumnId;

	@FXML
	private TableColumn<Veiculo, String> tableColumnPlaca;

	@FXML
	private TableColumn<Veiculo, String> tableColumnModelo;

	@FXML
	private TableColumn<Veiculo, String> tableColumnMarca;

	@FXML
	private TableColumn<Cliente, String> tableColumnCliente;

	@FXML
	private TableColumn<Veiculo, Veiculo> tableColumnEDIT;

	@FXML
	private TableColumn<Veiculo, Veiculo> tableColumnREMOVE;

	@FXML
	private Button btNovo;

	private ObservableList<Veiculo> obsList;

	@FXML
	public void onBtNovo(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Veiculo obj = new Veiculo();
		createDialogForm(obj, "/gui/VeiculoFormulario.fxml", parentStage);

	}

	public void setVeiculoService(VeiculoService veiculoService) {
		this.veiculoService = veiculoService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	@Override
	public void onDataChanged() {
		updateTableViewVeiculo();
	}

	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("Placa"));
		tableColumnMarca.setCellValueFactory(new PropertyValueFactory<>("Marca"));
		tableColumnModelo.setCellValueFactory(new PropertyValueFactory<>("Modelo"));
		tableColumnCliente.setCellValueFactory(new PropertyValueFactory<>("Cliente"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVeiculo.prefHeightProperty().bind(stage.heightProperty());
	}

	private void createDialogForm(Veiculo obj, String absoluteName, Stage parentStage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			VeiculoFormulario controller = loader.getController();
			controller.setEntidade(obj);
			controller.setService(new VeiculoService(), new ClienteService());
			controller.loadAssociateObjects();
			controller.subscribeDataChengeListener(this);
			controller.updateFormData();

			Stage cadastroStage = new Stage();
			cadastroStage.setTitle("Novo Veiculo");
			cadastroStage.setScene(new Scene(pane));
			cadastroStage.setResizable(false);
			cadastroStage.initOwner(parentStage);
			cadastroStage.initModality(Modality.WINDOW_MODAL);
			cadastroStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a página!", e.getMessage(), AlertType.ERROR);
		}
	}

	public void updateTableViewVeiculo() {
		if (veiculoService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Veiculo> list = veiculoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewVeiculo.setItems(obsList);
		initEditButtons();
		initRemoveButtons();

	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Veiculo, Veiculo>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Veiculo obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Veiculo, Veiculo>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Veiculo obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/VeiculoFormulario.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void removeEntity(Veiculo obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (veiculoService == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				veiculoService.remove(obj);
				updateTableViewVeiculo();

			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);

			}
		}
	}

}
