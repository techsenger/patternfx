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

package com.techsenger.patternfx.core;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractChildComponent<T extends AbstractChildView<?, ?>> extends AbstractParentComponent<T>
        implements ChildComponent<T> {

    protected class Mediator extends AbstractParentComponent.Mediator implements ChildMediator {

        private final ReadOnlyObjectWrapper<ParentViewModel<?>> parent = new ReadOnlyObjectWrapper<>();

        private final AbstractChildComponent<?> component = AbstractChildComponent.this;

        public Mediator() {
            this.parent.bind(
                component.parentProperty().map(p -> {
                    if (p != null) {
                        return p.getView().getViewModel();
                    } else {
                        return null;
                    }
                })
            );
        }

        @Override
        public ReadOnlyObjectProperty<ParentViewModel<?>> parentProperty() {
            return this.parent.getReadOnlyProperty();
        }

        @Override
        public ParentViewModel<?> getParent() {
            return this.parent.get();
        }
    }

    private final ReadOnlyObjectWrapper<ParentComponent<?>> parent = new ReadOnlyObjectWrapper<>();

    public AbstractChildComponent(T view) {
        super(view);
    }

    @Override
    public ReadOnlyObjectProperty<ParentComponent<?>> parentProperty() {
        return this.parent.getReadOnlyProperty();
    }

    @Override
    public ParentComponent<?> getParent() {
        return this.parent.get();
    }

    @Override
    public <T extends ParentComponent<?>> T getParent(Class<T> parentClass) {
        return (T) getParent();
    }

    @Override
    public void setParent(ParentComponent<?> parent) {
        this.parent.set(parent);
    }

    @Override
    protected abstract AbstractParentComponent.Mediator createMediator();
}
