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

package com.techsenger.patternfx.demo.mvp;

import com.google.errorprone.annotations.concurrent.LazyInit;
import com.techsenger.patternfx.mvp.AbstractFxView;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Pavel Castornii
 */
public class DialogFxView extends AbstractFxView<DialogPresenter<?>> implements DialogView {

    private final Stage stage;

    private final Label firstNameLabel = new Label("First Name");

    private final TextField firstNameTextField = new TextField();

    private final Label lastNameLabel = new Label("Last Name");

    private final TextField lastNameTextField = new TextField();

    private final TextField ageTextField = new TextField();

    private final Label ageLabel = new Label("Age");

    private final HBox hBox = new HBox(firstNameLabel, firstNameTextField, lastNameLabel, lastNameTextField,
                ageLabel, ageTextField);

    private final Dialog<ButtonType> dialog = new Dialog<>();

    @LazyInit
    private Button okButton;

    public DialogFxView(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void updateFirstNameValid(boolean value) {
        updateValid(firstNameTextField, value);
    }

    @Override
    public void updateLastNameValid(boolean value) {
        updateValid(lastNameTextField, value);
    }

    @Override
    public void updateAgeValid(boolean value) {
        updateValid(ageTextField, value);
    }

    protected Dialog<ButtonType> getDialog() {
        return this.dialog;
    }

    @Override
    protected void build() {
        super.build();
        firstNameLabel.setMinWidth(Region.USE_PREF_SIZE);
        HBox.setHgrow(firstNameTextField, Priority.ALWAYS);
        lastNameLabel.setMinWidth(Region.USE_PREF_SIZE);
        HBox.setHgrow(lastNameTextField, Priority.ALWAYS);
        ageLabel.setMinWidth(Region.USE_PREF_SIZE);
        HBox.setHgrow(ageTextField, Priority.ALWAYS);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);

        dialog.initOwner(stage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.getDialogPane().setContent(hBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        this.okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        dialog.setOnShown(e -> firstNameTextField.requestFocus());
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        var p = getPresenter();
        this.firstNameTextField.textProperty().addListener((ov, oldV, newV) -> p.onFirstNameChanged(newV));
        this.lastNameTextField.textProperty().addListener((ov, oldV, newV) -> p.onLastNameChanged(newV));
        this.ageTextField.textProperty().addListener((ov, oldV, newV) -> p.onAgeChanged(newV));
    }

    @Override
    protected void addHandlers() {
        super.addHandlers();
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!getPresenter().onOk()) {
                event.consume();
            }
        });
    }

    private void updateValid(TextField textField, boolean valid) {
        if (valid) {
            textField.setStyle("");
        } else {
            textField.setStyle("-fx-background-color: red, white; -fx-background-insets: 0, 1");
        }
    }

}
