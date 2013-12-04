package com.gdn.venice.hibernate;

import java.util.Properties;
import java.util.Set;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import org.apache.geronimo.gbean.AbstractName;
import org.apache.geronimo.gbean.AbstractNameQuery;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.KernelRegistry;
import org.hibernate.HibernateException;
import org.hibernate.transaction.TransactionManagerLookup;

public class GeronimoTransactionManagerLookup implements
		TransactionManagerLookup {
	public static final String UserTransactionName = "java:comp/UserTransaction";

	public TransactionManager getTransactionManager(Properties props)
			throws HibernateException {
		try {
			Kernel kernel = KernelRegistry.getSingleKernel();
			AbstractNameQuery query = new AbstractNameQuery(
					TransactionManager.class.getName());
			Set<AbstractName> names = kernel.listGBeans(query);
			if (names.size() != 1) {
				throw new IllegalStateException(
						"Expected one transaction manager, not " + names.size());
			}
			AbstractName name = names.iterator().next();
			TransactionManager transMg = (TransactionManager) kernel
					.getGBean(name);
			return (TransactionManager) transMg;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println();
			throw new HibernateException(
					"Geronimo Transaction Manager Lookup Failed", e);
		}
	}

	public String getUserTransactionName() {
		return UserTransactionName;
	}

	@Override
	public Object getTransactionIdentifier(Transaction arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
