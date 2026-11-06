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

package com.techsenger.patternfx.demo.mvc;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.core.ComponentParams;
import com.techsenger.patternfx.demo.Style;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.mvc.AbstractParentView;
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
 * MVC keeps the Controller in direct control of concrete UI elements, so the Composer here is a plain concrete
 * class (no separate Composer interface, unlike the MVP/MVVM demos) and the outer View exposes its controls to
 * {@link RegistryController} directly instead of declaring semantic update methods.
 *
 * @author Pavel Castornii
 */
public class RegistryView extends AbstractParentView<RegistryController<?>> {

    public class Composer extends AbstractParentView<RegistryController<?>>.Composer {

        private final RegistryView view = RegistryView.this;

        private @Nullable ReportView report;

        public DialogPort openDialog() {
            var v = new DialogView(view.getStage());
            var c = new DialogController<>(v, new ComponentParams());
            c.initialize();
            v.getDialog().showAndWait();
            return c;
        }

        public @Nullable ReportPort getReport() {
            if (report == null) {
                return null;
            }
            return report.getController();
        }

        public void showReport() {
            if (report != null) {
                throw new IllegalStateException("Report has been added");
            }
            report = new ReportView();
            var c = new ReportController(report, new ComponentParams());
            c.initialize();
            getModifiableChildren().add(report);
            view.root.getChildren().add(report.getNode());
        }

        public void hideReport() {
            if (report == null) {
                throw new IllegalStateException("Report hasn't been added");
            }
            view.root.getChildren().remove(report.getNode());
            getModifiableChildren().remove(report);
            report.getController().deinitialize();
            report = null;
        }
    }

    private final Button addButton = new Button("Add");

    private final Button removeButton = new Button("Remove");

    private final Button refreshButton = new Button("Refresh");

    private final Button reportButton = new Button("Show Report");

    private final Pane spacer = new Pane();

    private final ToolBar toolBar = new ToolBar(addButton, removeButton, refreshButton, spacer, reportButton);

    private final TableView<Person> personTable = new TableView<>();

    private final VBox content = new VBox(personTable);

    private final VBox root = new VBox(toolBar, content);

    private final Stage stage = new Stage();

    public RegistryView() {
        super();
    }

    @Override
    public Composer getComposer() {
        return (Composer) super.getComposer();
    }

    @Override
    protected void build() {
        super.build();
        VBox.setVgrow(personTable, Priority.ALWAYS);
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

        removeButton.setDisable(true);

        stage.setTitle("PatternFX - MVC");
        stage.setScene(new Scene(root, 800, 500));
    }

    @Override
    protected void addHandlers() {
        super.addHandlers();
        var controller = getController();
        addButton.setOnAction(e -> controller.onAdd());
        removeButton.setOnAction(e -> controller.onRemove());
        refreshButton.setOnAction(e -> controller.onRefresh());
        reportButton.setOnAction(e -> controller.onReport());
        stage.setOnCloseRequest(e -> controller.onCloseRequest());
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        personTable.getSelectionModel().selectedIndexProperty()
                .addListener((ov, oldV, newV) -> getController().onSelectedChanged(newV.intValue()));
    }

    @Override
    protected Composer createComposer() {
        return new RegistryView.Composer();
    }

    void show() {
        this.stage.show();
    }

    Stage getStage() {
        return stage;
    }

    Button getRemoveButton() {
        return removeButton;
    }

    Button getReportButton() {
        return reportButton;
    }

    TableView<Person> getPersonTable() {
        return personTable;
    }

    private void setupColumn(TableColumn<?, ?> column) {
        column.setResizable(false);
        column.prefWidthProperty().bind(personTable.widthProperty().multiply(0.25).subtract(1));
    }
}
