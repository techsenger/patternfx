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

package com.techsenger.mvvm4fx.core;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractChildComponent<T extends AbstractChildView<?>> extends AbstractParentComponent<T>
        implements ChildComponent<T> {

    protected abstract class Mediator extends AbstractParentComponent.Mediator implements ChildMediator {

        private final ReadOnlyObjectWrapper<ParentViewModel> parent = new ReadOnlyObjectWrapper<>();

        public Mediator() {
            AbstractChildComponent.this.parent.addListener((ov, oldV, newV) -> {
                if (newV == null) {
                    this.parent.set(null);
                } else {
                    this.parent.set(newV.getView().getViewModel());
                }
            });
        }

        @Override
        public ReadOnlyObjectProperty<ParentViewModel> parentProperty() {
            return this.parent.getReadOnlyProperty();
        }

        @Override
        public ParentViewModel getParent() {
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

    /**
     * Sets the parent component for this component.
     * <p>
     * This method is normally called automatically when the component is added as a child to another component.
     * It can also be used explicitly when only the child-to-parent relationship needs to be established, without
     * adding the component to the parent's list of children.
     *
     * @param parent the parent component to set
     */
    protected void setParent(ParentComponent<?> parent) {
        this.parent.set(parent);
    }

    @Override
    protected abstract AbstractParentComponent.Mediator createMediator();
}
