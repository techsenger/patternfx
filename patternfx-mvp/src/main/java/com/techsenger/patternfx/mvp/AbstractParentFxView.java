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

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentFxView<P extends ParentPresenter<?, ?>>
        extends AbstractFxView<P> implements ParentFxView<P> {

    public class Composer implements ParentFxView.Composer {

        private final AbstractParentFxView<?> view = AbstractParentFxView.this;

        @Override
        public List<? extends ChildPort> getChildren() {
            return view.getChildren().stream().map(v -> v.getPresenter().getPort()).collect(Collectors.toList());
        }

        @Override
        public TreeIterator<ParentPort> depthFirstIterator() {
            return new AbstractDepthFirstIterator<ParentPort, ParentFxView<?>>(view) {

                @Override
                protected List<ParentFxView<?>> getChildren(ParentFxView<?> parent) {
                    return (List) parent.getChildren();
                }

                @Override
                protected ParentPort map(ParentFxView<?> value) {
                    return value.getPresenter().getPort();
                }
            };
        }

        @Override
        public TreeIterator<ParentPort> breadthFirstIterator() {
            return new AbstractBreadthFirstIterator<ParentPort, ParentFxView<?>>(view) {

                @Override
                protected List<ParentFxView<?>> getChildren(ParentFxView<?> parent) {
                    return (List) parent.getChildren();
                }

                @Override
                protected ParentPort map(ParentFxView<?> value) {
                    return value.getPresenter().getPort();
                }
            };
        }

        @Override
        public String toTreeString() {
            return view.toTreeString();
        }

        @Override
        public String toTreeString(BiConsumer<ParentPort, StringBuilder> appender) {
            return view.toTreeString(depthFirstIterator(), appender);
        }
    }

    private final ObservableList<ChildFxView<?>> modifiableChildren = FXCollections.observableArrayList();

    private final ObservableList<ChildFxView<?>> children =
            FXCollections.unmodifiableObservableList(modifiableChildren);

    private final Composer composer;

    public AbstractParentFxView() {
        super();
        this.composer = createComposer();
        modifiableChildren.addListener((ListChangeListener<ChildFxView<?>>) (e) -> {
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
    public ObservableList<? extends ChildFxView<?>> getChildren() {
        return children;
    }

    @Override
    public TreeIterator<ParentFxView<?>> depthFirstIterator() {
        return new AbstractDepthFirstIterator<ParentFxView<?>, ParentFxView<?>>(this) {

            @Override
            protected List<ParentFxView<?>> getChildren(ParentFxView<?> parent) {
                return (List) parent.getChildren();
            }

            @Override
            protected ParentFxView<?> map(ParentFxView<?> value) {
                return value;
            }
        };
    }

    @Override
    public TreeIterator<ParentFxView<?>> breadthFirstIterator() {
        return new AbstractBreadthFirstIterator<ParentFxView<?>, ParentFxView<?>>(this) {

            @Override
            protected List<ParentFxView<?>> getChildren(ParentFxView<?> parent) {
                return (List) parent.getChildren();
            }

            @Override
            protected ParentFxView<?> map(ParentFxView<?> value) {
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
    public String toTreeString(BiConsumer<ParentFxView<?>, StringBuilder> appender) {
        return toTreeString(depthFirstIterator(), appender);
    }

    @Override
    public Composer getComposer() {
        return composer;
    }

    protected Composer createComposer() {
        return new AbstractParentFxView.Composer();
    }

    protected ObservableList<ChildFxView<?>> getModifiableChildren() {
        return modifiableChildren;
    }

    @Override
    protected void setPresenter(Presenter<?> presenter) {
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
