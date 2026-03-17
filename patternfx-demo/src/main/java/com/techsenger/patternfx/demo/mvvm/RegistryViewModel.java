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

package com.techsenger.patternfx.demo.mvvm;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.core.ComponentState;
import com.techsenger.patternfx.demo.DemoNames;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.demo.model.PersonService;
import com.techsenger.patternfx.mvvm.AbstractParentViewModel;
import com.techsenger.patternfx.mvvm.Descriptor;
import java.util.Iterator;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
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

    private final ReadOnlyObjectWrapper<ReportViewModel> report = new ReadOnlyObjectWrapper<>();

    public RegistryViewModel(PersonService service) {
        this.service = service;
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
            var r = getReport();
            if (r != null) {
                r.refresh(persons);
            }
        });
        updateReportButtonText(false);
    }

    @Override
    protected Descriptor createDescriptor() {
        return new Descriptor(DemoNames.PERSON_REGISTRY);
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

    ReadOnlyObjectProperty<ReportViewModel> reportProperty() {
        return report.getReadOnlyProperty();
    }

    String getReportButtonText() {
        return reportButtonText.get();
    }

    @Nullable ReportViewModel getReport() {
        return report.get();
    }

    ReadOnlyObjectWrapper<ReportViewModel> getReportWrapper() {
        return report;
    }

    void onRefresh() {
        persons.clear();
        persons.addAll(service.readAll());
    }

    void onAdd() {
        var dialogVM = new DialogViewModel((p) -> add(p));
        Objects.requireNonNull(getComposer());
        getComposer().openDialog(dialogVM);
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
        Objects.requireNonNull(getComposer());
        if (getReport() == null) {
            getComposer().addReport(new ReportViewModel());
            var report = getReport();
            Objects.requireNonNull(report);
            report.refresh(persons);
            updateReportButtonText(true);
        } else {
            getComposer().removeReport();
            updateReportButtonText(false);
        }
    }

    void onClose() {
        var iterator = breadthFirstIterator();
        while (iterator.hasNext()) {
            iterator.next().requestDeinitialize();
        }
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
