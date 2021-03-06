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

import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.evm.api.Scheduler.ISchedulerFactory;

import com.google.common.collect.ImmutableSet;
/**
 * 
 * Utility class for creating new rule engines and execution schemes.
 * 
 * The static create methods use the provided parameters to set up the EVM
 * and return a facade object for accessing it.
 * 
 * @author Abel Hegedus
 * 
 */
public final class EventDrivenVM {

    private EventDrivenVM() {
    }

    /**
     * Creates a new execution schema that is initialized over the given
     * IncQueryEngine, creates an executor and agenda with the given
     *  rule specifications and prepares a scheduler using the provided factory.
     * 
     * @param engine
     * @param schedulerFactory
     * @param specifications
     * @return the prepared execution schema
     */
    public static ExecutionSchema createExecutionSchema(final IncQueryEngine engine,
            final ISchedulerFactory schedulerFactory, final Set<RuleSpecification<? extends IPatternMatch>> specifications) {
        checkNotNull(schedulerFactory, "Cannot create execution schema with null scheduler factory");
        checkNotNull(specifications, "Cannot create execution schema with null rule specification set");
        Executor executor = new Executor(engine);
        Agenda agenda = executor.getAgenda();
        for (RuleSpecification<?> specification : specifications) {
            agenda.instantiateRule(specification);
        }
        Scheduler scheduler = schedulerFactory.prepareScheduler(executor);
        return ExecutionSchema.create(scheduler);
    }
    
    /**
     * Creates a new execution schema that is initialized over the given
     * IncQueryEngine, creates an executor and agenda with the given
     *  rule specifications and prepares a scheduler using the provided factory.
     * 
     * @param engine
     * @param schedulerFactory
     * @param specifications
     * @return the prepared execution schema
     */
    public static ExecutionSchema createExecutionSchema(final IncQueryEngine engine,
            final ISchedulerFactory schedulerFactory, final RuleSpecification<? extends IPatternMatch>... specifications) {
        return createExecutionSchema(engine, schedulerFactory, ImmutableSet.copyOf(specifications));
    }
    
    /**
     * Creates a new execution schema that is initialized over the given
     * IncQueryEngine, creates an executor and agenda without rules and
     *  prepares a scheduler using the provided factory.
     * 
     * @param engine
     * @param schedulerFactory
     * @return the prepared execution schema
     */
    public static ExecutionSchema createExecutionSchema(final IncQueryEngine engine,
            final ISchedulerFactory schedulerFactory) {
        checkNotNull(schedulerFactory, "Cannot create execution schema with null scheduler factory");
        Executor executor = new Executor(engine);
        Scheduler scheduler = schedulerFactory.prepareScheduler(executor);
        return ExecutionSchema.create(scheduler);
    }
    
    /**
     * Creates a new rule engine that is initialized over the given
     * IncQueryEngine and an agenda with the given rule specifications.
     
     * @param engine
     * @param specifications
     * @return the prepared rule engine
     */
    public static RuleEngine createRuleEngine(final IncQueryEngine engine,
            final Set<RuleSpecification<?>> specifications) {
        checkNotNull(specifications, "Cannot create rule engine with null rule specification set");
        Agenda agenda = new Agenda(engine);
        for (RuleSpecification<?> ruleSpecification : specifications) {
            agenda.instantiateRule(ruleSpecification);
        }

        return RuleEngine.create(agenda);
    }
    
    /**
     * Creates a new rule engine that is initialized over the given
     * IncQueryEngine and an agenda with the given rule specifications.
     
     * @param engine
     * @param specifications
     * @return the prepared rule engine
     */
    public static RuleEngine createRuleEngine(final IncQueryEngine engine,
            final RuleSpecification<?>... specifications) {
        return createRuleEngine(engine, ImmutableSet.copyOf(specifications));
    }
    
    /**
     * Creates a new rule engine that is initialized over the given
     * IncQueryEngine and an agenda without rules.
     
     * @param engine
     * @return the prepared rule engine
     */
    public static RuleEngine createRuleEngine(final IncQueryEngine engine) {
        Agenda agenda = new Agenda(engine);
        return RuleEngine.create(agenda);
    }
}
