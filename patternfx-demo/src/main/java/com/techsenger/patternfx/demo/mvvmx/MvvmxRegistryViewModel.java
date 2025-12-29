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

package com.techsenger.patternfx.demo.mvvmx;

import com.techsenger.patternfx.core.State;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.demo.model.PersonService;
import com.techsenger.patternfx.mvvmx.AbstractParentViewModel;
import java.util.Iterator;
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
public class MvvmxRegistryViewModel extends AbstractParentViewModel<MvvmxRegistryMediator> {

    private static final Logger logger = LoggerFactory.getLogger(MvvmxRegistryViewModel.class);

    private final PersonService service;

    private final ObservableList<Person> persons = FXCollections.observableArrayList();

    private final ObjectProperty<Person> selectedPerson = new SimpleObjectProperty<>();

    private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);

    private final ReadOnlyStringWrapper reportButtonText = new ReadOnlyStringWrapper();

    public MvvmxRegistryViewModel(PersonService service) {
        this.service = service;
    }

    @Override
    protected void initialize() {
        super.initialize();
        selectedPerson.addListener((ov, oldV, newV) -> {
            removeDisabled.set(newV == null);
            // log message example with component meta
            logger.debug("{} Selected person property changed", getMediator().getLogPrefix());
        });
        getMediator().stateProperty().addListener((ov, oldV, newV) -> {
            if (newV == State.INITIALIZED) {
                refresh();
            }
        });
        persons.addListener((ListChangeListener<Person>) e -> {
            var r = getMediator().getReport();
            if (r != null) {
                r.refresh(persons);
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

    String getReportButtonText() {
        return reportButtonText.get();
    }

    ReadOnlyStringProperty reportButtonTextProperty() {
        return reportButtonText.getReadOnlyProperty();
    }

    void refresh() {
        persons.clear();
        persons.addAll(service.readAll());
    }

    void add() {
        var dialogVM = new MvvmxDialogViewModel((p) -> add(p));
        getMediator().openDialog(dialogVM);
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
        if (getMediator().getReport() == null) {
            getMediator().addReport(new MvvmxReportViewModel());
            getMediator().getReport().refresh(persons);
            updateReportButtonText(true);
        } else {
            getMediator().removeReport();
            updateReportButtonText(false);
        }
    }

    void close() {
        var iterator = getMediator().breadthFirstIterator();
        while (iterator.hasNext()) {
            iterator.next().getMediator().deinitialize();
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
