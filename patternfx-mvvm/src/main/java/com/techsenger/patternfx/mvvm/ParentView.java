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

import com.techsenger.annotations.Unmodifiable;
import com.techsenger.patternfx.core.TreeIterator;
import java.util.function.BiConsumer;
import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public interface ParentView<VM extends ParentViewModel<?>> extends View<VM> {

    interface Composer extends com.techsenger.patternfx.mvvm.Composer {

        /**
         * Returns an unmodifiable observable list of child Views.
         *
         * @return a non-null, unmodifiable observable list of child Views
         */
        @Unmodifiable ObservableList<? extends ChildView<?>> getChildren();

        /**
         * Returns an iterator that traverses the View subtree starting from this View in depth-first order.
         *
         * @return an {@link Iterator} that iterates over this View and all of its descendants
         */
        TreeIterator<ParentView<?>> depthFirstIterator();

        /**
         * Returns an iterator that traverses the View subtree starting from this View in breadth-first order.
         *
         * @return an {@link Iterator} that iterates over this View and all of its descendants
         */
        TreeIterator<ParentView<?>> breadthFirstIterator();

        /**
         * Returns a tree-formatted string representation of this View's subtree, rooted at this View.
         *
         * <p>The given {@code appender} renders each View into the shared {@link StringBuilder}; this method
         * handles the tree structure and line breaks.
         *
         * @param appender renders a single View's line into the shared builder
         * @return a tree-formatted string representation of this View's subtree
         */
        String toTreeString(BiConsumer<ParentView<?>, StringBuilder> appender);
    }

    /**
     * Returns the Composer of this View; all references to this component's children are owned by the Composer,
     * not by this View.
     */
    Composer getComposer();

    /**
     * Deinitializes this View and its whole subtree, breadth-first: this View first, then its children level by
     * level.
     */
    void deinitializeTree();
}
