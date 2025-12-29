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

import com.techsenger.patternfx.core.TreeIterator;
import java.util.Iterator;
import java.util.function.BiConsumer;
import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public interface ParentViewModel<C extends Composer> extends ViewModel {

    C getComposer();

    /**
     * Returns an unmodifiable observable list of child ViewModels.
     *
     * @return a non-null, unmodifiable observable list of child ViewModels
     */
    ObservableList<ChildViewModel<?>> getChildren();

    /**
     * Returns an iterator that traverses the ViewModel subtree starting from this ViewModel in depth-first order.
     *
     * @return an {@link Iterator} that iterates over this ViewModel and all of its descendants
     */
    TreeIterator<ParentViewModel<?>> depthFirstIterator();

    /**
     * Returns an iterator that traverses the ViewModel subtree starting from this ViewModel in breadth-first order.
     *
     * @return an {@link Iterator} that iterates over this ViewModel and all of its descendants
     */
    TreeIterator<ParentViewModel<?>> breadthFirstIterator();

    /**
     * Returns a string representation of this ViewModel and all its descendants as a sub-tree with this
     * ViewModel as root.
     *
     * @return a tree-formatted string representation of this ViewModel
     */
    String toTreeString();

    /**
     * Returns a string representation of this ViewModel and all its descendants as a sub-tree with this ViewModel
     * as root, allowing the caller to customize the string output for each ViewModel.
     *
     * <p>The provided {@code appender} is invoked for each ViewModel and is responsible for appending the
     * complete string representation of that ViewModel to the given {@link StringBuilder}. The tree structure and
     * line separation are handled by this method.
     *
     * @param appender a callback used to append the full string representation of each ViewModel.
     * @return a tree-formatted string representation of this ViewModel
     */
    String toTreeString(BiConsumer<ParentViewModel<?>, StringBuilder> appender);

    /**
     * Deinitializes this ViewModel and all its descendants as a sub-tree with this ViewModel as the root.
     *
     * The deinitialization is performed using a breadth-first traversal: the parent ViewModel is deinitialized first,
     * followed by its children level by level.
     */
    void requestDeinitializeTree();
}
