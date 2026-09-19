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

package com.techsenger.patternfx.demo.mvp;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.core.ComponentParams;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.demo.model.PersonValidator;
import com.techsenger.patternfx.mvp.AbstractPresenter;

/**
 *
 * @author Pavel Castornii
 */
public class DialogPresenter<V extends DialogView> extends AbstractPresenter<V> implements DialogPort {

    private @Nullable String firstName;

    private @Nullable String lastName;

    private @Nullable Integer age;

    private @Nullable Person result;

    private boolean firstNameValid = true;

    private boolean lastNameValid = true;

    private boolean ageValid = true;

    public DialogPresenter(V view, ComponentParams params) {
        super(view, params);
    }

    @Override
    public @Nullable Person getResult() {
        return this.result;
    }

    public boolean isFirstNameValid() {
        return this.firstNameValid;
    }

    public boolean isLastNameValid() {
        return this.lastNameValid;
    }

    public boolean isAgeValid() {
        return this.ageValid;
    }

    protected void onFirstNameChanged(String value) {
        this.firstName = value;
    }

    protected void onLastNameChanged(String value) {
        this.lastName = value;
    }

    protected void onAgeChanged(String value) {
        this.age = Integer.valueOf(value);
    }

    protected boolean onOk() {
        var person = new Person(firstName, lastName, age);
        if (checkIfValid(person)) {
            this.result = person;
            return true;
        } else {
            return false;
        }
    }

    protected void setFirstNameValid(boolean firstNameValid) {
        if (this.firstNameValid == firstNameValid) {
            return;
        }
        this.firstNameValid = firstNameValid;
        getView().updateFirstNameValid(firstNameValid);
    }

    protected void setLastNameValid(boolean lastNameValid) {
        if (this.lastNameValid == lastNameValid) {
            return;
        }
        this.lastNameValid = lastNameValid;
        getView().updateLastNameValid(lastNameValid);
    }

    protected void setAgeValid(boolean ageValid) {
        if (this.ageValid == ageValid) {
            return;
        }
        this.ageValid = ageValid;
        getView().updateAgeValid(ageValid);
    }

    private boolean checkIfValid(Person person) {
        setFirstNameValid(PersonValidator.isFirstNameValid(person.getFirstName()));
        setLastNameValid(PersonValidator.isLastNameValid(person.getLastName()));
        setAgeValid(PersonValidator.isAgeValid(person.getAge()));
        return isFirstNameValid() && isLastNameValid() && isAgeValid();
    }
}
