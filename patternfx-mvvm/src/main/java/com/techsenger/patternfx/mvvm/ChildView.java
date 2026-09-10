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

package com.techsenger.patternfx.mvvm;

import com.techsenger.annotations.Nullable;
import javafx.beans.property.ReadOnlyObjectProperty;

/**
 *
 * @author Pavel Castornii
 */
public interface ChildView<VM extends ChildViewModel<?>> extends ParentView<VM> {

    interface Composer extends ParentView.Composer, ChildComposer {

        /**
         * Returns the property representing the parent View of this View. The property holds a reference to the
         * parent if this View is currently added as a child to another View, or {@code null} if it has no parent.
         *
         * @return the property containing the parent View
         */
        ReadOnlyObjectProperty<? extends ParentView<?>> parentProperty();

        /**
         * Returns the value of {@link #parentProperty()}.
         *
         * @return the parent View, or {@code null} if this View has no parent
         */
        @Nullable ParentView<?> getParent();

        /**
         * Sets the parent View of this View.
         *
         * <p>Framework contract: This method is intended to be called exclusively by {@link Composer}
         * implementations while managing the View hierarchy. Direct invocation by user code results in undefined
         * behavior.
         */
        void setParent(@Nullable ParentView<?> parent);
    }

    @Override
    Composer getComposer();

    /**
     * Requests focus. Child view implements this method via selecting FX node that will request focus.
     */
    void requestFocus();

    /**
     * Returns the main node of the view. It can be Tab, Node etc.
     */
    Object getNode();
}
