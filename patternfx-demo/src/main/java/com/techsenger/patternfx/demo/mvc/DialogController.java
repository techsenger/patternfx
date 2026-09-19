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

package com.techsenger.patternfx.demo.mvc;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.core.ComponentParams;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.demo.model.PersonValidator;
import com.techsenger.patternfx.mvc.AbstractController;

/**
 *
 * @author Pavel Castornii
 */
public class DialogController<V extends DialogView> extends AbstractController<V> implements DialogPort {

    private static @Nullable Integer parseAge(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private @Nullable Person result;

    public DialogController(V view, ComponentParams params) {
        super(view, params);
    }

    @Override
    public @Nullable Person getResult() {
        return this.result;
    }

    protected boolean onOk() {
        var firstName = getView().getFirstNameTextField().getText();
        var lastName = getView().getLastNameTextField().getText();
        var age = parseAge(getView().getAgeTextField().getText());
        var person = new Person(firstName, lastName, age);
        var firstNameValid = PersonValidator.isFirstNameValid(person.getFirstName());
        var lastNameValid = PersonValidator.isLastNameValid(person.getLastName());
        var ageValid = PersonValidator.isAgeValid(person.getAge());
        getView().markFirstNameValid(firstNameValid);
        getView().markLastNameValid(lastNameValid);
        getView().markAgeValid(ageValid);
        if (firstNameValid && lastNameValid && ageValid) {
            this.result = person;
            return true;
        }
        return false;
    }
}
