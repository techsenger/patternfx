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

/**
 *
 * @author Pavel Castornii
 */
public interface ChildViewModel<T extends Composer> extends ParentViewModel<T> {

    /**
     * Returns the property representing the ViewModel of the parent View. The property holds a reference to the
     * parent if this View is currently added as a child to another View, or {@code null} if it has no parent.
     *
     * @return the property containing the ViewModel of the parent View
     */
    ReadOnlyObjectProperty<ParentViewModel<?>> parentProperty();

    /**
     * Returns the value of {@link #parentProperty()}.
     *
     * @return the ViewModel of the parent View, or {@code null} if this View has no parent
     */
    ParentViewModel<?> getParent();
}
