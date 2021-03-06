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

import org.eclipse.incquery.runtime.base.api.NavigationHelper;

import com.google.common.base.Preconditions;

/**
 * This provider implementation uses the IQBase after update callback as
 * an event source for update complete events.
 * 
 * @author Abel Hegedus
 * 
 */
public class IQBaseCallbackUpdateCompleteProvider extends UpdateCompleteProvider {

    private final Runnable callback;
    private final NavigationHelper helper;

    /**
     * Creates a new provider for the given {@link NavigationHelper}.
     */
    public IQBaseCallbackUpdateCompleteProvider(final NavigationHelper helper) {
        super();
        Preconditions.checkNotNull(helper, "Cannot create provider with null helper!");
        this.callback = new IQBaseAfterUpdateCallback();

        this.helper = helper;
        helper.getAfterUpdateCallbacks().add(callback);
    }

    /**
     * Callback class invoked by the {@link NavigationHelper}.
     * 
     * @author Abel Hegedus
     *
     */
    private class IQBaseAfterUpdateCallback implements Runnable {
        @Override
        public void run() {
            updateCompleted();
        }
    }

    @Override
    public void dispose() {
        helper.getAfterUpdateCallbacks().remove(callback);
        super.dispose();
    }

}
