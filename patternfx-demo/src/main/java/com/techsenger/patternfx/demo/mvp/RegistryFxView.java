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

import com.techsenger.patternfx.demo.Style;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.mvp.AbstractParentFxView;
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
public class RegistryFxView<P extends RegistryPresenter<?, ?>> extends AbstractParentFxView<P> implements RegistryView {

    public class Composer extends AbstractParentFxView.Composer implements RegistryComposer {

        private final RegistryFxView<?> view = RegistryFxView.this;

        @Override
        public DialogPort showDialog() {
            var v = new DialogFxView(view.getStage());
            var p = new DialogPresenter<>(v);
            p.initialize();
            v.getDialog().showAndWait();
            return p.getPort();
        }

        @Override
        public ReportPort getReport() {
            if (view.getReport() == null) {
                return null;
            }
            return view.getReport().getPresenter().getPort();
        }

        @Override
        public void addReport() {
            if (view.getReport() != null) {
                throw new IllegalStateException("Report has been added");
            }
            var reportV = new ReportFxView();
            var reportP = new ReportPresenter(reportV);
            reportP.initialize();
            view.root.getChildren().add(reportV.getNode());
            view.getModifiableChildren().add(reportV);
            view.report = reportV;
        }

        @Override
        public void removeReport() {
            var report = view.getReport();
            if (report == null) {
                throw new IllegalStateException("Report hasn't been added");
            }
            view.getModifiableChildren().remove(report);
            view.root.getChildren().remove(report.getNode());
            view.report = null;
            report.getPresenter().deinitialize();
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

    private ReportFxView report;

    public RegistryFxView() {
        super();
    }

    @Override
    public void setRemoveDisable(boolean v) {
        this.removeButton.setDisable(v);
    }

    @Override
    public void setReportVisible(boolean visible) {
        if (visible) {
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
    public List<Person> getPersons() {
        return this.personTable.getItems();
    }

    @Override
    public void removePerson(int index) {
        this.personTable.getItems().remove(index);
    }

    @Override
    public int getSelectedIndex() {
        return this.personTable.getSelectionModel().getSelectedIndex();
    }

    @Override
    public void clearPersons() {
        this.personTable.getItems().clear();
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
        personTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, ageColumn);

        VBox.setVgrow(content, Priority.ALWAYS);
        content.setPadding(new Insets(Style.INSET));

        HBox.setHgrow(spacer, Priority.ALWAYS);

        setReportVisible(false);
        setRemoveDisable(true);

        stage.setTitle("PatternFX - MVP");
        stage.setScene(new Scene(root, 800, 500));
    }

    @Override
    protected void addHandlers() {
        super.addHandlers();
        var presenter = getPresenter();
        addButton.setOnAction(e -> presenter.handleAddAction());
        removeButton.setOnAction(e -> presenter.handleRemoveAction());
        refreshButton.setOnAction(e -> presenter.handleRefreshAction());
        reportButton.setOnAction(e -> presenter.handleReportAction());
        stage.setOnCloseRequest(e -> presenter.handleCloseRequest());
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        personTable.getSelectionModel().selectedIndexProperty()
                .addListener((ov, oldV, newV) -> getPresenter().handleSelectedChange(newV.intValue()));
    }

    @Override
    public Composer getComposer() {
        return (Composer) super.getComposer();
    }

    @Override
    protected Composer createComposer() {
        return new RegistryFxView.Composer();
    }

    Stage getStage() {
        return stage;
    }

    ReportFxView getReport() {
        return report;
    }

    private void setupColumn(TableColumn<?, ?> column) {
        column.setResizable(false);
        column.prefWidthProperty().bind(personTable.widthProperty().multiply(0.25).subtract(1));
    }
}
