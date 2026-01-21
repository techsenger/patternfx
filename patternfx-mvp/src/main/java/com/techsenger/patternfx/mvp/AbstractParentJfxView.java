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
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentJfxView<P extends ParentPresenter, C extends ParentComposer>
        extends AbstractJfxView<P> implements ParentJfxView<P> {

    private final ObservableList<ChildJfxView<?>> modifiableChildren = FXCollections.observableArrayList();

    private final ObservableList<ChildJfxView<?>> children =
            FXCollections.unmodifiableObservableList(modifiableChildren);

    private final C composer;

    public AbstractParentJfxView() {
        super();
        this.composer = createComposer();
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

    public C getComposer() {
        return composer;
    }

    protected C createComposer() {
        return (C) new DefaultParentJfxComposer<>(this);
    }

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
