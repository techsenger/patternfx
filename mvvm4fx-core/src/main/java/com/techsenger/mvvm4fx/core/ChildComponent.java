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

/**
 *
 * @author Pavel Castornii
 */
public interface ChildComponent<T extends ChildView<?>> extends ParentComponent<T> {

    /**
     * Returns the property representing the parent component of this component. The property holds a reference to the
     * parent if this component is currently added as a child to another component, or {@code null} if it has no parent.
     *
     * @return the property containing the parent component
     */
    ReadOnlyObjectProperty<? extends ParentComponent<?>> parentProperty();

    /**
     * Returns the value of {@link #parentProperty()}.
     *
     * @return the parent component, or {@code null} if this component has no parent
     */
    ParentComponent<?> getParent();

    /**
     * Returns the value of {@link #parentProperty()} cast to the specified type.
     *
     * @param <T> the expected type of the parent component
     * @param parentClass the class object representing the expected parent type
     * @return the parent component cast to the specified type, or {@code null} if this component has no parent
     * @throws ClassCastException if the parent exists but is not of the specified type
     */
    <T extends ParentComponent<?>> T getParent(Class<T> parentClass);

    /**
     * Sets the parent component of this component.
     *
     * <p>Framework contract: This method is intended to be called exclusively by {@link ParentComponent}
     * implementations while managing the component hierarchy. Direct invocation by user code results in undefined
     * behavior.
     */
    void setParent(ParentComponent<?> parent);
}
