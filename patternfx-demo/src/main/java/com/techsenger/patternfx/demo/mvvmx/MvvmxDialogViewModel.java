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

import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.demo.model.PersonValidator;
import com.techsenger.patternfx.mvvmx.AbstractComponentViewModel;
import com.techsenger.patternfx.mvvmx.ComponentMediator;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Pavel Castornii
 */
public class MvvmxDialogViewModel extends AbstractComponentViewModel<ComponentMediator> {

    private final StringProperty title = new SimpleStringProperty("New Person");

    private final StringProperty firstName = new SimpleStringProperty();

    private final StringProperty lastName = new SimpleStringProperty();

    private final ObjectProperty<Integer> age = new SimpleObjectProperty();

    private final BooleanProperty firstNameValid = new SimpleBooleanProperty(true);

    private final BooleanProperty lastNameValid = new SimpleBooleanProperty(true);

    private final BooleanProperty ageValid = new SimpleBooleanProperty(true);

    private final Consumer<Person> resultCallback;

    public MvvmxDialogViewModel(Consumer<Person> resultCallback) {
        this.resultCallback = resultCallback;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public Integer getAge() {
        return age.get();
    }

    public void setAge(Integer age) {
        this.age.set(age);
    }

    public ObjectProperty<Integer> ageProperty() {
        return age;
    }

    StringProperty titleProperty() {
        return title;
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

    boolean addNewPerson() {
        if (isPersonValid()) {
            var newPerson = new Person(getFirstName(), getLastName(), getAge());
            this.resultCallback.accept(newPerson);
            return true;
        }
        return false;
    }

    private boolean isPersonValid() {
        firstNameValid.set(PersonValidator.isFirstNameValid(getFirstName()));
        lastNameValid.set(PersonValidator.isLastNameValid(getLastName()));
        ageValid.set(PersonValidator.isAgeValid(getAge()));
        return firstNameValid.get() && lastNameValid.get() && ageValid.get();
    }
}
