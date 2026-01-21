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

package com.techsenger.patternfx.mvp;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 *
 * @author Pavel Castornii
 */
public interface ChildJfxView<P extends ChildPresenter> extends ParentFxView<P>, ChildView {

    /**
     * Returns the property representing the parent view of this view. The property holds a reference to the
     * parent if this view is currently added as a child to another view, or {@code null} if it has no parent.
     *
     * @return the property containing the parent view
     */
    ReadOnlyObjectProperty<? extends ParentFxView<?>> parentProperty();

    /**
     * Returns the value of {@link #parentProperty()}.
     *
     * @return the parent view, or {@code null} if this view has no parent
     */
     ParentFxView<?> getParent();

    /**
     * Returns the value of {@link #parentProperty()} cast to the specified type.
     *
     * @param <T> the expected type of the parent view
     * @param parentClass the class object representing the expected parent type
     * @return the parent view cast to the specified type, or {@code null} if this view has no parent
     * @throws ClassCastException if the parent exists but is not of the specified type
     */
    <T extends ParentFxView<?>> T getParent(Class<T> parentClass);

    /**
     * Sets the parent view of this view.
     *
     * <p>Framework contract: This method is intended to be called exclusively by {@link ParentComposer}
     * implementations while managing the view hierarchy. Direct invocation by user code results in undefined
     * behavior.
     */
    void setParent(ParentFxView<?> parent);

    /**
     * Returns the main node of the view. It can be Tab, Node etc.
     *
     * @return
     */
    Object getNode();
}
