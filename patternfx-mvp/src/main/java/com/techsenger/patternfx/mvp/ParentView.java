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

package com.techsenger.patternfx.mvp;

import com.techsenger.annotations.Unmodifiable;
import com.techsenger.patternfx.core.ChildPort;
import com.techsenger.patternfx.core.ParentPort;
import com.techsenger.patternfx.core.TreeIterator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 *
 * @author Pavel Castornii
 */
public interface ParentView extends View {

    interface Composer {

        /**
         * Creates and adds this component's default child components; invoked once, automatically, near the end
         * of initialization.
         */
        void compose();

        /**
         * Returns an unmodifiable list of child Ports.
         *
         * @return a non-null, unmodifiable list of child Ports
         */
        @Unmodifiable List<? extends ChildPort> getChildPorts();

        /**
         * Returns an iterator that traverses the component subtree starting from this component in depth-first order.
         *
         * @return an {@link Iterator} that iterates over this component and all of its descendants
         */
        TreeIterator<ParentPort> depthFirstPortIterator();

        /**
         * Returns an iterator that traverses the component subtree starting from this component in breadth-first
         * order.
         *
         * @return an {@link Iterator} that iterates over this component and all of its descendants
         */
        TreeIterator<ParentPort> breadthFirstPortIterator();

        /**
         * Returns a string representation of this component and all its descendants as a sub-tree with this
         * component as root.
         *
         * @return a tree-formatted string representation of this component
         */
        String toTreeString();

        /**
         * Returns a tree-formatted string representation of this component's subtree, rooted at this component.
         *
         * <p>The given {@code appender} renders each component into the shared {@link StringBuilder}; this method
         * handles the tree structure and line breaks.
         *
         * @param appender renders a single component's line into the shared builder
         * @return a tree-formatted string representation of this component's subtree
         */
        String toPortTreeString(BiConsumer<ParentPort, StringBuilder> appender);
    }

    /**
     * Returns the composer of this view; all references to this component's children are owned by the composer,
     * not by this view.
     */
    Composer getComposer();

    /**
     * Requests focus. Child view implements this method via selecting FX node that will request focus.
     */
    void requestFocus();
}
