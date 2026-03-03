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

    private String firstName;

    private String lastName;

    private Integer age;

    private Person result;

    public DialogPresenter(T view) {
        super(view);
    }

    @Override
    public Person getResult() {
        return DialogPresenter.this.result;
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
        var v = getView();
        this.result = new Person(firstName, lastName, age);
        if (!checkIfValid()) {
            this.result = null;
            return false;
        }
        return true;
    }

    private boolean checkIfValid() {
        var v = getView();
        var firstNameValid = PersonValidator.isFirstNameValid(this.result.getFirstName());
        v.setFirstNameValid(firstNameValid);
        var lastNameValid = PersonValidator.isLastNameValid(this.result.getLastName());
        v.setLastNameValid(lastNameValid);
        var ageValid = PersonValidator.isAgeValid(this.result.getAge());
        v.setAgeValid(ageValid);
        return firstNameValid && lastNameValid && ageValid;
    }

}
