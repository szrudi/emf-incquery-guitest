package org.eclipse.incquery.testing.queries.recordrolevalue;

import java.util.Arrays;
import org.eclipse.incquery.patternlanguage.patternLanguage.Pattern;
import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.incquery.runtime.api.impl.BasePatternMatch;
import org.eclipse.incquery.runtime.exception.IncQueryException;
import org.eclipse.incquery.snapshot.EIQSnapshot.MatchRecord;

/**
 * Pattern-specific match representation of the org.eclipse.incquery.testing.queries.RecordRoleValue pattern, 
 * to be used in conjunction with {@link RecordRoleValueMatcher}.
 * 
 * <p>Class fields correspond to parameters of the pattern. Fields with value null are considered unassigned.
 * Each instance is a (possibly partial) substitution of pattern parameters, 
 * usable to represent a match of the pattern in the result of a query, 
 * or to specify the bound (fixed) input parameters when issuing a query.
 * 
 * @see RecordRoleValueMatcher
 * @see RecordRoleValueProcessor
 * 
 */
public abstract class RecordRoleValueMatch extends BasePatternMatch {
  private MatchRecord fRecord;
  
  private Object fRole;
  
  private static String[] parameterNames = {"Record", "Role"};
  
  private RecordRoleValueMatch(final MatchRecord pRecord, final Object pRole) {
    this.fRecord = pRecord;
    this.fRole = pRole;
    
  }
  
  @Override
  public Object get(final String parameterName) {
    if ("Record".equals(parameterName)) return this.fRecord;
    if ("Role".equals(parameterName)) return this.fRole;
    return null;
    
  }
  
  public MatchRecord getRecord() {
    return this.fRecord;
    
  }
  
  public Object getRole() {
    return this.fRole;
    
  }
  
  @Override
  public boolean set(final String parameterName, final Object newValue) {
    if (!isMutable()) throw new java.lang.UnsupportedOperationException();
    if ("Record".equals(parameterName) ) {
    	this.fRecord = (org.eclipse.incquery.snapshot.EIQSnapshot.MatchRecord) newValue;
    	return true;
    }
    if ("Role".equals(parameterName) && newValue instanceof java.lang.Object) {
    	this.fRole = (java.lang.Object) newValue;
    	return true;
    }
    return false;
    
  }
  
  public void setRecord(final MatchRecord pRecord) {
    if (!isMutable()) throw new java.lang.UnsupportedOperationException();
    this.fRecord = pRecord;
    
  }
  
  public void setRole(final Object pRole) {
    if (!isMutable()) throw new java.lang.UnsupportedOperationException();
    this.fRole = pRole;
    
  }
  
  @Override
  public String patternName() {
    return "org.eclipse.incquery.testing.queries.RecordRoleValue";
    
  }
  
  @Override
  public String[] parameterNames() {
    return RecordRoleValueMatch.parameterNames;
    
  }
  
  @Override
  public Object[] toArray() {
    return new Object[]{fRecord, fRole};
    
  }
  
  @Override
  public String prettyPrint() {
    StringBuilder result = new StringBuilder();
    result.append("\"Record\"=" + prettyPrintValue(fRecord) + ", ");
    result.append("\"Role\"=" + prettyPrintValue(fRole));
    return result.toString();
    
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fRecord == null) ? 0 : fRecord.hashCode()); 
    result = prime * result + ((fRole == null) ? 0 : fRole.hashCode()); 
    return result; 
    
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
    	return true;
    if (!(obj instanceof RecordRoleValueMatch)) { // this should be infrequent				
    	if (obj == null)
    		return false;
    	if (!(obj instanceof IPatternMatch))
    		return false;
    	IPatternMatch otherSig  = (IPatternMatch) obj;
    	if (!pattern().equals(otherSig.pattern()))
    		return false;
    	return Arrays.deepEquals(toArray(), otherSig.toArray());
    }
    RecordRoleValueMatch other = (RecordRoleValueMatch) obj;
    if (fRecord == null) {if (other.fRecord != null) return false;}
    else if (!fRecord.equals(other.fRecord)) return false;
    if (fRole == null) {if (other.fRole != null) return false;}
    else if (!fRole.equals(other.fRole)) return false;
    return true;
  }
  
  @Override
  public Pattern pattern() {
    try {
    	return RecordRoleValueMatcher.factory().getPattern();
    } catch (IncQueryException ex) {
     	// This cannot happen, as the match object can only be instantiated if the matcher factory exists
     	throw new IllegalStateException	(ex);
    }
    
  }
  static final class Mutable extends RecordRoleValueMatch {
    Mutable(final MatchRecord pRecord, final Object pRole) {
      super(pRecord, pRole);
      
    }
    
    @Override
    public boolean isMutable() {
      return true;
    }
  }
  
  static final class Immutable extends RecordRoleValueMatch {
    Immutable(final MatchRecord pRecord, final Object pRole) {
      super(pRecord, pRole);
      
    }
    
    @Override
    public boolean isMutable() {
      return false;
    }
  }
  
}
