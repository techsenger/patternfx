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

package com.techsenger.patternfx.mvc;

import com.techsenger.annotations.Unmodifiable;
import com.techsenger.patternfx.core.ParentPort;
import com.techsenger.patternfx.core.PortTreeComposer;
import com.techsenger.patternfx.core.ViewTreeComposer;
import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public interface ParentView<C extends ParentController<?>> extends View<C> {

    interface Composer extends PortTreeComposer, ViewTreeComposer<ParentView<?>>, ParentPort.ComposerAccess {

        /**
         * Creates and adds this component's default child components; invoked automatically and exactly once,
         * from {@code postInitialize()}, right after the component's core initialization phase completes.
         */
        void compose();

        /**
         * Returns an unmodifiable observable list of child Views.
         *
         * @return a non-null, unmodifiable observable list of child Views
         */
        @Unmodifiable ObservableList<? extends ChildView<?>> getChildren();
    }

    /**
     * Returns the composer of this view; all references to this component's children are owned by the composer,
     * not by this view.
     */
    Composer getComposer();
}
