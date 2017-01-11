package mx.nic.rdap.db.impl;

import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.db.exception.RdapDatabaseException;
import mx.nic.rdap.db.spi.DomainSpi;
import mx.nic.rdap.db.struct.SearchResultStruct;

public class DomainDAOImpl implements DomainSpi {

	@Override
	public Long storeToDatabase(Domain domain) throws RdapDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Domain getByName(String domainName, Boolean useNsAsAttribute) throws RdapDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultStruct searchByName(String domainName, Integer resultLimit,
			boolean useNameserverAsDomainAttribute) throws RdapDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultStruct searchByNsName(String nsName, Integer resultLimit, boolean useNsAsAttribute)
			throws RdapDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultStruct searchByNsIp(String ip, Integer resultLimit, boolean useNsAsAttribute)
			throws RdapDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultStruct searchByRegexName(String regexName, Integer resultLimit,
			boolean useNameserverAsDomainAttribute) throws RdapDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultStruct searchByRegexNsName(String regexNsName, Integer resultLimit,
			boolean useNameserverAsDomainAttribute) throws RdapDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResultStruct searchByRegexNsIp(String ip, Integer resultLimit, boolean useNsAsAttribute)
			throws RdapDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existByName(String domainName) throws RdapDatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

}
