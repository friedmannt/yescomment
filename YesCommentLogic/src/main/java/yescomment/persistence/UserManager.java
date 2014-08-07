package yescomment.persistence;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import yescomment.model.User;
import yescomment.util.SHA256Encoder;

@Stateless
public class UserManager extends AbstractEntityManager<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@PersistenceContext(unitName = "YesCommentModel")
	protected EntityManager em;

	public UserManager() {
		super(User.class);

	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public User getUserByName(@NotNull final String userName) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> r = cq.from(User.class);
		cq.select(r);
		cq.where(cb.equal(r.get("userName"), userName));
		TypedQuery<User> typedQuery = em.createQuery(cq);

		List<User> users = typedQuery.getResultList();
		assert users.size() <= 1;
		if (users.isEmpty()) {
			return null;
		} else {
			return users.get(0);
		}

	}
	
	public User registerUser(@NotNull final String userName,@NotNull final String password) {
		User user=new User();
		user.setAdministrator(false);
		user.setUserName(userName);
		try {
			user.setEncodedPassword(SHA256Encoder.encodeHex(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return create(user);
	}

}
