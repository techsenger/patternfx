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

package com.techsenger.patternfx.demo.mvvm;

import com.techsenger.patternfx.core.ComponentParams;
import com.techsenger.patternfx.core.ComponentState;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.demo.model.PersonService;
import com.techsenger.patternfx.mvvm.AbstractParentViewModel;
import java.util.Iterator;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public class RegistryViewModel extends AbstractParentViewModel<RegistryComposer> {

    private static final Logger logger = LoggerFactory.getLogger(RegistryViewModel.class);

    private final PersonService service;

    private final ObservableList<Person> persons = FXCollections.observableArrayList();

    private final ObjectProperty<Person> selectedPerson = new SimpleObjectProperty<>();

    private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);

    private final ReadOnlyStringWrapper reportButtonText = new ReadOnlyStringWrapper();

    public RegistryViewModel(RegistryParams params) {
        super(params);
        this.service = params.getService();
    }

    @Override
    protected void initialize() {
        super.initialize();
        selectedPerson.addListener((ov, oldV, newV) -> {
            removeDisabled.set(newV == null);
            // log message example with component meta
            logger.debug("{} Selected person property changed", getDescriptor().getLogPrefix());
        });
        getDescriptor().stateProperty().addListener((ov, oldV, newV) -> {
            if (newV == ComponentState.INITIALIZED) {
                onRefresh();
            }
        });
        persons.addListener((ListChangeListener<Person>) e -> {
            var report = getComposer().getReport();
            if (report != null) {
                report.refresh(persons);
            }
        });
        updateReportButtonText(false);
    }

    ObservableList<Person> getPersons() {
        return persons;
    }

    ObjectProperty<Person> selectedPersonProperty() {
        return selectedPerson;
    }

    BooleanProperty removeDisabledProperty() {
        return this.removeDisabled;
    }

    ReadOnlyStringProperty reportButtonTextProperty() {
        return reportButtonText.getReadOnlyProperty();
    }

    String getReportButtonText() {
        return reportButtonText.get();
    }

    void onRefresh() {
        persons.clear();
        persons.addAll(service.readAll());
    }

    void onAdd() {
        var dialog = getComposer().openDialog(new ComponentParams());
        var newPerson = dialog.getResult();
        dialog.requestDeinitialize();
        if (newPerson != null) {
            add(newPerson);
        }
    }

    void onRemove() {
        var id = selectedPerson.get().getId();
        Objects.requireNonNull(id);
        service.delete(id);
        for (Iterator<Person> it = persons.iterator(); it.hasNext();) {
            Person p = it.next();
            if (Objects.equals(p.getId(), id)) {
                it.remove();
                break;
            }
        }
    }

    void onToggleReport() {
        var composer = getComposer();
        if (composer.getReport() == null) {
            composer.showReport(new ComponentParams());
            var report = Objects.requireNonNull(composer.getReport());
            report.refresh(persons);
            updateReportButtonText(true);
        } else {
            composer.hideReport();
            updateReportButtonText(false);
        }
    }

    void onClose() {
        requestDeinitializeTree();
    }

    private void add(Person person) {
        service.save(person);
        persons.add(person);
    }

    private void updateReportButtonText(boolean visible) {
        if (visible) {
            reportButtonText.set("Hide Report");
        } else {
            reportButtonText.set("Show Report");
        }
    }
}
