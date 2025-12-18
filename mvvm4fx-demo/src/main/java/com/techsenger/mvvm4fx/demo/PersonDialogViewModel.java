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
import com.techsenger.mvvm4fx.core.ParentMediator;
import com.techsenger.mvvm4fx.demo.model.Person;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Pavel Castornii
 */
public class PersonDialogViewModel extends AbstractParentViewModel<ParentMediator> {

    private final StringProperty title = new SimpleStringProperty("New Person");

    private final Person person = new Person();

    private final BooleanProperty firstNameValid = new SimpleBooleanProperty(true);

    private final BooleanProperty lastNameValid = new SimpleBooleanProperty(true);

    private final BooleanProperty ageValid = new SimpleBooleanProperty(true);

    StringProperty titleProperty() {
        return title;
    }

    Person getPerson() {
        return person;
    }

    BooleanProperty firstNameValidProperty() {
        return firstNameValid;
    }

    BooleanProperty lastNameValidProperty() {
        return lastNameValid;
    }

    BooleanProperty ageValidProperty() {
        return ageValid;
    }

    boolean isPersonValid() {
        firstNameValid.set(person.isFirstNameValid());
        lastNameValid.set(person.isLastNameValid());
        ageValid.set(person.isAgeValid());
        return firstNameValid.get() && lastNameValid.get() && ageValid.get();
    }

    Person createPerson() {
        var newPerson = new Person(person.getFirstName(), person.getLastName(), person.getAge());
        return newPerson;
    }
}
