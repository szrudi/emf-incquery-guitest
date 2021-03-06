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
package org.eclipse.incquery.databinding.runtime.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.list.AbstractObservableList;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.incquery.databinding.runtime.api.IncQueryObservables;
import org.eclipse.incquery.runtime.api.EngineManager;
import org.eclipse.incquery.runtime.api.IMatcherFactory;
import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.api.IncQueryMatcher;
import org.eclipse.incquery.runtime.evm.api.EventDrivenVM;
import org.eclipse.incquery.runtime.evm.api.ExecutionSchema;
import org.eclipse.incquery.runtime.evm.api.RuleEngine;
import org.eclipse.incquery.runtime.evm.api.RuleSpecification;
import org.eclipse.incquery.runtime.evm.specific.UpdateCompleteBasedScheduler;
import org.eclipse.incquery.runtime.exception.IncQueryException;
import org.eclipse.incquery.runtime.extensibility.MatcherFactoryRegistry;

/**
 * Observable view of a match set for a given {@link IncQueryMatcher} on a model (match sets of an
 * {@link IncQueryMatcher} are ordered by the order of their appearance).
 * 
 * <p>
 * This implementation uses the {@link ExecutionSchema} to get notifications for match set changes, and can be
 * instantiated using either an existing {@link IncQueryMatcher}, or an {@link IMatcherFactory} and either a
 * {@link Notifier}, {@link IncQueryEngine} or {@link ExecutionSchema}.
 * 
 * @author Abel Hegedus
 * 
 */
public class ObservablePatternMatchList<Match extends IPatternMatch> extends AbstractObservableList {

    private final List<Match> cache = Collections.synchronizedList(new ArrayList<Match>());
    private final ListCollectionUpdate updater = new ListCollectionUpdate();

    /**
     * Creates an observable view of the match set of the given {@link IncQueryMatcher}.
     * 
     * <p>
     * Consider using {@link IncQueryObservables#observeMatchesAsList} instead!
     * 
     * @param matcher
     *            the {@link IncQueryMatcher} to use as the source of the observable list
     * @throws IncQueryException if the {@link IncQueryEngine} base index is not available
     */
    @SuppressWarnings("unchecked")
    public <Matcher extends IncQueryMatcher<Match>> ObservablePatternMatchList(Matcher matcher) throws IncQueryException {
        this((IMatcherFactory<Matcher>) MatcherFactoryRegistry
                .getOrCreateMatcherFactory(matcher.getPattern()), matcher.getEngine());
    }

    /**
     * Creates an observable view of the match set of the given {@link IMatcherFactory} initialized on the given
     * {@link Notifier}.
     * 
     * <p>
     * Consider using {@link IncQueryObservables#observeMatchesAsList} instead!
     * 
     * @param factory
     *            the {@link IMatcherFactory} used to create a matcher
     * @param notifier
     *            the {@link Notifier} on which the matcher is created
     * @throws IncQueryException if the {@link IncQueryEngine} creation fails on the {@link Notifier}
     */
    public <Matcher extends IncQueryMatcher<Match>> ObservablePatternMatchList(IMatcherFactory<Matcher> factory,
            Notifier notifier) throws IncQueryException {
        this(factory, EngineManager.getInstance().getIncQueryEngine(notifier));
    }

    /**
     * Creates an observable view of the match set of the given {@link IMatcherFactory} initialized on the given
     * {@link IncQueryEngine}.
     * 
     * <p>
     * Consider using {@link IncQueryObservables#observeMatchesAsList} instead!
     * 
     * @param factory
     *            the {@link IMatcherFactory} used to create a matcher
     * @param engine
     *            the {@link IncQueryEngine} on which the matcher is created
     * @throws IncQueryException if the {@link IncQueryEngine} base index is not available
     */
    public <Matcher extends IncQueryMatcher<Match>> ObservablePatternMatchList(IMatcherFactory<Matcher> factory,
            IncQueryEngine engine) {
        RuleSpecification<Match> specification = ObservableCollectionHelper.createRuleSpecification(updater, factory);
        RuleEngine triggerEngine = EventDrivenVM.createExecutionSchema(engine,
                UpdateCompleteBasedScheduler.getIQBaseSchedulerFactory(engine));
        triggerEngine.addRule(specification,true);
    }

    /**
     * Creates an observable view of the match set of the given {@link IMatcherFactory} initialized on the given
     * {@link ExecutionSchema}.
     * 
     * <p>
     * Consider using {@link IncQueryObservables#observeMatchesAsList} instead!
     * 
     * <p>
     * 
     * @param factory
     *            the {@link IMatcherFactory} used to create a matcher
     * @param engine
     *            an existing {@link ExecutionSchema} that specifies the used model
     */
    public <Matcher extends IncQueryMatcher<Match>> ObservablePatternMatchList(IMatcherFactory<Matcher> factory,
            RuleEngine engine) {
        super();
        RuleSpecification<Match> specification = ObservableCollectionHelper.createRuleSpecification(updater, factory);
        engine.addRule(specification,true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.observable.list.IObservableList#getElementType()
     */
    @Override
    public Object getElementType() {
        return IPatternMatch.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.observable.list.AbstractObservableList#doGetSize()
     */
    @Override
    protected int doGetSize() {
        return cache.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractList#get(int)
     */
    @Override
    public Object get(int index) {
        return cache.get(index);
    }

    public class ListCollectionUpdate implements IObservablePatternMatchCollectionUpdate<Match> {
        @Override
        public void addMatch(Match match) {
            ListDiffEntry diffentry = Diffs.createListDiffEntry(cache.size(), true, match);
            cache.add(match);
            ListDiff diff = Diffs.createListDiff(diffentry);
            fireListChange(diff);
        }

        @Override
        public void removeMatch(Match match) {
            final int index = cache.indexOf(match);
            ListDiffEntry diffentry = Diffs.createListDiffEntry(index, false, match);
            cache.remove(match);
            ListDiff diff = Diffs.createListDiff(diffentry);
            fireListChange(diff);
        }
    }

}
