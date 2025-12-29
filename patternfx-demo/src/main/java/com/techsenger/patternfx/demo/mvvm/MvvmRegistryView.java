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

package com.techsenger.patternfx.demo.mvvm;

import com.techsenger.patternfx.demo.Style;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.mvvm.AbstractParentView;
import com.techsenger.patternfx.mvvm.BindingUtils;
import com.techsenger.patternfx.mvvm.Composer;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
public class MvvmRegistryView extends AbstractParentView<MvvmRegistryViewModel> {

    private final class ComposerImpl implements MvvmRegistryComposer {

        @Override
        public void openDialog(MvvmDialogViewModel vm) {
            var dialogV = new MvvmDialogView(vm, stage);
            dialogV.initialize();
            dialogV.getDialog().showAndWait();
            dialogV.deinitialize();
        }

        @Override
        public void addReport(MvvmReportViewModel vm) {
            var v = new MvvmReportView(vm);
            v.initialize();
            root.getChildren().add(v.getNode());
            getModifiableChildren().add(v);
            report.set(v);
        }

        @Override
        public void removeReport() {
            root.getChildren().remove(root.getChildren().size() - 1);
            getModifiableChildren().remove(report.get());
            report.get().deinitialize();
            report.set(null);
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

    private final ReadOnlyObjectWrapper<MvvmReportView> report = new ReadOnlyObjectWrapper<>();

    private final Stage stage = new Stage();

    public MvvmRegistryView(MvvmRegistryViewModel viewModel) {
        super(viewModel);
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
        personTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, ageColumn);

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
        viewModel.selectedPersonProperty().bind(personTable.getSelectionModel().selectedItemProperty());
        removeButton.disableProperty().bind(viewModel.removeDisabledProperty());
        reportButton.textProperty().bind(viewModel.reportButtonTextProperty());
        BindingUtils.bind(viewModel.getReportWrapper(), report);
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

    @Override
    protected void postInitialize() {
        super.postInitialize();
        stage.show();
    }

    @Override
    protected Composer createComposer() {
        return new ComposerImpl();
    }

    Stage getStage() {
        return stage;
    }

    private void setupColumn(TableColumn<?, ?> column) {
        column.setResizable(false);
        column.prefWidthProperty().bind(personTable.widthProperty().multiply(0.25).subtract(1));
    }
}
