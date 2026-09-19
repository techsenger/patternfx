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

package com.techsenger.patternfx.mvc;

import com.techsenger.patternfx.core.ComponentParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentController<V extends ParentView<?>> extends AbstractController<V>
        implements ParentController<V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractParentController.class);

    public AbstractParentController(V view, ComponentParams params) {
        super(view, params);
    }

    @Override
    public ComposerAccess getComposerAccess() {
        return getView().getComposer();
    }

    @Override
    public void deinitializeTree() {
        var composer = getView().getComposer();
        if (logger.isDebugEnabled()) {
            var tree = composer.toTreeString();
            logger.debug("{} Deinitializing this component tree:\n{}", getDescriptor().getLogPrefix(), tree);
        }
        var iterator = composer.breadthFirstPortIterator();
        while (iterator.hasNext()) {
            var port = iterator.next();
            var controller = (ParentController<?>) port;
            controller.deinitialize();
        }
    }

    @Override
    protected void postInitialize() {
        super.postInitialize();
        this.getView().getComposer().compose();
    }
}
