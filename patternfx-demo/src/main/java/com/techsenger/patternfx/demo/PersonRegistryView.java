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

package com.techsenger.patternfx.demo;

import com.techsenger.patternfx.core.AbstractParentView;
import com.techsenger.patternfx.demo.model.Person;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Pavel Castornii
 */
public class PersonRegistryView extends AbstractParentView<PersonRegistryViewModel, PersonRegistryComponent> {

    private final Button addButton = new Button("Add");

    private final Button removeButton = new Button("Remove");

    private final Button refreshButton = new Button("Refresh");

    private final ToolBar toolBar = new ToolBar(addButton, removeButton, refreshButton);

    private final TableView<Person> personTable = new TableView<>();

    private final VBox content = new VBox(personTable);

    private final VBox root = new VBox(toolBar, content);

    private final Stage stage;

    public PersonRegistryView(Stage stage, PersonRegistryViewModel viewModel) {
        super(viewModel);
        this.stage = stage;
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
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        var firstNameColumn = new TableColumn<Person, String>("First Name");
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));
        var lastNameColumn = new TableColumn<Person, String>("Last Name");
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));
        var ageColumn = new TableColumn<Person, Integer>("Age");
        ageColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAge()));
        personTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, ageColumn);

        VBox.setVgrow(content, Priority.ALWAYS);
        content.setPadding(new Insets(Style.INSET));

        stage.setScene(new Scene(root, 800, 500));
    }

    @Override
    protected void bind() {
        super.bind();
        var vm = getViewModel();
        stage.titleProperty().bind(vm.titleProperty());
        vm.selectedPersonProperty().bind(personTable.getSelectionModel().selectedItemProperty());
        removeButton.disableProperty().bind(vm.removeDisabledProperty());
    }

    @Override
    protected void addHandlers() {
        super.addHandlers();
        var vm = getViewModel();
        addButton.setOnAction(e -> {
            var dialogVM = vm.createDialog();
            var dialogV = new PersonDialogView(stage, dialogVM);
            var dialogComponent = new PersonDialogComponent(dialogV);
            dialogComponent.initialize();

            var jfxDialog = dialogV.getDialog();
            var result = jfxDialog.showAndWait();
            vm.add(result);

            dialogComponent.deinitialize();
        });
        removeButton.setOnAction(e -> vm.remove());
        refreshButton.setOnAction(e -> vm.refresh());
    }
}
