package ru.xakaton.bimit.utils;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;


public class UseIdOrGenerate extends UUIDGenerator {
	
	Serializable fieldObject = null;
	
	@Override
	public Serializable generate(SharedSessionContractImplementor  session, Object obj) throws HibernateException {
		
	    if (obj == null) throw new HibernateException(new NullPointerException()) ;

	    if ((((EntityId) obj).getUuid()) == null) {
	        Serializable id = super.generate(session, obj) ;
	        return id;
	    } else {
	        return ((EntityId) obj).getUuid();
	    }
	}
}
