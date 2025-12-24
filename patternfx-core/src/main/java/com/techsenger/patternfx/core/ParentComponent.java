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

import java.util.function.BiConsumer;
import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public interface ParentComponent<T extends ParentView<?, ?>> extends Component<T> {

    /**
     * Returns an unmodifiable list of child components.
     *
     * @return an unmodifiable list of child components (never {@code null})
     */
    ObservableList<ChildComponent<?>> getChildren();

    /**
     * Returns an iterator that traverses the component subtree starting from this component in depth-first order.
     *
     * @return an {@link Iterator} that iterates over this component and all of its descendants
     */
    TreeIterator<ParentComponent<?>> depthFirstIterator();

    /**
     * Returns an iterator that traverses the component subtree starting from this component in breadth-first order.
     *
     * @return an {@link Iterator} that iterates over this component and all of its descendants
     */
    TreeIterator<ParentComponent<?>> breadthFirstIterator();

    /**
     * Returns a string representation of this component and all its descendants as a sub-tree with this
     * component as root.
     *
     * @return a tree-formatted string representation of this component
     */
    String toTreeString();

    /**
     * Returns a string representation of this component and all its descendants as a sub-tree with this component
     * as root, allowing the caller to customize the string output for each component.
     *
     * The provided {@code componentAppender} is invoked for each component and is responsible for appending the
     * complete string representation of that component to the given {@link StringBuilder}. The tree structure and
     * line separation are handled by this method.
     *
     * @param componentAppender a callback used to append the full string representation of each component.
     * @return a tree-formatted string representation of this component
     */
    String toTreeString(BiConsumer<ParentComponent<?>, StringBuilder> componentAppender);
}
