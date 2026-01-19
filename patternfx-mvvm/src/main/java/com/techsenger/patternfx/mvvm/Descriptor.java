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

package com.techsenger.patternfx.mvvm;

import com.techsenger.patternfx.core.AbstractObservableDescriptor;
import com.techsenger.patternfx.core.Group;
import com.techsenger.patternfx.core.Name;
import com.techsenger.patternfx.core.State;
import java.util.UUID;
import javafx.beans.property.ObjectProperty;

/**
 *
 * @author Pavel Castornii
 */
public final class Descriptor extends AbstractObservableDescriptor {

    public Descriptor(Name name) {
        super(name);
    }

    public Descriptor(Name name, UUID uuid) {
        super(name, uuid);
    }

    @Override
    public ObjectProperty<Group> groupProperty() {
        return getGroupWrapper();
    }

    @Override
    public void setGroup(Group group) {
        super.setGroup(group);
    }

    @Override
    protected void setState(State state) {
        super.setState(state);
    }

}
