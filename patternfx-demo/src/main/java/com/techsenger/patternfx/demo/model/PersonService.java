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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pavel Castornii
 */
public class PersonService {

    private int idCounter = 1;

    private final List<Person> persons = new ArrayList<>();

    public PersonService() {
        save(new Person("John", "Smith", 25));
        save(new Person("Mike", "Brown", 40));
        save(new Person("Sarah", "Wilson", 34));
    }

    public List<Person> readAll() {
         return new ArrayList<>(persons);
    }

    public void save(Person person) {
        person.setId(idCounter++);
        persons.add(person);
    }

    public void delete(int id) {
        persons.removeIf(p -> p.getId() == id);
    }
}
