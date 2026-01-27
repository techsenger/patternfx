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

import com.techsenger.patternfx.mvp.AbstractParentFxView;
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
public class DialogFxView extends AbstractParentFxView<DialogPresenter<?>> implements DialogView {

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

    private Button okButton;

    public DialogFxView(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFirstNameValid(boolean value) {
        updateValid(firstNameTextField, value);
    }

    @Override
    public void setLastNameValid(boolean value) {
        updateValid(lastNameTextField, value);
    }

    @Override
    public void setAgeValid(boolean value) {
        updateValid(ageTextField, value);
    }

    @Override
    public String getFirstName() {
        return this.firstNameTextField.getText();
    }

    @Override
    public String getLastName() {
        return this.lastNameTextField.getText();
    }

    @Override
    public String getAge() {
        return this.ageTextField.getText();
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
    }

    @Override
    protected void addHandlers() {
        super.addHandlers();
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!getPresenter().handleOkAction()) {
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
