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

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentPresenter<V extends ParentView, C extends ParentComposer>
        extends AbstractPresenter<V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractParentPresenter.class);

    protected class Port implements com.techsenger.patternfx.mvp.ParentPort {

        private final AbstractParentPresenter<?, ?> presenter = AbstractParentPresenter.this;

        @Override
        public Descriptor getDescriptor() {
            return presenter.getDescriptor();
        }

        @Override
        public void deinitialize() {
            presenter.deinitialize();
        }

        @Override
        public List<? extends ChildPort> getChildren() {
            return getComposer().getChildren();
        }
    }

    private final Port port;

    private C composer;

    public AbstractParentPresenter(V view) {
        super(view);
        this.port = createPort();
    }

    /**
     * Returns the composer.
     * @return
     */
    public C getComposer() {
        return this.composer;
    }

    /**
     * Returns the port.
     * @return
     */
    public ParentPort getPort() {
        return this.port;
    }

    /**
     * Deinitializes this component and all its descendants as a sub-tree with this component as the root.
     *
     * The deinitialization is performed using a breadth-first traversal: the parent component is deinitialized first,
     * followed by its children level by level.
     */
    public void deinitializeTree() {
        if (logger.isDebugEnabled()) {
            var tree = composer.toTreeString();
            logger.debug("{} Deinitializing this view tree:\n{}", getDescriptor().getLogPrefix(), tree);
        }
        var iterator = composer.breadthFirstIterator();
        while (iterator.hasNext()) {
            iterator.next().deinitialize();
        }
    }

    protected void setComposer(ParentComposer composer) {
        this.composer = (C) composer;
    }

    protected Port createPort() {
        return new Port();
    }
}
