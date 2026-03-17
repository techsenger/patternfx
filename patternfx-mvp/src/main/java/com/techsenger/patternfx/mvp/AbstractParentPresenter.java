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

import com.google.errorprone.annotations.concurrent.LazyInit;
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

    @LazyInit
    private C composer;

    public AbstractParentPresenter(V view) {
        super(view);
    }

    @Override
    public C getComposer() {
        return this.composer;
    }

    @Override
    public void deinitializeTree() {
        if (logger.isDebugEnabled()) {
            var tree = composer.toTreeString();
            logger.debug("{} Deinitializing this component tree:\n{}", getDescriptor().getLogPrefix(), tree);
        }
        var iterator = composer.breadthFirstIterator();
        while (iterator.hasNext()) {
            var port = iterator.next();
            var presenter = (ParentPresenter<?, ?>) port;
            presenter.deinitialize();
        }
    }

    @Override
    public List<? extends ChildPort> getChildren() {
        return getComposer().getChildren();
    }

    @Override
    public void requestFocus() {
        getView().requestFocus();
    }

    @Override
    protected void postInitialize() {
        super.postInitialize();
        this.composer.compose();
    }

    @SuppressWarnings("unchecked")
    protected void setComposer(ParentComposer composer) {
        this.composer = (C) composer;
    }
}
