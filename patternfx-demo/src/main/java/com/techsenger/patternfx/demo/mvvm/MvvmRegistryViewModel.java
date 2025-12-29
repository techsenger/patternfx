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

import com.techsenger.patternfx.core.State;
import com.techsenger.patternfx.demo.DemoNames;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.demo.model.PersonService;
import com.techsenger.patternfx.mvvm.AbstractParentViewModel;
import com.techsenger.patternfx.mvvm.Descriptor;
import java.util.Iterator;
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
public class MvvmRegistryViewModel extends AbstractParentViewModel<MvvmRegistryComposer> {

    private static final Logger logger = LoggerFactory.getLogger(MvvmRegistryViewModel.class);

    private final PersonService service;

    private final ObservableList<Person> persons = FXCollections.observableArrayList();

    private final ObjectProperty<Person> selectedPerson = new SimpleObjectProperty<>();

    private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);

    private final ReadOnlyStringWrapper reportButtonText = new ReadOnlyStringWrapper();

    private final ReadOnlyObjectWrapper<MvvmReportViewModel> report = new ReadOnlyObjectWrapper<>();

    public MvvmRegistryViewModel(PersonService service) {
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
            if (newV == State.INITIALIZED) {
                refresh();
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

    ReadOnlyObjectProperty<MvvmReportViewModel> reportProperty() {
        return report.getReadOnlyProperty();
    }

    String getReportButtonText() {
        return reportButtonText.get();
    }

    MvvmReportViewModel getReport() {
        return report.get();
    }

    ReadOnlyObjectWrapper<MvvmReportViewModel> getReportWrapper() {
        return report;
    }

    void refresh() {
        persons.clear();
        persons.addAll(service.readAll());
    }

    void add() {
        var dialogVM = new MvvmDialogViewModel((p) -> add(p));
        getComposer().openDialog(dialogVM);
    }

    void remove() {
        var id = selectedPerson.get().getId();
        service.delete(id);
        for (Iterator<Person> it = persons.iterator(); it.hasNext();) {
            Person p = it.next();
            if (p.getId() == id) {
                it.remove();
                break;
            }
        }
    }

    void toggleReport() {
        if (getReport() == null) {
            getComposer().addReport(new MvvmReportViewModel());
            getReport().refresh(persons);
            updateReportButtonText(true);
        } else {
            getComposer().removeReport();
            updateReportButtonText(false);
        }
    }

    void close() {
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
