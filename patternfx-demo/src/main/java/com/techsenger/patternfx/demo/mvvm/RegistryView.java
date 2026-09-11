/*
 * Copyright 2024-2026 Pavel Castornii.
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

package com.techsenger.patternfx.demo.mvvm;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.core.ComponentParams;
import com.techsenger.patternfx.demo.Style;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.mvvm.AbstractParentView;
import java.util.List;
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
public class RegistryView extends AbstractParentView<RegistryViewModel> {

    public class Composer extends AbstractParentView<RegistryViewModel>.Composer implements RegistryComposer {

        private @Nullable ReportView report;

        @Override
        public DialogPort openDialog(ComponentParams params) {
            var viewModel = new DialogViewModel(params);
            var v = new DialogView(viewModel, stage);
            v.initialize();
            v.getDialog().showAndWait();
            return viewModel;
        }

        @Override
        public @Nullable ReportPort getReport() {
            return report == null ? null : report.getViewModel();
        }

        @Override
        public void showReport(ComponentParams params) {
            if (report != null) {
                throw new IllegalStateException("Report has been added");
            }
            var viewModel = new ReportViewModel(params);
            var v = new ReportView(viewModel);
            v.initialize();
            root.getChildren().add(v.getNode());
            getModifiableChildren().add(v);
            report = v;
        }

        @Override
        public void hideReport() {
            if (report == null) {
                throw new IllegalStateException("Report hasn't been added");
            }
            getModifiableChildren().remove(report);
            root.getChildren().remove(report.getNode());
            report.deinitialize();
            report = null;
        }
    }

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
    public Composer getComposer() {
        return (Composer) super.getComposer();
    }

    @Override
    protected void build() {
        super.build();
        VBox.setVgrow(personTable, Priority.ALWAYS);
        personTable.setItems(getViewModel().getPersons());
        personTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        var idColumn = new TableColumn<Person, Integer>("Id");
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        setupColumn(idColumn);
        var firstNameColumn = new TableColumn<Person, String>("First Name");
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));
        setupColumn(firstNameColumn);
        var lastNameColumn = new TableColumn<Person, String>("Last Name");
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));
        setupColumn(lastNameColumn);
        var ageColumn = new TableColumn<Person, Integer>("Age");
        ageColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAge()));
        setupColumn(ageColumn);
        var columns = List.of(idColumn, firstNameColumn, lastNameColumn, ageColumn);
        personTable.getColumns().addAll(columns);

        VBox.setVgrow(content, Priority.ALWAYS);
        content.setPadding(new Insets(Style.INSET));

        HBox.setHgrow(spacer, Priority.ALWAYS);

        stage.setTitle("PatternFX - MVVM");
        stage.setScene(new Scene(root, 800, 500));
    }

    @Override
    protected void bind() {
        super.bind();
        var viewModel = getViewModel();
        viewModel.selectedPersonWrapper().bind(personTable.getSelectionModel().selectedItemProperty());
        removeButton.disableProperty().bind(viewModel.removeDisabledProperty());
        reportButton.textProperty().bind(viewModel.reportButtonTextProperty());
    }

    @Override
    protected void addHandlers() {
        super.addHandlers();
        var viewModel = getViewModel();
        addButton.setOnAction(e -> viewModel.onAdd());
        removeButton.setOnAction(e -> viewModel.onRemove());
        refreshButton.setOnAction(e -> viewModel.onRefresh());
        reportButton.setOnAction(e -> viewModel.onToggleReport());
        stage.setOnCloseRequest(e -> viewModel.onClose());
        stage.show();
    }

    @Override
    protected Composer createComposer() {
        return new RegistryView.Composer();
    }

    Stage getStage() {
        return stage;
    }

    private void setupColumn(TableColumn<?, ?> column) {
        column.setResizable(false);
        column.prefWidthProperty().bind(personTable.widthProperty().multiply(0.25).subtract(1));
    }
}
