#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.rs.server;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dozer.DozerBeanMapper;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ${package}.entity.user.User;
import ${package}.rs.dto.UserDTO;
import ${package}.service.user.UserManager;

/**
 * 
 * @author calvin
 */
@Path("/users")
public class UserResourceService {

	private static Logger logger = LoggerFactory.getLogger(UserResourceService.class);

	@Autowired
	private UserManager userManager;
	@Autowired
	private DozerBeanMapper dozer;

	/**
	 * 获取所有用户.
	 */
	@GET
	@Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<UserDTO> getAllUser() {
		try {
			List<User> entityList = userManager.getAllUser();
			List<UserDTO> dtoList = new ArrayList<UserDTO>();
			for (User userEntity : entityList) {
				dtoList.add(dozer.map(userEntity, UserDTO.class));
			}
			return dtoList;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 获取用户.
	 */
	@GET
	@Path("{id}")
	@Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public UserDTO getUser(@PathParam("id") Long id) {
		try {
			User entity = userManager.getUser(id);
			UserDTO dto = dozer.map(entity, UserDTO.class);
			return dto;
		} catch (ObjectNotFoundException e) {
			String message = "用户不存在(id:" + id + ")";
			logger.error(message, e);
			throw buildException(Status.NOT_FOUND, message);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 创建用户.
	 */
	@POST
	@Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces( { MediaType.TEXT_PLAIN })
	public String createUser(UserDTO user) {
		try {
			User userEntity = dozer.map(user, User.class);
			userManager.saveUser(userEntity);
			return userEntity.getId().toString();
		} catch (ConstraintViolationException e) {
			String message = "新建用户参数存在唯一性冲突(用户:" + user + ")";
			logger.error(message, e);
			throw buildException(Status.BAD_REQUEST, message);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 创建含出错信息的WebApplicationException.
	 */
	private WebApplicationException buildException(Status status, String message) {
		return new WebApplicationException(Response.status(status).entity(message).build());
	}
}
