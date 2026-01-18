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

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Pavel Castornii
 */
public class Demo extends Application {

    private enum Pattern {
        MVP(new MvpRunner()), MVVM(new MvvmRunner()), MVVMX(new MvvmxRunner());

        private final Runnable runner;

        Pattern(Runnable runner) {
            this.runner = runner;
        }

        public Runnable getRunner() {
            return runner;
        }
    }

    private final Label label = new Label("Select Pattern:");

    private final ObservableList<Pattern> patterns = FXCollections.observableArrayList(Pattern.values());

    private final ListView<Pattern> patternListView = new ListView<>(patterns);

    private final VBox root = new VBox(label, patternListView);

    @Override
    public void start(Stage stage) throws Exception {
        patternListView.setCellFactory(lv -> {
            ListCell<Pattern> cell = new ListCell<>() {
                @Override
                protected void updateItem(Pattern item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.toString());
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty() && event.getClickCount() == 2) {
                    Pattern pattern = cell.getItem();
                    // the unit will be deinitialized automatically when the stage
                    // is closed, via the handler registered with stage#setOnCloseRequest
                    pattern.getRunner().run();
                }
            });
            return cell;
        });
        root.setSpacing(Style.INSET);
        root.setPadding(new Insets(Style.INSET));
        var scene = new Scene(root, 300, 200);
        stage.setTitle("PatternFX");
        stage.setScene(scene);
        stage.show();
    }
}
