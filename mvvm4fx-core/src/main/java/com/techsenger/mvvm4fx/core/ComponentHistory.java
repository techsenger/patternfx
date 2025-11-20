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

import java.io.Serializable;

/**
 * Represents a serializable snapshot of a component's {@code ViewModel} state. A history object stores only the
 * information that must persist between component sessions and excludes all transient or runtime aspects.
 *
 * <p>The {@code ViewModel} itself may contain both persistent and non-persistent values. It also defines default
 * values for all of its properties, regardless of whether they are stored in the history or used only at runtime. The
 * {@code ViewModel} owns the meaning of those defaults, while the history is responsible solely for persisting and
 * restoring the subset of states that are marked as persistent.
 *
 * <p>The history defines the persistent structure of a component and acts as a container for all states that should be
 * restored when the component is reinitialized. It is completely independent of the {@code ViewModel} and does not
 * contain default values or presentation logic. Default values belong to the {@code ViewModel}, while the history
 * merely reflects the states that were last saved.
 *
 * <p>It is the responsibility of the history to save and restore all persistent states of the {@code ViewModel} at the
 * appropriate points in the component's lifecycle. When the component becomes deinitialized, the current states of the
 * {@code ViewModel} are copied into the history. When the component is constructed again, the history restores those
 * states back into the {@code ViewModel}. This deterministic synchronization ensures a consistent mapping between the
 * runtime state and its persisted representation.
 *
 * @author Pavel Castornii
 */
public interface ComponentHistory<T extends ComponentViewModel> extends Serializable {

    /**
     * Returns whether this history instance is fresh, meaning it was newly created and has not yet been used to
     * save or restore any component state. A fresh history contains no previously stored data and should not be
     * passed to a component for restoration.
     *
     * <p>Once the component's state has been saved into this history, the flag becomes {@code false}, indicating that
     * the history now holds valid data that can be used to restore the component's state in future instances.
     *
     * @return {@code true} if this history is fresh and has not yet been used, {@code false} otherwise
     */
    boolean isFresh();

    /**
     * Method called before the component is serialized. This can be used to prepare the object's state
     * before saving it in a binary format.
     */
    void preSerialize();

    /**
     * Method called after the component is deserialized. This can be used to restore the object's state
     * after loading it from a binary format.
     */
    void postDeserialize();

    /**
     * Method copies all data from history to view model. This method is called when the component
     * becomes {@link ComponentState#CONSTRUCTED} and the policy is {@link HistoryPolicy#ALL} or
     * {@link HistoryPolicy#DATA}.
     *
     * @param viewModel
     */
    void restoreData(T viewModel);

    /**
     * Method copies all data from view model to history. This method is called when the component
     * becomes {@link ComponentState#DEINITIALIZED} and the policy is {@link HistoryPolicy#ALL} or
     * {@link HistoryPolicy#DATA}.
     *
     * @param viewModel
     */
    void saveData(T viewModel);

    /**
     * Method copies all appearance information from history to view model. This method is called when the component
     * becomes {@link ComponentState#CONSTRUCTED} and the policy is {@link HistoryPolicy#ALL} or
     * {@link HistoryPolicy#APPEARANCE}.
     *
     * @param viewModel
     */
    void restoreAppearance(T viewModel);

    /**
     * Method copies all data from view model to history. This method is called when the component
     * becomes {@link ComponentState#DEINITIALIZED} and the policy is {@link HistoryPolicy#ALL} or
     * {@link HistoryPolicy#APPEARANCE}.
     *
     * @param viewModel
     */
    void saveAppearance(T viewModel);
}
