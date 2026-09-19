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

import com.techsenger.patternfx.demo.model.PersonService;
import com.techsenger.patternfx.mvc.AbstractParentController;
import java.util.Objects;

/**
 *
 * @author Pavel Castornii
 */
public class RegistryController<V extends RegistryView> extends AbstractParentController<V> {

    private final PersonService service;

    public RegistryController(V view, RegistryParams params) {
        super(view, params);
        this.service = params.getService();
    }

    public boolean isReportShown() {
        return getView().getComposer().getReport() != null;
    }

    protected void onSelectedChanged(int value) {
        getView().getRemoveButton().setDisable(value < 0);
    }

    protected void onAdd() {
        var dialog = getView().getComposer().openDialog();
        var newPerson = dialog.getResult();
        dialog.deinitialize();
        if (newPerson != null) {
            service.save(newPerson);
            getView().getPersonTable().getItems().add(newPerson);
            refreshReport();
        }
    }

    protected void onRemove() {
        var selectedIndex = getView().getPersonTable().getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            return;
        }
        var person = getView().getPersonTable().getItems().get(selectedIndex);
        Objects.requireNonNull(person.getId());
        service.delete(person.getId());
        getView().getPersonTable().getItems().remove(selectedIndex);
        refreshReport();
    }

    protected void onRefresh() {
        getView().getPersonTable().getItems().clear();
        getView().getPersonTable().getItems().addAll(service.readAll());
        refreshReport();
    }

    protected void onReport() {
        var composer = getView().getComposer();
        if (composer.getReport() == null) {
            composer.showReport();
            refreshReport();
            getView().getReportButton().setText("Hide Report");
        } else {
            composer.hideReport();
            getView().getReportButton().setText("Show Report");
        }
    }

    protected void onCloseRequest() {
        deinitialize();
    }

    @Override
    protected void postInitialize() {
        super.postInitialize();
        getView().show();
        onRefresh();
    }

    private void refreshReport() {
        var report = getView().getComposer().getReport();
        if (report != null) {
            report.refresh(getView().getPersonTable().getItems());
        }
    }
}
