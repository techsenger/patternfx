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

package com.techsenger.patternfx.demo.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Pavel Castornii
 */
public class Person {

    private final ObjectProperty<Integer> id = new SimpleObjectProperty<>();

    private final StringProperty firstName = new SimpleStringProperty();

    private final StringProperty lastName = new SimpleStringProperty();

    private final ObjectProperty<Integer> age = new SimpleObjectProperty<>();

    public Person() {

    }

    public Person(String firstName, String lastName, Integer age) {
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer value) {
        id.set(value);
    }

    public ObjectProperty<Integer> idProperty() {
        return id;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public boolean isFirstNameValid() {
        return firstName.get() != null && !firstName.get().isBlank();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public boolean isLastNameValid() {
        return lastName.get() != null && !lastName.get().isBlank();
    }

    public ObjectProperty<Integer> ageProperty() {
        return age;
    }

    public Integer getAge() {
        return age.get();
    }

    public void setAge(Integer age) {
        this.age.set(age);
    }

    public boolean isAgeValid() {
        return age.get() != null && age.get() >= 0 && age.get() <= 125;
    }
}
