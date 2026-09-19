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

import com.techsenger.annotations.Unmodifiable;
import com.techsenger.patternfx.core.AbstractBreadthFirstIterator;
import com.techsenger.patternfx.core.AbstractDepthFirstIterator;
import com.techsenger.patternfx.core.ChildPort;
import com.techsenger.patternfx.core.ParentPort;
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
public abstract class AbstractParentView<C extends ParentController<?>> extends AbstractView<C>
        implements ParentView<C> {

    public class Composer implements ParentView.Composer {

        private final AbstractParentView<?> view = AbstractParentView.this;

        private final ObservableList<ChildView<?>> modifiableChildren = FXCollections.observableArrayList();

        private final ObservableList<ChildView<?>> children =
                FXCollections.unmodifiableObservableList(modifiableChildren);

        public Composer() {
            modifiableChildren.addListener((ListChangeListener<ChildView<?>>) (e) -> {
                while (e.next()) {
                    if (e.wasAdded() || e.wasReplaced()) {
                        for (var c: e.getAddedSubList()) {
                            c.getComposer().setParent(view);
                        }
                    }
                    if (e.wasRemoved() || e.wasReplaced()) {
                        for (var c: e.getRemoved()) {
                            c.getComposer().setParent(null);
                        }
                    }
                }
            });
        }

        @Override
        public void compose() {
            // empty
        }

        @Override
        public List<? extends ChildPort> getChildPorts() {
            return getChildren().stream().map(v -> v.getController()).collect(Collectors.toList());
        }

        @Override
        public TreeIterator<ParentPort> depthFirstPortIterator() {
            return new AbstractDepthFirstIterator<ParentPort, ParentView<?>>(view) {

                @Override
                @SuppressWarnings("unchecked")
                protected List<ParentView<?>> getChildren(ParentView<?> parent) {
                    return (List<ParentView<?>>) (List<?>) parent.getComposer().getChildren();
                }

                @Override
                protected ParentPort map(ParentView<?> value) {
                    return value.getController();
                }
            };
        }

        @Override
        public TreeIterator<ParentPort> breadthFirstPortIterator() {
            return new AbstractBreadthFirstIterator<ParentPort, ParentView<?>>(view) {

                @Override
                @SuppressWarnings("unchecked")
                protected List<ParentView<?>> getChildren(ParentView<?> parent) {
                    return (List<ParentView<?>>) (List<?>) parent.getComposer().getChildren();
                }

                @Override
                protected ParentPort map(ParentView<?> value) {
                    return value.getController();
                }
            };
        }

        @Override
        public String toPortTreeString(BiConsumer<ParentPort, StringBuilder> appender) {
            return toTreeString(depthFirstPortIterator(), appender);
        }

        @Override
        public @Unmodifiable ObservableList<? extends ChildView<?>> getChildren() {
            return children;
        }

        @Override
        public TreeIterator<ParentView<?>> depthFirstIterator() {
            return new AbstractDepthFirstIterator<ParentView<?>, ParentView<?>>(view) {

                @Override
                @SuppressWarnings("unchecked")
                protected List<ParentView<?>> getChildren(ParentView<?> parent) {
                    return (List<ParentView<?>>) (List<?>) parent.getComposer().getChildren();
                }

                @Override
                protected ParentView<?> map(ParentView<?> value) {
                    return value;
                }
            };
        }

        @Override
        public TreeIterator<ParentView<?>> breadthFirstIterator() {
            return new AbstractBreadthFirstIterator<ParentView<?>, ParentView<?>>(view) {

                @Override
                @SuppressWarnings("unchecked")
                protected List<ParentView<?>> getChildren(ParentView<?> parent) {
                    return (List<ParentView<?>>) (List<?>) parent.getComposer().getChildren();
                }

                @Override
                protected ParentView<?> map(ParentView<?> value) {
                    return value;
                }
            };
        }

        @Override
        public String toTreeString() {
            return toTreeString(depthFirstIterator(),
                    (v, b) -> b.append(v.getController().getDescriptor().getFullName()));
        }

        @Override
        public String toTreeString(BiConsumer<ParentView<?>, StringBuilder> appender) {
            return toTreeString(depthFirstIterator(), appender);
        }

        protected ObservableList<ChildView<?>> getModifiableChildren() {
            return modifiableChildren;
        }

        private <T> String toTreeString(TreeIterator<T> iterator, BiConsumer<T, StringBuilder> appender) {
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

    private final Composer composer;

    public AbstractParentView() {
        super();
        this.composer = createComposer();
    }

    @Override
    public Composer getComposer() {
        return composer;
    }

    protected Composer createComposer() {
        return new AbstractParentView<C>.Composer();
    }
}
