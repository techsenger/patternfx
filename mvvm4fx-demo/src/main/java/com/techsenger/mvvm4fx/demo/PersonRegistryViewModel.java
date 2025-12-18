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

package com.techsenger.mvvm4fx.demo;

import com.techsenger.mvvm4fx.core.AbstractParentViewModel;
import com.techsenger.mvvm4fx.core.ComponentState;
import com.techsenger.mvvm4fx.core.ParentMediator;
import com.techsenger.mvvm4fx.demo.model.Person;
import com.techsenger.mvvm4fx.demo.model.PersonService;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public class PersonRegistryViewModel extends AbstractParentViewModel<ParentMediator> {

    private static final Logger logger = LoggerFactory.getLogger(PersonRegistryViewModel.class);

    private final PersonService service;

    private final StringProperty title = new SimpleStringProperty();

    private final ObservableList<Person> persons = FXCollections.observableArrayList();

    private final ObjectProperty<Person> selectedPerson = new SimpleObjectProperty<>();

    private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);

    public PersonRegistryViewModel(PersonService service) {
        this.service = service;
        title.bind(Bindings.size(persons).asString("Person Registry (%d Items)"));
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
            if (newV == ComponentState.INITIALIZED) {
                refresh();
            }
        });
    }

    ObservableList<Person> getPersons() {
        return persons;
    }

    StringProperty titleProperty() {
        return title;
    }

    ObjectProperty<Person> selectedPersonProperty() {
        return selectedPerson;
    }

    PersonDialogViewModel createDialog() {
        return new PersonDialogViewModel();
    }

    BooleanProperty removeDisabledProperty() {
        return this.removeDisabled;
    }

    void refresh() {
        persons.clear();
        persons.addAll(service.readAll());
    }

    void add(Optional<Person> person) {
        if (person.isPresent()) {
            var p = person.get();
            service.save(p);
            persons.add(p);
        }
    }

    void remove() {
        var id = selectedPerson.get().getId();
        service.delete(id);
        persons.removeIf(p -> p.getId().equals(id));
    }
}
