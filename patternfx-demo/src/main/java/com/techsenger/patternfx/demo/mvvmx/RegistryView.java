/*
 * Copyright 2024-2025 Pavel Castornii.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.techsenger.patternfx.demo.mvvmx;

import com.techsenger.patternfx.demo.Style;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.mvvmx.AbstractParentView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Pavel Castornii
 */
public class RegistryView extends AbstractParentView<RegistryViewModel, RegistryComponent> {

    private final Button addButton = new Button("Add");

    private final Button removeButton = new Button("Remove");

    private final Button refreshButton = new Button("Refresh");

    private final Button reportButton = new Button();

    private final Pane spacer = new Pane();

    private final ToolBar toolBar = new ToolBar(addButton, removeButton, refreshButton, spacer, reportButton);

    private final TableView<Person> personTable = new TableView<>();

    private final VBox content = new VBox(personTable);

    private final VBox root = new VBox(toolBar, content);

    private final Stage stage = new Stage();

    public RegistryView(RegistryViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected void initialize() {
        super.initialize();
        stage.show();
    }

    @Override
    protected void build() {
        super.build();
        VBox.setVgrow(personTable, Priority.ALWAYS);
        personTable.setItems(getViewModel().getPersons());
        personTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        var idColumn = new TableColumn<Person, Integer>("Id");
        setupColumn(idColumn);
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        var firstNameColumn = new TableColumn<Person, String>("First Name");
        setupColumn(firstNameColumn);
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));
        var lastNameColumn = new TableColumn<Person, String>("Last Name");
        setupColumn(lastNameColumn);
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));
        var ageColumn = new TableColumn<Person, Integer>("Age");
        setupColumn(ageColumn);
        ageColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAge()));
        personTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, ageColumn);

        VBox.setVgrow(content, Priority.ALWAYS);
        content.setPadding(new Insets(Style.INSET));

        HBox.setHgrow(spacer, Priority.ALWAYS);

        stage.setTitle("PatternFX - MVVMX");
        stage.setScene(new Scene(root, 800, 500));
    }

    @Override
    protected void bind() {
        super.bind();
        var viewModel = getViewModel();
        viewModel.selectedPersonProperty().bind(personTable.getSelectionModel().selectedItemProperty());
        removeButton.disableProperty().bind(viewModel.removeDisabledProperty());
        reportButton.textProperty().bind(viewModel.reportButtonTextProperty());
    }

    @Override
    protected void addHandlers() {
        super.addHandlers();
        var viewModel = getViewModel();
        addButton.setOnAction(e -> viewModel.add());
        removeButton.setOnAction(e -> viewModel.remove());
        refreshButton.setOnAction(e -> viewModel.refresh());
        reportButton.setOnAction(e -> viewModel.toggleReport());
        stage.setOnCloseRequest(e -> viewModel.close());
    }

    void addReport(ReportView report) {
        this.root.getChildren().add(report.getNode());
    }

    void removeReport() {
        this.root.getChildren().remove(2);
    }

    Stage getStage() {
        return stage;
    }

    private void setupColumn(TableColumn<?, ?> column) {
        column.setResizable(false);
        column.prefWidthProperty().bind(personTable.widthProperty().multiply(0.25).subtract(1));
    }
}
