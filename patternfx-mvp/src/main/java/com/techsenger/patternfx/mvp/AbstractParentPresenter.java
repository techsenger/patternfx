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
        extends AbstractPresenter<V> implements ParentPresenter<V, C> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractParentPresenter.class);

    protected class Port implements ParentPort {

        private final AbstractParentPresenter<?, ?> presenter = AbstractParentPresenter.this;

        public Port() {
            // empty
        }

        @Override
        public void requestFocus() {
            getView().requestFocus();
        }

        @Override
        public Descriptor getDescriptor() {
            return presenter.getDescriptor();
        }

        @Override
        public void initialize() {
            presenter.initialize();
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

    private ComposeParameters parameters;

    public AbstractParentPresenter(V view) {
        super(view);
        this.port = createPort();
        this.parameters = createParameters();
    }

    @Override
    public C getComposer() {
        return this.composer;
    }

    @Override
    public Port getPort() {
        return this.port;
    }

    @Override
    public void deinitializeTree() {
        if (logger.isDebugEnabled()) {
            var tree = composer.toTreeString();
            logger.debug("{} Deinitializing this component tree:\n{}", getDescriptor().getLogPrefix(), tree);
        }
        var iterator = composer.breadthFirstIterator();
        while (iterator.hasNext()) {
            iterator.next().deinitialize();
        }
    }

    @Override
    protected void postInitialize() {
        super.postInitialize();
        this.composer.compose(parameters);
        this.parameters = null;
    }

    protected void setComposer(ParentComposer composer) {
        this.composer = (C) composer;
    }

    protected Port createPort() {
        return new Port();
    }

    /**
    * Creates the parameters to be passed to {@link ParentComposer#compose(ComposerParameters)} during the static
    * composition phase. After composition completes, the instance is set to {@code null} and is no longer accessible.
    *
    * @return the parameters for composition, or {@code null} if no parameters are required
    */
    protected ComposeParameters createParameters() {
        return null;
    }

    protected ComposeParameters getParameters() {
        return parameters;
    }
}
