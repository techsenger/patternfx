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
     * Returns the Composer that owns this component's child references (and, for a {@link ChildViewModel}, its
     * parent reference); this ViewModel holds none of them itself.
     */
    C getComposer();

    /**
     * Requests deinitialization of this ViewModel and its whole subtree, breadth-first: this ViewModel first, then
     * its children level by level.
     */
    void requestDeinitializeTree();
}
