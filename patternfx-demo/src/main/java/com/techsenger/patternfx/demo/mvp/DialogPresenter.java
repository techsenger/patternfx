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

package com.techsenger.patternfx.demo.mvp;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.demo.DemoNames;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.demo.model.PersonValidator;
import com.techsenger.patternfx.mvp.AbstractParentPresenter;
import com.techsenger.patternfx.mvp.Descriptor;
import com.techsenger.patternfx.mvp.ParentComposer;

/**
 *
 * @author Pavel Castornii
 */
public class DialogPresenter<T extends DialogView> extends AbstractParentPresenter<T, ParentComposer>
        implements DialogPort {

    private @Nullable String firstName;

    private @Nullable String lastName;

    private @Nullable Integer age;

    private @Nullable Person result;

    public DialogPresenter(T view) {
        super(view);
    }

    @Override
    public @Nullable Person getResult() {
        return this.result;
    }

    @Override
    protected Descriptor createDescriptor() {
        return new Descriptor(DemoNames.PERSON_DIALOG);
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

    private boolean checkIfValid(Person person) {
        var v = getView();

        var firstNameValid = PersonValidator.isFirstNameValid(person.getFirstName());
        v.setFirstNameValid(firstNameValid);
        var lastNameValid = PersonValidator.isLastNameValid(person.getLastName());
        v.setLastNameValid(lastNameValid);
        var ageValid = PersonValidator.isAgeValid(person.getAge());
        v.setAgeValid(ageValid);
        return firstNameValid && lastNameValid && ageValid;
    }

}
