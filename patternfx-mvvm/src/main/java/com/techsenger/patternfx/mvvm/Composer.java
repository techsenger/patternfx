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
import com.techsenger.patternfx.core.PortTreeComposer;

/**
 * The Port-level facet of a component's Composer &mdash; the type a {@link ParentViewModel} is parametrized with.
 * It never references View types, so a ViewModel holding a {@code Composer} gains no access to the View tree; the
 * View-typed facet (e.g. {@link ParentView.Composer}) is a separate, richer interface implemented by the same
 * concrete Composer instance.
 *
 * @author Pavel Castornii
 */
public interface Composer extends PortTreeComposer, ParentPort.ComposerAccess {

    /**
     * Creates and adds this component's default child components; invoked automatically and exactly once, from
     * {@code postInitialize()}, right after the component's core initialization phase completes.
     */
    void compose();
}
