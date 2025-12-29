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

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractChildViewModel<C extends Composer> extends AbstractParentViewModel<C>
        implements ChildViewModel<C> {

    private final ReadOnlyObjectWrapper<ParentViewModel<?>> parent = new ReadOnlyObjectWrapper<>();

    public AbstractChildViewModel() {
        super();
    }

    @Override
    public ReadOnlyObjectProperty<ParentViewModel<?>> parentProperty() {
        return this.parent.getReadOnlyProperty();
    }

    @Override
    public ParentViewModel<?> getParent() {
        return this.parent.get();
    }

    void setParent(ParentViewModel<?> parent) {
        this.parent.set(parent);
    }

}
