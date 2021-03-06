/*******************************************************************************
 * Copyright (c) 2010-2012, Abel Hegedus, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Abel Hegedus - initial API and implementation
 *******************************************************************************/
package org.eclipse.incquery.runtime.evm.update;

import java.util.HashSet;
import java.util.Set;

/**
 * This abstract implementation allows the registration of listeners and calls them when 
 * an update complete event occurs. The class is abstract since there is no definition of
 * the actual update complete event.
 * 
 * @author Abel Hegedus
 * 
 */
public abstract class UpdateCompleteProvider implements IUpdateCompleteProvider {

    private final Set<IUpdateCompleteListener> listeners;

    public UpdateCompleteProvider() {
        listeners = new HashSet<IUpdateCompleteListener>();
    }

    @Override
    public boolean addUpdateCompleteListener(final IUpdateCompleteListener listener, final boolean fireNow) {
        boolean added = listeners.add(listener);
        if (added) {
            listener.updateComplete();
        }
        return added;
    }

    @Override
    public boolean removeUpdateCompleteListener(final IUpdateCompleteListener listener) {
        return this.listeners.remove(listener);
    }

    /**
     * Notifies each listener that an update complete event occurred.
     */
    protected void updateCompleted() {
        for (IUpdateCompleteListener listener : this.listeners) {
            listener.updateComplete();
        }
    }

    /**
     * Disposes of the provider by clearing the listener list
     */
    public void dispose() {
        listeners.clear();
    }

}
