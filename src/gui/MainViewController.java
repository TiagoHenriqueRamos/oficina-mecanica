package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.utils.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.AgendamentoService;
import model.services.ClienteService;
import model.services.OrdemDeServicoService;
import model.services.VeiculoService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemCadastroCliente;

	@FXML
	private MenuItem menuItemCadastroVeiculo;

	@FXML
	private MenuItem menuItemAgendamento;

	@FXML
	private MenuItem menuItemOrdemDeServico;


	@FXML
	private MenuItem menuItemRelatorio;

	@FXML
	public void onMenuItemCadastroCliente() {
		loadView("/gui/Cliente.fxml", (ClienteController controller) -> {
			controller.setClienteService(new ClienteService());
			controller.updateTableViewCliente();
		});
	}

	@FXML
	public void onMenuItemCadastroVeiculo() {
		loadView("/gui/Veiculo.fxml", (VeiculoController controller) -> {
			controller.setVeiculoService(new VeiculoService());
			controller.updateTableViewVeiculo();
		});
	}

	@FXML
	public void onMenuItemAgendamento() {
		loadView("/gui/Agendamento.fxml", (AgendamentoController controller) -> {
			controller.setAgendamentoService(new AgendamentoService());
			controller.updateTableViewAgendamento();
		});
	}

	@FXML
	public void onMenuItemOrdemDeServico() {
		loadView("/gui/OrdemDeServico.fxml", (OrdemDeServicoController controller) -> {
			controller.setServices(new AgendamentoService(), new OrdemDeServicoService());
			controller.updateTableViewOrdemDeServico();

		});
	}

	@FXML
	public void onMenuItemRelatorio() {
		loadView("/gui/Relatorio.fxml", (RelatorioController controller) -> {
			controller.setRelatorioService(new AgendamentoService(), new OrdemDeServicoService());
			controller.updateTableViewRelatorio();

		});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initialingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initialingAction.accept(controller);

		}

		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a pagina", e.getMessage(), AlertType.ERROR);

		}
	}

}
