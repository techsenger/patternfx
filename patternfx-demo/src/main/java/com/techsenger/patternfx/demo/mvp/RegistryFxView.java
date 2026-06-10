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

package com.techsenger.patternfx.demo.mvp;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.demo.Style;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.mvp.AbstractParentFxView;
import com.techsenger.patternfx.mvp.ComponentParams;
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
public class RegistryFxView<P extends RegistryPresenter<?>> extends AbstractParentFxView<P> implements RegistryView {

    public class Composer extends AbstractParentFxView<P>.Composer implements RegistryView.Composer {

        private final RegistryFxView<P> view = RegistryFxView.this;

        private @Nullable ReportFxView report;

        @Override
        public DialogPort openDialog() {
            var v = new DialogFxView(view.getStage());
            var p = new DialogPresenter<>(v, new ComponentParams());
            p.initialize();
            v.getDialog().showAndWait();
            return p;
        }

        @Override
        public @Nullable ReportPort getReport() {
            if (report == null) {
                return null;
            }
            return report.getPresenter();
        }

        @Override
        public void showReport() {
            if (report != null) {
                throw new IllegalStateException("Report has been added");
            }
            report = new ReportFxView();
            var reportP = new ReportPresenter(report, new ComponentParams());
            reportP.initialize();
            view.root.getChildren().add(report.getNode());
            getModifiableChildren().add(report);
        }

        @Override
        public void hideReport() {
            if (report == null) {
                throw new IllegalStateException("Report hasn't been added");
            }
            getModifiableChildren().remove(report);
            view.root.getChildren().remove(report.getNode());
            report.getPresenter().deinitialize();
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

    public RegistryFxView() {
        super();
    }

    @Override
    public void setRemoveDisabled(boolean v) {
        this.removeButton.setDisable(v);
    }

    @Override
    public void setReportShown(boolean value) {
        if (value) {
            reportButton.setText("Hide Report");
        } else {
            reportButton.setText("Show Report");
        }
    }

    @Override
    public void showStage() {
        this.stage.show();
    }

    @Override
    public void requestFocus() {
        personTable.requestFocus();
    }

    @Override
    public void addPersons(List<Person> persons) {
        this.personTable.getItems().addAll(persons);
    }

    @Override
    public void removePerson(int index) {
        this.personTable.getItems().remove(index);
    }

    @Override
    public void clearPersons() {
        this.personTable.getItems().clear();
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

        setReportShown(false);
        setRemoveDisabled(true);

        stage.setTitle("PatternFX - MVP");
        stage.setScene(new Scene(root, 800, 500));
    }

    @Override
    protected void addHandlers() {
        super.addHandlers();
        var presenter = getPresenter();
        addButton.setOnAction(e -> presenter.onAdd());
        removeButton.setOnAction(e -> presenter.onRemove());
        refreshButton.setOnAction(e -> presenter.onRefresh());
        reportButton.setOnAction(e -> presenter.onReport());
        stage.setOnCloseRequest(e -> presenter.onCloseRequest());
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        personTable.getSelectionModel().selectedIndexProperty()
                .addListener((ov, oldV, newV) -> getPresenter().onSelectedChanged(newV.intValue()));
    }

    @Override
    protected Composer createComposer() {
        return new RegistryFxView<P>.Composer();
    }

    Stage getStage() {
        return stage;
    }

    private void setupColumn(TableColumn<?, ?> column) {
        column.setResizable(false);
        column.prefWidthProperty().bind(personTable.widthProperty().multiply(0.25).subtract(1));
    }
}
