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
import com.techsenger.patternfx.mvp.AbstractChildPresenter;
import com.techsenger.patternfx.mvp.ChildComposer;
import com.techsenger.patternfx.mvp.Descriptor;
import java.util.List;

/**
 *
 * @author Pavel Castornii
 */
public class ReportPresenter extends AbstractChildPresenter<ReportView, ChildComposer> {

    private final class Port extends AbstractChildPresenter.Port implements ReportPort {

        @Override
        public void refresh(List<Person> persons) {
            ReportPresenter.this.refresh(persons);
        }

    }

    public ReportPresenter(ReportView view) {
        super(view);
    }

    @Override
    protected Descriptor createDescriptor() {
        return new Descriptor(DemoNames.PERSON_REPORT);
    }

    @Override
    public Port getPort() {
        return (Port) super.getPort();
    }

    @Override
    protected Port createPort() {
        return new ReportPresenter.Port();
    }

    private void refresh(List<Person> persons) {
        double average = persons.stream().mapToDouble(Person::getAge).average().orElse(0.0);
        getView().setAverageAge(String.valueOf(average));
        getView().setTotalPeople(String.valueOf(persons.size()));
    }
}
