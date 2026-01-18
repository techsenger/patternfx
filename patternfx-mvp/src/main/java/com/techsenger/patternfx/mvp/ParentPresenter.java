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

/**
 *
 * @author Pavel Castornii
 */
public interface ParentPresenter extends Presenter {

    /**
     * Deinitializes this component and all its descendants as a sub-tree with this component as the root.
     *
     * The deinitialization is performed using a breadth-first traversal: the parent component is deinitialized first,
     * followed by its children level by level.
     */
    void deinitializeTree();
}
