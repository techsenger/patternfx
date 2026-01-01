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
public abstract class AbstractParentView<VM extends AbstractParentViewModel<?>>
        extends AbstractView<VM> implements ParentView<VM> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractParentView.class);

    private final ListBinder childrenBinder;

    private final ObservableList<ChildView<?>> modifiableChildren = FXCollections.observableArrayList();

    private final ObservableList<ChildView<?>> children =
            FXCollections.unmodifiableObservableList(modifiableChildren);

    public AbstractParentView(VM viewModel) {
        super(viewModel);
        modifiableChildren.addListener((ListChangeListener<ChildView<?>>) (e) -> {
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
        childrenBinder = ListBinder.bindContent(getViewModel().getModifiableChildren(), modifiableChildren,
                    (v) -> v.getViewModel());
        getViewModel().setComposer(createComposer());
    }

    @Override
    public ObservableList<ChildView<?>> getChildren() {
        return children;
    }

    @Override
    public TreeIterator<ParentView<?>> depthFirstIterator() {
        return new AbstractDepthFirstIterator<ParentView<?>>(this) {

            @Override
            protected List<ParentView<?>> getChildren(ParentView<?> parent) {
                return (List) parent.getChildren();
            }
        };
    }

    @Override
    public TreeIterator<ParentView<?>> breadthFirstIterator() {
        return new AbstractBreadthFirstIterator<ParentView<?>>(this) {

            @Override
            protected List<ParentView<?>> getChildren(ParentView<?> parent) {
                return (List) parent.getChildren();
            }
        };
    }

    @Override
    public String toTreeString() {
        return getViewModel().toTreeString(depthFirstIterator(),
                (v, b) -> b.append(v.getViewModel().getDescriptor().getFullName()));
    }

    @Override
    public String toTreeString(BiConsumer<ParentView<?>, StringBuilder> appender) {
        return getViewModel().toTreeString(depthFirstIterator(), appender);
    }

    @Override
    public void deinitializeTree() {
        if (logger.isDebugEnabled()) {
            var tree = toTreeString();
            logger.debug("{} Deinitializing this view tree:\n{}", getDescriptor().getLogPrefix(), tree);
        }
        var iterator = breadthFirstIterator();
        while (iterator.hasNext()) {
            iterator.next().deinitialize();
        }
    }

    protected abstract Composer createComposer();

    @Override
    protected void addListeners() {
        super.addListeners();
        getViewModel().getRequestDeinitializeTree().addListener((newV) -> deinitializeTree());
    }

    protected ObservableList<ChildView<?>> getModifiableChildren() {
        return modifiableChildren;
    }
}
