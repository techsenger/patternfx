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

package com.techsenger.patternfx.core;

import java.util.function.BiConsumer;

/**
 * Traverses a component subtree at the {@link ParentPort} level.
 *
 * @author Pavel Castornii
 */
public interface PortTreeComposer extends TreeComposer {

    /**
     * Returns an iterator that traverses the component subtree starting from this component in depth-first order,
     * exposing each visited component through its {@link ParentPort}.
     *
     * @return an {@link Iterator} that iterates over the Ports of this component and all of its descendants
     */
    TreeIterator<ParentPort> depthFirstPortIterator();

    /**
     * Returns an iterator that traverses the component subtree starting from this component in breadth-first
     * order, exposing each visited component through its {@link ParentPort}.
     *
     * @return an {@link Iterator} that iterates over the Ports of this component and all of its descendants
     */
    TreeIterator<ParentPort> breadthFirstPortIterator();

    /**
     * Returns a tree-formatted string representation of this component's subtree, rooted at this component,
     * rendered via each component's {@link ParentPort}.
     *
     * <p>The given {@code appender} renders each component's Port into the shared {@link StringBuilder}; this
     * method handles the tree structure and line breaks.
     *
     * @param appender renders a single component's Port into the shared builder
     * @return a tree-formatted string representation of this component's subtree
     */
    String toPortTreeString(BiConsumer<ParentPort, StringBuilder> appender);
}
