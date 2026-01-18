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

package com.techsenger.patternfx.mvvmx;

import com.techsenger.patternfx.core.AbstractBreadthFirstIterator;
import com.techsenger.patternfx.core.AbstractDepthFirstIterator;
import com.techsenger.patternfx.core.TreeIterator;
import com.techsenger.toolkit.fx.binding.ListBinder;
import java.util.List;
import java.util.function.BiConsumer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentComponent<T extends AbstractParentView<?, ?>> extends AbstractComponent<T>
        implements ParentComponent<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractParentComponent.class);

    protected class Mediator extends AbstractComponent.Mediator implements ParentMediator {

        private final ListBinder childrenBinder;

        private final ObservableList<ChildViewModel<?>> modifiableChildren = FXCollections.observableArrayList();

        private final ObservableList<ChildViewModel<?>> children =
                FXCollections.unmodifiableObservableList(modifiableChildren);

        private final AbstractParentComponent<?> component = AbstractParentComponent.this;

        public Mediator() {
            childrenBinder = ListBinder.bindContent(modifiableChildren, component.getChildren(),
                    (v) -> v.getView().getViewModel());
        }

        @Override
        public TreeIterator<ParentViewModel<?>> depthFirstIterator() {
            return new AbstractDepthFirstIterator<ParentViewModel<?>, ParentComponent<?>>(component) {

                @Override
                protected List<ParentComponent<?>> getChildren(ParentComponent<?> parent) {
                    return (List) parent.getChildren();
                }

                @Override
                protected ParentViewModel<?> map(ParentComponent<?> value) {
                    return value.getView().getViewModel();
                }
            };
        }

        @Override
        public TreeIterator<ParentViewModel<?>> breadthFirstIterator() {
            return new AbstractBreadthFirstIterator<ParentViewModel<?>, ParentComponent<?>>(component) {

                @Override
                protected List<ParentComponent<?>> getChildren(ParentComponent<?> parent) {
                    return (List) parent.getChildren();
                }

                @Override
                protected ParentViewModel<?> map(ParentComponent<?> value) {
                    return value.getView().getViewModel();
                }
            };
        }

        @Override
        public String toTreeString() {
            return component.toTreeString(depthFirstIterator(), (c, b) -> b.append(c.getMediator().getFullName()));
        }

        @Override
        public String toTreeString(BiConsumer<ParentViewModel<?>, StringBuilder> componentAppender) {
            return component.toTreeString(depthFirstIterator(), componentAppender);
        }

        @Override
        public ObservableList<ChildViewModel<?>> getChildren() {
            return children;
        }

        @Override
        public void deinitializeTree() {
            component.deinitializeTree();
        }
    }

    private final ObservableList<ChildComponent<?>> modifiableChildren = FXCollections.observableArrayList();

    private final ObservableList<ChildComponent<?>> children =
            FXCollections.unmodifiableObservableList(modifiableChildren);

    public AbstractParentComponent(T view) {
        super(view);
        modifiableChildren.addListener((ListChangeListener<ChildComponent<?>>) (e) -> {
            while (e.next()) {
                if (e.wasAdded() || e.wasReplaced()) {
                    for (var c: e.getAddedSubList()) {
                        c.setParent(this);
                    }
                }
                if (e.wasRemoved() || e.wasReplaced()) {
                    for (var c: e.getRemoved()) {
                        c.setParent(null);
                    }
                }
            }
        });
    }

    @Override
    public TreeIterator<ParentComponent<?>> depthFirstIterator() {
        return new AbstractDepthFirstIterator<ParentComponent<?>, ParentComponent<?>>(this) {

            @Override
            protected List<ParentComponent<?>> getChildren(ParentComponent<?> parent) {
                return (List) parent.getChildren();
            }

            @Override
            protected ParentComponent<?> map(ParentComponent<?> value) {
                return value;
            }
        };
    }

    @Override
    public TreeIterator<ParentComponent<?>> breadthFirstIterator() {
        return new AbstractBreadthFirstIterator<ParentComponent<?>, ParentComponent<?>>(this) {

            @Override
            protected List<ParentComponent<?>> getChildren(ParentComponent<?> parent) {
                return (List) parent.getChildren();
            }

            @Override
            protected ParentComponent<?> map(ParentComponent<?> value) {
                return value;
            }
        };
    }

    @Override
    public ObservableList<ChildComponent<?>> getChildren() {
        return this.children;
    }

    @Override
    public String toTreeString() {
        return toTreeString(depthFirstIterator(), (c, b) -> b.append(c.getFullName()));
    }

    @Override
    public String toTreeString(BiConsumer<ParentComponent<?>, StringBuilder> componentAppender) {
        return toTreeString(depthFirstIterator(), componentAppender);
    }

    @Override
    public void deinitializeTree() {
        if (logger.isDebugEnabled()) {
            var tree = toTreeString();
            logger.debug("{} Deinitializing this component tree:\n{}", getLogPrefix(), tree);
        }
        var iterator = breadthFirstIterator();
        while (iterator.hasNext()) {
            iterator.next().deinitialize();
        }
    }

    protected ObservableList<ChildComponent<?>> getModifiableChildren() {
        return modifiableChildren;
    }

    @Override
    protected abstract Mediator createMediator();

    private <T> String toTreeString(TreeIterator<T> iterator, BiConsumer<T, StringBuilder> componentAppender) {
        var builder = new StringBuilder();
        var sep = System.lineSeparator();
        while (iterator.hasNext()) {
            var c = iterator.next();
            if (builder.length() > 0) {
                builder.append(sep);
            }
            builder.append("    ".repeat(iterator.getDepth()));
            componentAppender.accept(c, builder);
        }
        return builder.toString();
    }
}
