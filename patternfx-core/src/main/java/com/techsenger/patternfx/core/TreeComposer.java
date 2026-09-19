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

/**
 * The root of the {@link PortTreeComposer}/{@link ViewTreeComposer} split, holding the one member both facets
 * share regardless of which node type they traverse.
 *
 * @author Pavel Castornii
 */
public interface TreeComposer {

    /**
     * Returns a string representation of this component and all its descendants as a sub-tree with this component
     * as root.
     *
     * @return a tree-formatted string representation of this component
     */
    String toTreeString();
}
