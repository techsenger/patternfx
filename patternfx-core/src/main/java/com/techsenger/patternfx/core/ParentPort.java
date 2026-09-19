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

import com.techsenger.annotations.Unmodifiable;
import java.util.List;

/**
 *
 * @author Pavel Castornii
 */
public interface ParentPort extends Port {

    interface ComposerAccess {

        /**
         * Returns an unmodifiable list of child Ports.
         *
         * @return a non-null, unmodifiable list of child Ports
         */
        @Unmodifiable List<? extends ChildPort> getChildPorts();
    }

    /**
     * Returns the Port-safe facet of this component's Composer, without requiring the caller to know how to reach
     * the Composer itself.
     */
    ComposerAccess getComposerAccess();
}
