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

import com.techsenger.patternfx.core.TreeIterator;
import java.util.function.BiConsumer;
import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public interface ParentJfxView<P extends ParentPresenter> extends JfxView<P>, ParentView {

    /**
     * Returns an unmodifiable list of child Views.
     *
     * @return an unmodifiable list of child Views (never {@code null})
     */
    ObservableList<ChildJfxView<?>> getChildren();

    /**
     * Returns an iterator that traverses the View subtree starting from this View in depth-first order.
     *
     * @return an {@link Iterator} that iterates over this View and all of its descendants
     */
    TreeIterator<ParentJfxView<?>> depthFirstIterator();

    /**
     * Returns an iterator that traverses the View subtree starting from this View in breadth-first order.
     *
     * @return an {@link Iterator} that iterates over this View and all of its descendants
     */
    TreeIterator<ParentJfxView<?>> breadthFirstIterator();

    /**
     * Returns a string representation of this View and all its descendants as a sub-tree with this
     * View as root.
     *
     * @return a tree-formatted string representation of this View
     */
    String toTreeString();

    /**
     * Returns a string representation of this View and all its descendants as a sub-tree with this View
     * as root, allowing the caller to customize the string output for each View.
     *
     * The provided {@code appender} is invoked for each View and is responsible for appending the
     * complete string representation of that View to the given {@link StringBuilder}. The tree structure and
     * line separation are handled by this method.
     *
     * @param appender a callback used to append the full string representation of each View.
     * @return a tree-formatted string representation of this View
     */
    String toTreeString(BiConsumer<ParentJfxView<?>, StringBuilder> appender);
}
