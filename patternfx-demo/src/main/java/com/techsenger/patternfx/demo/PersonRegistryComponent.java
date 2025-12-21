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

package com.techsenger.patternfx.demo;

import com.techsenger.patternfx.core.AbstractParentComponent;
import com.techsenger.patternfx.core.ComponentName;

/**
 *
 * @author Pavel Castornii
 */
public class PersonRegistryComponent extends AbstractParentComponent<PersonRegistryView> {

    private final class Mediator extends AbstractParentComponent.Mediator implements PersonRegistryMediator {

        @Override
        public void openDialog(PersonDialogViewModel vm) {
            var dialogV = new PersonDialogView(vm, getView().getStage());
            var dialogC = new PersonDialogComponent(dialogV);
            dialogC.initialize();
            dialogC.getView().getDialog().showAndWait();
            dialogC.deinitialize();
        }
    }

    public PersonRegistryComponent(PersonRegistryView view) {
        super(view);
    }

    @Override
    public ComponentName getName() {
        return DemoComponentNames.PERSON_REGISTRY;
    }

    @Override
    protected Mediator createMediator() {
        return new Mediator();
    }
}
