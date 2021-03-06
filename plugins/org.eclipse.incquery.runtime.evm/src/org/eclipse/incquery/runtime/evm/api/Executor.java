/*******************************************************************************
 * Copyright (c) 2010-2013, Abel Hegedus, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Abel Hegedus - initial API and implementation
 *******************************************************************************/
package org.eclipse.incquery.runtime.evm.api;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import org.eclipse.incquery.runtime.api.IncQueryEngine;

/**
 * The executor is responsible for firing enabled activations of its agenda,
 * when its scheduler notifies it. The executor also manages a context that
 * is passed to activations.
 * 
 * @author Abel Hegedus
 *
 */
public class Executor {

    private Agenda agenda;
    private Context context;
    private boolean scheduling = false;
    
    /**
     * Creates an executor for the given IncQueryEngine.
     * Executors are usually created as part of an ExecutionSchema 
     * through the EventDrivenVM.createExecutionSchema methods.
     * 
     * @param engine
     */
    protected Executor(final IncQueryEngine engine) {
        this(engine, Context.create());
    }

    /**
     * Creates an executor for the given IncQueryEngine and Context.
     * Executors are usually created as part of an ExecutionSchema 
     * through the EventDrivenVM.createExecutionSchema methods.
     * 
     * @param engine
     * @param context
     */
    protected Executor(final IncQueryEngine engine, final Context context) {
        this.context = checkNotNull(context, "Cannot create trigger engine with null context!");
        agenda = new Agenda(engine);
    }

    /**
     * This method is called by the scheduler to indicate that the
     * executor should start its firing strategy.
     * 
     * The default implementation uses an as-long-as-possible strategy,
     * where the first enabled activation is fired, as long as there is one.
     * 
     * If firing causes further schedule calls, these reentrant calls are 
     * ignored, since the activations will be fired if they became enabled.
     */
    protected void schedule() {
        
        if(!startScheduling()) {
            return;
        }
        
        Set<Activation<?>> enabledActivations = agenda.getEnabledActivations();
        while(!enabledActivations.isEmpty()) {
            Activation<?> activation = enabledActivations.iterator().next();
            agenda.getIncQueryEngine().getLogger().debug(String.format("Executing %s in %s.",activation,this));
            activation.fire(context);
        }
        
        endScheduling();
    }

    /**
     * This method is called from schedule() to indicate that a new call
     * was received. If there is already scheduling in progress, that is 
     * logged and false is returned.
     * 
     * Otherwise, a new scheduling starts, which is logged and stored.
     * 
     * @return true, if the firing strategy can start, false otherwise
     */
    protected synchronized boolean startScheduling() {
        if(scheduling) {
            agenda.getIncQueryEngine().getLogger().debug(String.format("Reentrant schedule call ignored in %s.", this));
            return false;
        } else {
            scheduling = true;
            agenda.getIncQueryEngine().getLogger().debug(String.format("Executing started in %s.",this));
            return true;
        }
    }
    
    /**
     * This method is called by schedule() to indicate that the firing 
     * strategy is finished its execution. This is logged and the scheduling
     * state is set to false.
     */
    protected synchronized void endScheduling() {
        agenda.getIncQueryEngine().getLogger().debug(String.format("Executing ended in %s.",this));
        scheduling = false;
    }

    /**
     * @return the agenda
     */
    public Agenda getAgenda() {
        return agenda;
    }
    
    /**
     * @return the context
     */
    public Context getContext() {
        return context;
    }
    
    /**
     * Disposes of the executor by disposing its agenda.
     * 
     */
    protected void dispose() {
        agenda.dispose();
    }
    
}
