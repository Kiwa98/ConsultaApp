package com.wms.consulta;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConsultaController {

    @FXML private TextField txtEan;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNome;

    @FXML private TableView<Product> tabelaResultados;
    @FXML private TableColumn<Product, String> colEan;
    @FXML private TableColumn<Product, String> colCodigo;
    @FXML private TableColumn<Product, String> colNome;
    @FXML private TableColumn<Product, Integer> colQtd;
    @FXML private TableColumn<Product, String> colLocal;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        colEan.setCellValueFactory(data -> data.getValue().eanProperty());
        colCodigo.setCellValueFactory(data -> data.getValue().codigoProperty());
        colNome.setCellValueFactory(data -> data.getValue().nomeProperty());
        colQtd.setCellValueFactory(data -> data.getValue().quantidadeProperty().asObject());
        colLocal.setCellValueFactory(data -> data.getValue().localProperty());
    }

    @FXML
    protected void onConsultar() {

        String filtro = !txtEan.getText().isEmpty() ? txtEan.getText().trim() :
                !txtCodigo.getText().isEmpty() ? txtCodigo.getText().trim() :
                        txtNome.getText().trim();

        if (filtro.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Digite EAN, Código ou Nome.");
            alert.show();
            return;
        }

        try {
            String url = "https://wmsapi-1.onrender.com/estoque";

            Request req = new Request.Builder().url(url).build();
            Response res = client.newCall(req).execute();

            if (!res.isSuccessful()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao conectar: " + res.code());
                alert.show();
                return;
            }

            String json = res.body().string();
            JsonNode array = mapper.readTree(json);

            ObservableList<Product> produtos = FXCollections.observableArrayList();

            for (JsonNode item : array) {

                boolean match =
                        item.get("ean").asText().equalsIgnoreCase(filtro) ||
                                item.get("totvs").asText().equalsIgnoreCase(filtro) ||
                                item.get("nome").asText().toLowerCase().contains(filtro.toLowerCase());

                if (match) {
                    produtos.add(new Product(
                            item.get("ean").asText(),
                            item.get("totvs").asText(),
                            item.get("nome").asText(),
                            item.get("quantidade").asInt(),
                            item.get("localizacao").asText()
                    ));
                }
            }

            tabelaResultados.setItems(produtos);

            if (produtos.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nenhum produto encontrado.");
                alert.show();
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro: " + e.getMessage());
            alert.show();
        }
    }

    // 🔥 NOVO: LISTAR TUDO
    @FXML
    protected void onListarTudo() {
        try {
            String url = "https://wmsapi-1.onrender.com/estoque";

            Request req = new Request.Builder().url(url).build();
            Response res = client.newCall(req).execute();

            String json = res.body().string();
            JsonNode array = mapper.readTree(json);

            ObservableList<Product> produtos = FXCollections.observableArrayList();

            for (JsonNode item : array) {
                produtos.add(new Product(
                        item.get("ean").asText(),
                        item.get("totvs").asText(),
                        item.get("nome").asText(),
                        item.get("quantidade").asInt(),
                        item.get("localizacao").asText()
                ));
            }

            tabelaResultados.setItems(produtos);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro: " + e.getMessage());
            alert.show();
        }
    }
}
