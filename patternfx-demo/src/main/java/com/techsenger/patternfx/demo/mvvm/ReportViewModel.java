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

package com.techsenger.patternfx.demo.mvvm;

import com.techsenger.patternfx.demo.DemoNames;
import com.techsenger.patternfx.demo.model.Person;
import com.techsenger.patternfx.mvvm.AbstractLeafViewModel;
import com.techsenger.patternfx.mvvm.Descriptor;
import java.util.List;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

/**
 *
 * @author Pavel Castornii
 */
public class ReportViewModel extends AbstractLeafViewModel {

    private final ReadOnlyIntegerWrapper totalPeople = new ReadOnlyIntegerWrapper();

    private final ReadOnlyDoubleWrapper averageAge = new ReadOnlyDoubleWrapper();

    public ReportViewModel() {

    }

    public ReadOnlyIntegerProperty totalPeopleProperty() {
        return totalPeople.getReadOnlyProperty();
    }

    public int getTotalPeople() {
        return totalPeople.get();
    }

    public ReadOnlyDoubleProperty averageAgeProperty() {
        return averageAge.getReadOnlyProperty();
    }

    public double getAverageAge() {
        return averageAge.get();
    }

    @Override
    protected Descriptor createDescriptor() {
        return new Descriptor(DemoNames.PERSON_REPORT);
    }

    void setTotalPeople(int value) {
        totalPeople.set(value);
    }

    void setAverageAge(double value) {
        averageAge.set(value);
    }

    void refresh(List<Person> persons) {
        setTotalPeople(persons.size());
        double average = persons.stream().mapToDouble(Person::getAge).average().orElse(0.0);
        setAverageAge(average);
    }
}
