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
import com.techsenger.patternfx.mvp.AbstractPresenter;
import com.techsenger.patternfx.mvp.Descriptor;

/**
 *
 * @author Pavel Castornii
 */
public class DialogPresenter<T extends DialogView> extends AbstractPresenter<T> {

    protected class Port extends AbstractPresenter.Port implements DialogPort {

        @Override
        public Person getResult() {
            return DialogPresenter.this.result;
        }
    }

    private Person result;

    public DialogPresenter(T view) {
        super(view);
    }

    @Override
    public Port getPort() {
        return (Port) super.getPort();
    }

    @Override
    protected Descriptor createDescriptor() {
        return new Descriptor(DemoNames.PERSON_DIALOG);
    }

    protected boolean handleOkAction() {
        var v = getView();
        this.result = new Person(v.getFirstName(), v.getLastName(), Integer.valueOf(v.getAge()));
        if (!checkIfValid()) {
            this.result = null;
            return false;
        }
        return true;
    }

    protected Person getResult() {
        return result;
    }

    @Override
    protected Port createPort() {
        return new DialogPresenter.Port();
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
