/*******************************************************************************
 * Copyright (c) 2010-2012, Tamas Szabo, Abel Hegedus, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tamas Szabo, Abel Hegedus - initial API and implementation
 *******************************************************************************/

package org.eclipse.incquery.runtime.evm.api;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.incquery.runtime.api.IncQueryMatcher;

import com.google.common.base.Objects;

/**
 * An {@link Activation} is a created for a {@link RuleInstance} when the preconditions (LHS) are fully satisfied with
 * some domain model elements and the rule becomes eligible for execution.
 * 
 * <p>
 * An Activation holds a state, a pattern match, the corresponding rule 5nstance. The state of the
 * Activation can be either Inactive, Appeared, Disappeared, Upgraded or Fired, while its actual
 * state will be managed by the life-cycle of its rule.
 * 
 * @author Tamas Szabo
 * 
 * @param <Match>
 *            the type of the pattern match
 */
public class Activation<Match extends IPatternMatch> {

    private Match patternMatch;
    private ActivationState state;
    private boolean enabled;
    private RuleInstance<Match> rule;
    private int cachedHash = -1;

    protected <Matcher extends IncQueryMatcher<Match>> Activation(RuleInstance<Match> rule, Match patternMatch) {
        this.patternMatch = checkNotNull(patternMatch,"Cannot create activation with null patternmatch");
        this.rule = checkNotNull(rule,"Cannot create activation with null rule");
        this.state = ActivationState.INACTIVE;
    }

    public Match getPatternMatch() {
        return patternMatch;
    }

    public ActivationState getState() {
        return state;
    }
    
    /**
     * An activatio is enabled, if the there are jobs corresponding
     *  to the state of the activation.
     * 
     * @return true, if there are jobs for the current state
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * @return the rule
     */
    public RuleInstance<Match> getRule() {
        return rule;
    }

    /**
     * Should be only set through {@link RuleInstance#activationStateTransition}
     * 
     * @param state
     */
    protected void setState(final ActivationState state) {
        this.state = checkNotNull(state, "Activation state cannot be null!");
        enabled = rule.getSpecification().getEnabledStates().contains(state);
    }

    /**
     * The activation will be fired; the appropriate job of the rule will be executed based on the activation state.
     */
    public void fire(final Context context) {
        checkNotNull(context,"Cannot fire activation with null context");
        rule.fire(this, context);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Activation) {
            Activation<?> other = (Activation<?>) obj;
            return (other.rule.equals(this.rule)) && (other.patternMatch.equals(this.patternMatch)
                    /*&& (other.state == this.state*/);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (cachedHash == -1) {
            cachedHash = Objects.hashCode(rule, patternMatch/*, state*/);
        }
        return cachedHash;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this).
                add("match",patternMatch).
                add("state",state).toString();
    }
}
