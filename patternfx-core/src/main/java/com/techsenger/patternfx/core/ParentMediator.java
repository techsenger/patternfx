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

import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public interface ParentMediator extends ComponentMediator {

    /**
     * Returns an unmodifiable observable list of child ViewModels.
     *
     * @return a non-null, unmodifiable observable list of child ViewModels
     */
    ObservableList<ChildViewModel<?>> getChildren();

    /**
     * Returns an iterator that traverses the component subtree starting from this component in depth-first order.
     *
     * @return an {@link Iterator} that iterates over this component and all of its descendants
     */
    SubtreeIterator<ParentViewModel<?>> depthFirstIterator();

    /**
     * Returns an iterator that traverses the component subtree starting from this component in breadth-first order.
     *
     * @return an {@link Iterator} that iterates over this component and all of its descendants
     */
    SubtreeIterator<ParentViewModel<?>> breadthFirstIterator();
}
