#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dao.security;

import java.util.List;

import org.springframework.stereotype.Repository;
import ${package}.entity.security.Resource;
import org.springside.modules.orm.hibernate.HibernateDao;

@Repository
public class ResourceDao extends HibernateDao<Resource, Long> {
	public static final String QUERY_BY_RESOURCETYPE = "from Resource r left join fetch r.authorities WHERE r.resourceType=? ORDER BY r.position ASC";

	@SuppressWarnings("unchecked")
	public List<Resource> getUrlResourceWithAuthorities() {
		return distinct(createQuery(QUERY_BY_RESOURCETYPE, Resource.URL_TYPE)).list();
	}
}