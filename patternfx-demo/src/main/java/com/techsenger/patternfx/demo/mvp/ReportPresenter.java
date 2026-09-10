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
import com.techsenger.patternfx.mvp.AbstractChildPresenter;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Pavel Castornii
 */
public class ReportPresenter extends AbstractChildPresenter<ReportView> implements ReportPort {

    private @Nullable String totalPeople;

    private @Nullable String averageAge;

    public ReportPresenter(ReportView view, ComponentParams params) {
        super(view, params);
    }

    @Override
    public void refresh(List<Person> persons) {
        double average = persons.stream().mapToDouble(Person::getAge).average().orElse(0.0);
        setAverageAge(String.valueOf(average));
        setTotalPeople(String.valueOf(persons.size()));
    }

    public @Nullable String getTotalPeople() {
        return this.totalPeople;
    }

    public @Nullable String getAverageAge() {
        return this.averageAge;
    }

    protected void setTotalPeople(String totalPeople) {
        if (Objects.equals(this.totalPeople, totalPeople)) {
            return;
        }
        this.totalPeople = totalPeople;
        getView().updateTotalPeople(totalPeople);
    }

    protected void setAverageAge(String averageAge) {
        if (Objects.equals(this.averageAge, averageAge)) {
            return;
        }
        this.averageAge = averageAge;
        getView().updateAverageAge(averageAge);
    }
}
