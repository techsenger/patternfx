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

package com.techsenger.mvvm4fx.core;

import com.techsenger.toolkit.fx.binding.ListBinder;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentComponent<T extends AbstractParentView<?>> extends AbstractComponent<T>
        implements ParentComponent<T> {

    protected abstract class Mediator extends AbstractComponent.Mediator implements ParentMediator {

        private final ListBinder childrenBinder;

        private final ObservableList<ChildViewModel> modifiableChildren = FXCollections.observableArrayList();

        private final ObservableList<ChildViewModel> children =
                FXCollections.unmodifiableObservableList(modifiableChildren);

        public Mediator() {
            var outerChildren = AbstractParentComponent.this.modifiableChildren;
            childrenBinder = ListBinder.bindContent(modifiableChildren, outerChildren,
                    (v) -> v.getView().getViewModel());
        }

        @Override
        public SubtreeIterator<ParentViewModel> depthFirstIterator() {
            return new AbstractDepthFirstIterator<ParentViewModel>(getView().getViewModel()) {

                @Override
                List<ParentViewModel> getChildren(ParentViewModel parent) {
                    return (List) children;
                }
            };
        }

        @Override
        public SubtreeIterator<ParentViewModel> breadthFirstIterator() {
            return new AbstractBreadthFirstIterator<ParentViewModel>(getView().getViewModel()) {

                @Override
                List<ParentViewModel> getChildren(ParentViewModel parent) {
                    return (List) children;
                }
            };
        }

        @Override
        public ObservableList<ChildViewModel> getChildren() {
            return children;
        }
    }

    private final ObservableList<ChildComponent<?>> modifiableChildren = FXCollections.observableArrayList();

    private final ObservableList<ChildComponent<?>> children =
            FXCollections.unmodifiableObservableList(modifiableChildren);

    public AbstractParentComponent(T view) {
        super(view);
    }

    @Override
    public SubtreeIterator<ParentComponent<?>> depthFirstIterator() {
        return new AbstractDepthFirstIterator<ParentComponent<?>>(this) {

            @Override
            List<ParentComponent<?>> getChildren(ParentComponent<?> parent) {
                return (List) parent.getChildren();
            }
        };
    }

    @Override
    public SubtreeIterator<ParentComponent<?>> breadthFirstIterator() {
        return new AbstractBreadthFirstIterator<ParentComponent<?>>(this) {

            @Override
            List<ParentComponent<?>> getChildren(ParentComponent<?> parent) {
                return (List) parent.getChildren();
            }
        };
    }

    @Override
    public ObservableList<ChildComponent<?>> getChildren() {
        return this.children;
    }

    @Override
    protected abstract AbstractParentComponent.Mediator createMediator();

    protected void addChild(ChildComponent<?> child) {
        this.modifiableChildren.add(child);
        child.setParent(this);
    }

    protected void removeChild(ChildComponent<?> child) {
        this.modifiableChildren.remove(child);
        child.setParent(null);
    }
}
