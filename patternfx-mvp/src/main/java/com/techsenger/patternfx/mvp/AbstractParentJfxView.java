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

import com.techsenger.patternfx.core.AbstractBreadthFirstIterator;
import com.techsenger.patternfx.core.AbstractDepthFirstIterator;
import com.techsenger.patternfx.core.TreeIterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentJfxView<T extends ParentPresenter>
        extends AbstractJfxView<T> implements ParentJfxView<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractParentJfxView.class);

    protected class Composer implements ParentComposer {

        private final AbstractParentJfxView<?> view = AbstractParentJfxView.this;

        @Override
        public List<Port> getChildren() {
            return view.children.stream().map(v -> v.getPresenter().getPort()).collect(Collectors.toList());
        }

        @Override
        public TreeIterator<Port> depthFirstIterator() {
            return new AbstractDepthFirstIterator<Port, ParentJfxView<?>>(view) {

                @Override
                protected List<ParentJfxView<?>> getChildren(ParentJfxView<?> parent) {
                    return (List) parent.getChildren();
                }

                @Override
                protected Port map(ParentJfxView<?> value) {
                    return value.getPresenter().getPort();
                }
            };
        }

        @Override
        public TreeIterator<Port> breadthFirstIterator() {
            return new AbstractBreadthFirstIterator<Port, ParentJfxView<?>>(view) {

                @Override
                protected List<ParentJfxView<?>> getChildren(ParentJfxView<?> parent) {
                    return (List) parent.getChildren();
                }

                @Override
                protected Port map(ParentJfxView<?> value) {
                    return value.getPresenter().getPort();
                }
            };
        }

        @Override
        public String toTreeString() {
            return view.toTreeString();
        }

        @Override
        public String toTreeString(BiConsumer<Port, StringBuilder> appender) {
            return view.toTreeString(depthFirstIterator(), appender);
        }
    }

    private final ObservableList<ChildJfxView<?>> modifiableChildren = FXCollections.observableArrayList();

    private final ObservableList<ChildJfxView<?>> children =
            FXCollections.unmodifiableObservableList(modifiableChildren);

    public AbstractParentJfxView() {
        super();
        modifiableChildren.addListener((ListChangeListener<ChildJfxView<?>>) (e) -> {
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
    public ObservableList<ChildJfxView<?>> getChildren() {
        return children;
    }

    @Override
    public TreeIterator<ParentJfxView<?>> depthFirstIterator() {
        return new AbstractDepthFirstIterator<ParentJfxView<?>, ParentJfxView<?>>(this) {

            @Override
            protected List<ParentJfxView<?>> getChildren(ParentJfxView<?> parent) {
                return (List) parent.getChildren();
            }

            @Override
            protected ParentJfxView<?> map(ParentJfxView<?> value) {
                return value;
            }
        };
    }

    @Override
    public TreeIterator<ParentJfxView<?>> breadthFirstIterator() {
        return new AbstractBreadthFirstIterator<ParentJfxView<?>, ParentJfxView<?>>(this) {

            @Override
            protected List<ParentJfxView<?>> getChildren(ParentJfxView<?> parent) {
                return (List) parent.getChildren();
            }

            @Override
            protected ParentJfxView<?> map(ParentJfxView<?> value) {
                return value;
            }
        };
    }

    @Override
    public String toTreeString() {
        return toTreeString(depthFirstIterator(),
                (v, b) -> b.append(getDescriptor().getFullName()));
    }

    @Override
    public String toTreeString(BiConsumer<ParentJfxView<?>, StringBuilder> appender) {
        return toTreeString(depthFirstIterator(), appender);
    }

    protected abstract Composer createComposer();

    protected ObservableList<ChildJfxView<?>> getModifiableChildren() {
        return modifiableChildren;
    }

    @Override
    protected void setPresenter(Presenter presenter) {
        super.setPresenter(presenter);
        if (presenter instanceof AbstractParentPresenter<?, ?>) {
            ((AbstractParentPresenter<?, ?>) presenter).setComposer(createComposer());
        }
    }

    <T> String toTreeString(TreeIterator<T> iterator, BiConsumer<T, StringBuilder> appender) {
        var builder = new StringBuilder();
        var sep = System.lineSeparator();
        while (iterator.hasNext()) {
            var c = iterator.next();
            if (builder.length() > 0) {
                builder.append(sep);
            }
            builder.append("    ".repeat(iterator.getDepth()));
            appender.accept(c, builder);
        }
        return builder.toString();
    }
}
