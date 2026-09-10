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

import com.techsenger.patternfx.core.ParentPort;

/**
 *
 * @author Pavel Castornii
 */
public interface ParentViewModel<C extends Composer> extends ViewModel, ParentPort {

    /**
     * Returns the Composer of this ViewModel's View. All references to this component's children and, for a
     * {@link ChildViewModel}, its parent are owned by the Composer &mdash; this ViewModel holds none of them
     * itself.
     */
    C getComposer();

    /**
     * Deinitializes this ViewModel and all its descendants as a sub-tree with this ViewModel as the root.
     *
     * The deinitialization is performed using a breadth-first traversal: the parent ViewModel is deinitialized first,
     * followed by its children level by level.
     */
    void requestDeinitializeTree();
}
