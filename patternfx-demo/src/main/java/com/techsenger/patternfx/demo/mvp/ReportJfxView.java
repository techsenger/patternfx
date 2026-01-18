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
import com.techsenger.patternfx.mvp.AbstractChildJfxView;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 *
 * @author Pavel Castornii
 */
public class ReportJfxView extends AbstractChildJfxView<ReportPresenter> implements ReportView {

    private final Label totalPeopleLabel = new Label("Total People:");

    private final Label totalPeopleValueLabel = new Label();

    private final Label averageAgeLabel = new Label("Average Age:");

    private final Label averageAgeValueLabel = new Label();

    private final GridPane gridPane = new GridPane();

    public ReportJfxView() {
        super();
    }

    @Override
    public GridPane getNode() {
        return this.gridPane;
    }

    @Override
    public void requestFocus() {

    }

    @Override
    public void setTotalPeople(String value) {
        totalPeopleValueLabel.textProperty().set(value);
    }

    @Override
    public void setAverageAge(String value) {
        averageAgeValueLabel.textProperty().set(value);
    }

    @Override
    protected void build() {
        super.build();
        var boldStyle = "-fx-font-weight: bold";
        totalPeopleLabel.setStyle(boldStyle);
        totalPeopleValueLabel.setStyle(boldStyle);
        averageAgeLabel.setStyle(boldStyle);
        averageAgeValueLabel.setStyle(boldStyle);
        gridPane.add(totalPeopleLabel, 0, 0);
        gridPane.add(totalPeopleValueLabel, 1, 0);
        gridPane.add(averageAgeLabel, 2, 0);
        gridPane.add(averageAgeValueLabel, 3, 0);
        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            cc.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(cc);
        }
        gridPane.setPadding(new Insets(0, Style.INSET, Style.INSET, Style.INSET));
    }

    @Override
    protected Composer createComposer() {
        return null;
    }
}
