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

package com.techsenger.patternfx.mvvm;

import com.techsenger.patternfx.core.AbstractBreadthFirstIterator;
import com.techsenger.patternfx.core.AbstractDepthFirstIterator;
import com.techsenger.patternfx.core.TreeIterator;
import com.techsenger.toolkit.fx.value.ObservableSource;
import com.techsenger.toolkit.fx.value.SimpleObservableSource;
import java.util.List;
import java.util.function.BiConsumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentViewModel<C extends Composer> extends AbstractViewModel
        implements ParentViewModel<C> {

    private C composer;

    private final ObservableList<ChildViewModel<?>> modifiableChildren = FXCollections.observableArrayList();

    private final ObservableList<ChildViewModel<?>> children =
            FXCollections.unmodifiableObservableList(modifiableChildren);

    private final ObservableSource<Void> requestDeinitializeTree = new SimpleObservableSource<>();

    public AbstractParentViewModel() {
        super();
    }

    @Override
    public C getComposer() {
        return this.composer;
    }

    @Override
    public ObservableList<ChildViewModel<?>> getChildren() {
        return children;
    }

    @Override
    public TreeIterator<ParentViewModel<?>> depthFirstIterator() {
        return new AbstractDepthFirstIterator<ParentViewModel<?>, ParentViewModel<?>>(this) {

            @Override
            protected List<ParentViewModel<?>> getChildren(ParentViewModel<?> parent) {
                return (List) parent.getChildren();
            }

            @Override
            protected ParentViewModel<?> map(ParentViewModel<?> value) {
                return value;
            }
        };
    }

    @Override
    public TreeIterator<ParentViewModel<?>> breadthFirstIterator() {
        return new AbstractBreadthFirstIterator<ParentViewModel<?>, ParentViewModel<?>>(this) {

            @Override
            protected List<ParentViewModel<?>> getChildren(ParentViewModel<?> parent) {
                return (List) parent.getChildren();
            }

            @Override
            protected ParentViewModel<?> map(ParentViewModel<?> value) {
                return value;
            }
        };
    }

    @Override
    public String toTreeString() {
        return toTreeString(depthFirstIterator(), (vm, b) -> b.append(vm.getDescriptor().getFullName()));
    }

    @Override
    public String toTreeString(BiConsumer<ParentViewModel<?>, StringBuilder> appender) {
        return toTreeString(depthFirstIterator(), appender);
    }

    @Override
    public void requestDeinitializeTree() {
        requestDeinitializeTree.next(null);
    }

    protected void setComposer(Composer composer) {
        this.composer = (C) composer;
    }

    protected ObservableList<ChildViewModel<?>> getModifiableChildren() {
        return modifiableChildren;
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

    ObservableSource<Void> getRequestDeinitializeTree() {
        return requestDeinitializeTree;
    }
}
