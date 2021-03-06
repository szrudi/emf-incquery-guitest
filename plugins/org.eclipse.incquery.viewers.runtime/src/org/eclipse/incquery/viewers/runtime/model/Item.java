/*******************************************************************************
 * Copyright (c) 2010-2013, Zoltan Ujhelyi, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zoltan Ujhelyi - initial API and implementation
 *******************************************************************************/
package org.eclipse.incquery.viewers.runtime.model;

import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.incquery.databinding.runtime.api.IncQueryObservables;
import org.eclipse.incquery.runtime.api.IPatternMatch;

/**
 * @author Zoltan Ujhelyi
 * 
 */
public class Item extends FormattableElement {

    public static final String ANNOTATION_ID = "Item";

    private final IPatternMatch sourceMatch;
    private String labelDefinition;

    public Item(IPatternMatch match, String labelDefinition) {
        sourceMatch = match;
        this.labelDefinition = labelDefinition;
    }

    /**
     * @return the label
     */
    public IObservableValue getLabel() {
        if (labelDefinition == null || labelDefinition.isEmpty()) {
            return Observables.constantObservableValue("");
        } else {
            return IncQueryObservables.getObservableLabelFeature(sourceMatch, labelDefinition, this);
        }
    }

}
